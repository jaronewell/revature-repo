package com.banking;

import org.apache.log4j.Logger;

public class JavaLog4j {

	final static Logger logger = Logger.getLogger(Main.class);
	
	//info, fatal, error, warning
	
	public static void main(String[] args) {

		if(logger.isInfoEnabled()) {
			logger.info("this is info");
		}
		
		logger.info("this is info");
		logger.error("error", new Exception());
		logger.fatal("fatal message");
		logger.warn("warning");
	}
	
}
