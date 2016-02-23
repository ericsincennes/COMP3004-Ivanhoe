package comp3004.ivanhoe.testcases;


import comp3004.ivanhoe.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ClientTest {
	
	@Test
	public void connectWithoutServer(){	//no server
		Client c = new Client();
		boolean x = c.connect(2244); //no server existing should fail
		
		assertTrue(x == true);
	}
	
	@Test
	public void connecteToEmptyServer(){	//server /w no code
		Client c = new Client();
		Server s;
		
		boolean x = c.connect(2244); //no server code
		assertTrue(x == true);
	}
	
	@Test
	public void connectToCodeServer(){
		Client c = new Client();
		Server s = new Server();
		
		boolean x = c.connect(2244); //no server code
		assertTrue(x == true);
	}

}
