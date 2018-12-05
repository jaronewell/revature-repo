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
import com.revature.service.EmployeeService;

/**
 * Servlet implementation class EmployeeServlet
 */
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	EmployeeService eServ = new EmployeeService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getRequestURI();
		ObjectMapper om = new ObjectMapper();
		
		if("/ReimbursementApplication/api/employee".equals(path)) {
			List<Employee> employeeList = eServ.readAllEmployees();
			String employeeListJson = om.writeValueAsString(employeeList);
			
			response.getWriter().write(employeeListJson);
		}
		else if("/ReimbursementApplication/api/employee/current".equals(path)) {
			HttpSession sess = request.getSession();
			
			Employee currentEmp = (Employee) sess.getAttribute("employee");
			String currentEmpJson = om.writeValueAsString(currentEmp);
			
			System.out.println(currentEmpJson);
			
			response.getWriter().write(currentEmpJson);
		}
		else {
			String username = path.substring(path.lastIndexOf('/')+1);
			
			Employee emp = eServ.readEmployee(username);
			String empJson = om.writeValueAsString(emp);
			
			response.getWriter().write(empJson);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
