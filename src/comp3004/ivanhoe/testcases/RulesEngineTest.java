package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.*;
import comp3004.ivanhoe.Card.CardColour;

public class RulesEngineTest {
	RulesEngine e;
	
	@Before
	public void setUp() throws Exception {
		e = new RulesEngine(5);
	}
	
	@Test
	public void choosefirstTournement(){
		e.registerThread(10);
		e.registerThread(11);
		e.registerThread(12);
		e.registerThread(13);
		e.registerThread(14);
		
		long a = e.chooseFirstTournament();
		assertTrue(a >= 10 && a <= 14);
	}
	
	@Test
	public void InitTourneyColours(){
		choosefirstTournement();
		
		e.initializeTournementColour(CardColour.Blue);
		assertEquals(CardColour.Blue, e.getPlayerById(10).getDisplay().getColour());
	}
	
	@Test
	public void dealHand(){
		InitTourneyColours();
		e.dealHand();
		System.out.print(e.getPlayerById(11).getHandSize());
		assertEquals(8, e.getPlayerById(11).getHandSize());
	}
}