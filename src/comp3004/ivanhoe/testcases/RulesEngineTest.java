package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.Test;

import comp3004.ivanhoe.*;

public class RulesEngineTest {
	
	@Test
	public void RegisterThread(){
		Client c = new Client();		
		assertTrue(c.getPlayerNum() != -1);
	}
	
	@Test
	public void choosefirstTournement(){
		RulesEngine e = new RulesEngine(5);
		e.registerThread(10);
		e.registerThread(11);
		e.registerThread(12);
		e.registerThread(13);
		e.registerThread(14);
		
		long a = e.firstTournament();
		assertTrue(a != -1);
	}
	
	@Test
	public void getTournementChoice(){
		
	}
	
	
	
}