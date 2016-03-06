package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import comp3004.ivanhoe.*;

public class ServerTest {

	@Test
	public void StartServer() {	
		String LogDirectory = (System.getProperty("user.dir") + "/src/Logs/");
		Server s = new Server();
		File f = new File(LogDirectory + "ServerLog");
		
		assertTrue(f.exists());
	}


	@Test
	public void ConnectOneClient(){
		Server s = new Server();
		Client c = new Client();
		
		boolean b = c.connect(2244);
		
		assertTrue(b == true);
		
	}
	
	@Test
	public void ConnectTwoClients(){
		Server s = new Server();
		Client c1 = new Client();
		Client c2 = new Client();
		
		boolean b1 = c1.connect(2244);
		boolean b2 = c2.connect(2244);
		
		
		assertTrue(b1 == true && b2 == true);
	}
	
	@Test
	public void ConnectThreeClients(){
		Server s = new Server();
		Client c1 = new Client();
		Client c2 = new Client();
		Client c3 = new Client();
		
		boolean b1 = c1.connect(2244);
		boolean b2 = c2.connect(2244);
		boolean b3 = c3.connect(2244);
		
		assertTrue(b1 == true && b2 == true && b3 == true);
	}
	
	@Test
	public void ConnectFourClients(){
		Server s = new Server();
		Client c1 = new Client();
		Client c2 = new Client();
		Client c3 = new Client();
		Client c4 = new Client();
		
		boolean b1 = c1.connect(2244);
		boolean b2 = c2.connect(2244);
		boolean b3 = c3.connect(2244);
		boolean b4 = c4.connect(2244);
		
		assertTrue(b1 == true && b2 == true && b3 == true && b4 == true);
	}
	
	@Test
	public void ConnectFiveClients(){
		Server s = new Server();
		Client c1 = new Client();
		Client c2 = new Client();
		Client c3 = new Client();
		Client c4 = new Client();
		Client c5 = new Client();
		
		boolean b1 = c1.connect(2244);
		boolean b2 = c2.connect(2244);
		boolean b3 = c3.connect(2244);
		boolean b4 = c4.connect(2244);
		boolean b5 = c5.connect(2244);
		
		assertTrue(b1 == true && b2 == true && b3 == true && b4 == true && b5 == true);
	}
	
	@Test
	public void testMaxConnections(){
		Server s = new Server();
		Client c1 = new Client();
		Client c2 = new Client();
		Client c3 = new Client();
		Client c4 = new Client();
		Client c5 = new Client();
		Client c6 = new Client();
		
		boolean b1 = c1.connect(2244);
		boolean b2 = c2.connect(2244);
		boolean b3 = c3.connect(2244);
		boolean b4 = c4.connect(2244);
		boolean b5 = c5.connect(2244);
		boolean b6 = c6.connect(2244);
		
		assertTrue(b6 == false);
	}
}