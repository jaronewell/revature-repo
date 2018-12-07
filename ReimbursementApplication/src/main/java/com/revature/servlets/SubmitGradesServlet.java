package com.revature.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.revature.pojos.Request;
import com.revature.service.RequestService;

/**
 * Servlet implementation class SubmitGradesServlet
 */

@WebServlet("/upload")
@MultipartConfig
public class SubmitGradesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	RequestService rServ = new RequestService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Part filePart = request.getPart("gradeFile");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		InputStream fileContent = filePart.getInputStream();
		
		System.out.println(fileName);
		
		HttpSession sess = request.getSession();
		
		Integer reqId = (Integer) request.getSession().getAttribute("requestId");
		
		Request req = rServ.readRequest(reqId);
		
		req.setStatus(req.getStatus()+1);
		rServ.updateRequest(req);
		
		response.sendRedirect("/html/reimbursementstatus.html");
	}

}
