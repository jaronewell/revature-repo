package com.revature.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.revature.dao.RequestDao;
import com.revature.dao.RequestDaoPostgres;
import com.revature.pojos.Employee;
import com.revature.pojos.Request;
import com.revature.pojos.Request.EventType;
import com.revature.pojos.Request.GradingFormat;

public class RequestService {

	RequestDao rDao = new RequestDaoPostgres();
	
	public List<Request> readAllForEmployee(Employee emp) {
		return rDao.readAllForEmployee(emp);
	}
	
	public List<Request> readAllRequests(){
		return rDao.readAll();
	}
	
	public Request readRequest(int id) {
		return rDao.getRequestById(id);
	}
	
	public void makeRequest(HttpServletRequest request, Employee emp) {
		
		DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		LocalDate startDate = LocalDate.parse(request.getParameter("startDate"), DATEFORMATTER);
		LocalDate endDate = LocalDate.parse(request.getParameter("endDate"), DATEFORMATTER);
		String location = request.getParameter("location");
		GradingFormat gradingFormat = getGradingFormat(request.getParameter("gradingFormat"));
		EventType eventType = getEventType(request.getParameter("eventType"));
		double cost = Double.parseDouble(request.getParameter("cost"));
		String description = request.getParameter("description");
		String justification = request.getParameter("justification");
		int missedHours = Integer.parseInt(request.getParameter("missedHours"));
		
		int status = getStatus(request);
		
		Request req = new Request(0, emp.getEmployeeId(), LocalDate.now(), startDate, endDate, location, description, cost, gradingFormat, eventType, justification, status, "", missedHours);
		
		System.out.println(req);
		rDao.create(req);
		
		//Add files to file table
	}
	
	private int getStatus(HttpServletRequest request) {
		
		int status = 0;
		
		if(Boolean.parseBoolean(request.getParameter("bencoApproval"))){
	        status = 3;
	    }
	    else if (Boolean.parseBoolean(request.getParameter("departmentHeadApproval"))){
	        status = 2;
	    }
	    else if (Boolean.parseBoolean(request.getParameter("supervisorApproval"))){
	        status = 1;
	    }

		return status;
	}

	private GradingFormat getGradingFormat(String formatString) {
		
		GradingFormat gf = null;
		
		switch(formatString) {
		case "Graded":
			gf = GradingFormat.Graded;
			break;
		case "Presentation":
			gf = GradingFormat.Presentation;
			break;
		}
		
		return gf;
	}
	
	private EventType getEventType(String typeString) {
		
		EventType type = null;
		
		switch(typeString) {
		case "University Course (80%)":
			type = EventType.UniversityCourse;
			break;
		case "Seminar (60%)":
			type = EventType.Seminar;
			break;
		case "Certification Preparation Classes (75%)":
			type = EventType.CertificationPreparationClass;
			break;
		case "Certification (100%)":
			type = EventType.Certification;
			break;
		case "Technical Training (90%)":
			type = EventType.TechnicalTraining;
			break;
		case "Other (30%)":
			type = EventType.Other;
			break;
		}
		
		return type;
	}
}
