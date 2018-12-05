package com.revature.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.pojos.Employee;
import com.revature.service.AuthService;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 2014093911531175277L;
	private AuthService aServ = new AuthService();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		req.getSession().invalidate();
		
		resp.sendRedirect("html/login.html");
//		RequestDispatcher rd = req.getRequestDispatcher("html/login.html");
//		rd.forward(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		Employee emp = aServ.validateEmployee(username, password);
		
		if(emp != null) {
			System.out.println("Employee found: " + emp.getFirstName() + " " + emp.getLastName());
			//set up session
			HttpSession sess = req.getSession();
			sess.setAttribute("employee", emp);
			resp.sendRedirect("status");
//			RequestDispatcher rd = req.getRequestDispatcher("request");
//			rd.forward(req, resp);
		}
		else {
			resp.sendRedirect("login");
			resp.getWriter().write("<h1>You have logged in incorrectly!!</h1>");
		}
	}
}
