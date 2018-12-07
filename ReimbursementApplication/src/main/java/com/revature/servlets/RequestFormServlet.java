package com.revature.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.pojos.Employee;
import com.revature.pojos.Employee.EmployeeType;
import com.revature.service.RequestService;

public class RequestFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	RequestService rServ = new RequestService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		
		Employee emp = (Employee) sess.getAttribute("employee");
		
		String username = null;
		
		if(emp != null) {
			username = emp.getUsername();
		}
		
		if(username == null) {
			response.sendRedirect("login");
		}
		else if(emp.getType() != EmployeeType.Employee) {
			response.sendRedirect("status");
		}
		else {
			response.sendRedirect("./html/requestreimbursement.html");
//			RequestDispatcher rd = request.getRequestDispatcher("./html/requestreimbursement.html");
//			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		Employee emp = (Employee) sess.getAttribute("employee");
		
		System.out.println("In RequestForm doPost Employee: " + emp);
		rServ.makeRequest(request, emp);
	}

}
