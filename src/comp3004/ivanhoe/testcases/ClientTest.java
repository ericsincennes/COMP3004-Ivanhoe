package comp3004.ivanhoe.testcases;


import comp3004.ivanhoe.*;

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Test;

public class ClientTest {
	
	@Test
	public void test1(){	//no server
		Client c = new Client();
		String host = "localhost";
		
		boolean x = c.connect(host, 2244); //no server existing should be false
		
		assertTrue(x == true);
		
		
	}
	
	@Test
	public void test2(){	//server /w no code
		Client c = new Client();
		Server s = new Server();
		String host = "localhost";
		
		boolean x = c.connect(host, 2244); //no server code
		
		assertTrue(x == true);
	}
	
	@Test
	public void test3(){
		Client c = new Client();
		Server s = new Server();
		String host = "localhost";
		
		s.startup(2244);
		
		boolean x = c.connect(host, 2244); //no server code
		assertTrue(x == true);
	}
}
