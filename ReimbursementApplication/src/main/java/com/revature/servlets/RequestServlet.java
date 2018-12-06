package com.revature.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.pojos.Employee;
import com.revature.pojos.Request;
import com.revature.service.EmployeeService;
import com.revature.service.RequestService;

/**
 * Servlet implementation class RequestServlet
 */
public class RequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	RequestService rServ = new RequestService();
	EmployeeService eServ = new EmployeeService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getRequestURI();
		ObjectMapper om = new ObjectMapper();
		
		System.out.println();
		
		if("/ReimbursementApplication/api/request".equals(path)) {
			List<Request> requestList = rServ.readAllRequests();
			String requestListJson = om.writeValueAsString(requestList);
			
			response.getWriter().write(requestListJson);
		}
		else {
			String username = path.substring(path.lastIndexOf('/')+1);
			
			Employee emp = eServ.readEmployee(username);
			List<Request> requestList = rServ.readAllForEmployee(emp);
			
			String requestListJson = om.writeValueAsString(requestList);
			
			System.out.println(requestListJson);
			response.getWriter().write(requestListJson);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
