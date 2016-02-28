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
		e.registerThread(10);
		e.registerThread(11);
		e.registerThread(12);
		e.registerThread(13);
		e.registerThread(14);
	}
	
	@Test
	public void choosefirstTournement(){
		long a = e.chooseFirstTournament();
		assertTrue(a >= 10 && a <= 14);
	}
	
	@Test
	public void InitTourneyColours(){
		choosefirstTournement();
		
		e.initializeTournamentColour(CardColour.Blue);
		assertEquals(CardColour.Blue, e.getPlayerById(10).getDisplay().getColour());
	}
	
	@Test
	public void dealHand(){
		e.dealHand();
		assertEquals(8, e.getPlayerById(11).getHandSize());
	}
		
	public void validatePlay(){
		e.initializeTournamentColour(CardColour.Blue);
		e.getPlayerById(11).addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(e.validatePlay("Blue 4", (long) 11));
	}
	
	@Test
	public void playCard(){
		Player p = e.getPlayerById(11);
		e.initializeTournamentColour(CardColour.Blue);
		p.addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(e.playCard("Blue 4",(long) 11));
	}
	
	@Test
	public void playTurn(){
		e.chooseFirstTournament();
		e.initializeTournamentColour(CardColour.Red);
		e.dealHand();
		
		Player p = e.getPlayerById(11);
		//e.giveCardToPlayer(p.getid());
		p.addCard(new ColourCard(CardColour.Red, 3));
		
		assertTrue(e.playCard("Red 3", p.getid()));
		assertTrue(e.endTurn(p.getid()));
		
	}
	
}