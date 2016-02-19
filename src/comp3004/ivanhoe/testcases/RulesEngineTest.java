package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.Test;

import comp3004.ivanhoe.*;

public class RulesEngineTest {
	
	@Test
	public void RegisterThread(){
		Client c = new Client();
		
		System.out.println(c.getPlayerNum());
		assertTrue(c.getPlayerNum() != -1);
	}
	
	@Test
	public void chooseDealer(){
		
	}
}
