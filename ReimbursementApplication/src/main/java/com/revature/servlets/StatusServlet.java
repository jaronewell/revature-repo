package com.revature.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.pojos.Employee;

/**
 * Servlet implementation class StatusServlet
 */
public class StatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		
		Employee emp = (Employee) sess.getAttribute("employee");
		
		if(emp == null) {
			response.sendRedirect("login");
			return;
		}
		else if(emp.getType() == Employee.EmployeeType.BenCo) {
			System.out.println("Benco Status");
			
		}
		else if(emp.getType() == Employee.EmployeeType.DepartmentHead) {
			System.out.println("Head Status");
			
		}
		else if(emp.getType() == Employee.EmployeeType.Supervisor) {
			System.out.println("Supervisor Status");
			
		}
		else {
			System.out.println("Employee Status");
			
		}
		
		response.sendRedirect("html/reimbursementstatus.html");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
