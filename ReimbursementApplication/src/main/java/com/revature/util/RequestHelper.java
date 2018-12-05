package com.revature.util;

import javax.servlet.http.HttpServlet;

import com.revature.servlets.LoginServlet;
import com.revature.servlets.PageNotFoundServlet;

public class RequestHelper {

	private static HttpServlet loginServlet = new LoginServlet();
	
	private static HttpServlet pnfServlet = new PageNotFoundServlet();
	
	public HttpServlet dispatchRequest(String path) {
		
		HttpServlet nextServlet = null;
		
		switch(path) {
		case "/FrontController/login":
			nextServlet = loginServlet;
			break;
			
		default:
			nextServlet = pnfServlet;
			
		}
		
		return nextServlet;
	}
	
}
