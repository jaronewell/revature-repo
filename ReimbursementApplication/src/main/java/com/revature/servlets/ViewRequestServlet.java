package com.revature.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.pojos.Request;

/**
 * Servlet implementation class ViewRequestServlet
 */
public class ViewRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		response.sendRedirect("html/viewrequest.html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		doGet(request, response);
	}

//	ObjectMapper om = new ObjectMapper();
//	
//	Request req = (Request) request.getSession().getAttribute("request");
//	String reqJson = om.writeValueAsString(req);
//	
//	response.getWriter().write(reqJson);
//	
}
