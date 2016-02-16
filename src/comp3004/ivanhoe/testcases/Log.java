package src.comp3004.ivanhoe.testcases;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	Logger logger;
	FileHandler fh;

	public Log(String loggername, String classname){
		logger = Logger.getLogger(loggername);
		try {  

			//configure the logger  
			fh = new FileHandler("C:/Users/Shray/git/3004-Ivanhoe/src/comp3004/ivanhoe/testcases/" + classname + ".log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);

			logger.info("Logger Configured");  
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}

	public void logmsg(String message){
		logger.info(message);
	}
	
	public static void main(String[] args) {}
	
}
