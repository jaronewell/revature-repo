package com.revature.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.pojos.Request;
import com.revature.service.RequestService;

/**
 * Servlet implementation class ViewRequestServlet
 */
public class ViewRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	RequestService rServ = new RequestService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper om = new ObjectMapper();
		
		Integer reqId = (Integer) request.getSession().getAttribute("requestId");
		
		Request req = rServ.readRequest(reqId);
		String reqJson = om.writeValueAsString(req);
		
		response.getWriter().write(reqJson);
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper om = new ObjectMapper();
		
		HttpSession sess = request.getSession();
		Integer reqId = (Integer) request.getSession().getAttribute("requestId");
		
		Request req = rServ.readRequest(reqId);
		
		String decisionJson = request.getReader().readLine();
		
		String decision = om.readValue(decisionJson, String.class);
		
		if("approve".equals(decision)) {
			
			req.setStatus(req.getStatus()+1);
			rServ.updateRequest(req);
			
		}
		else if("reject".equals(decision)) {
			
			req.setStatus(-1);
			rServ.updateRequest(req);
		}
			
		
		
		
	}

}
