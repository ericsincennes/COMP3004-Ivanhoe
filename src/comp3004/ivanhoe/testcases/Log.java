package comp3004.ivanhoe.testcases;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	Logger logger;
	FileHandler fh;
	private final static String LogDirectory = (System.getProperty("user.dir") + "/src/Logs/");

	public Log(String loggername, String classname){
		logger = Logger.getLogger(loggername);
		try {  

			//configure the logger  
			fh = new FileHandler(LogDirectory + classname + ".log");
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
