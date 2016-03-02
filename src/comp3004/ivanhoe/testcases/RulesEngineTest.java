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
		e = new RulesEngine(2);
		e.registerThread(1);
		e.registerThread(2);
		//e.registerThread(3);
		//e.registerThread(4);
		//e.registerThread(5);
	}
	
	@Test
	public void choosefirstTournement(){
		long a = e.chooseFirstTournament();
		assertTrue(a >= 1 && a <= 2);
	}
	
	@Test
	public void InitTourneyColours(){
		choosefirstTournement();
		
		e.initializeTournamentColour(CardColour.Blue);
		assertEquals(CardColour.Blue, e.getPlayerById(1).getDisplay().getColour());
	}
	
	@Test
	public void dealHand(){
		e.dealHand();
		assertEquals(8, e.getPlayerById(2).getHandSize());
	}
		
	public void validatePlay(){
		e.initializeTournamentColour(CardColour.Blue);
		e.getPlayerById(1).addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(e.validatePlay("Blue 4", (long) 1));
	}
	
	@Test
	public void playCard(){
		Player p = e.getPlayerById(2);
		e.initializeTournamentColour(CardColour.Blue);
		p.addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(e.playCard("Blue 4", p.getid()));
	}
	
	@Test
	public void playTurn(){
		e.chooseFirstTournament();
		e.initializeTournamentColour(CardColour.Red);
		e.dealHand();
		
		Player p = e.getPlayerById(2);
		//e.giveCardToPlayer(p.getid());
		p.addCard(new ColourCard(CardColour.Red, 3));
		
		assertTrue(e.playCard("Red 3", p.getid()));
		assertTrue(e.playCard("Maiden",p.getid()));
		System.out.println(p.getDisplay().calculatePoints()); 
		assertEquals(p.getDisplay().calculatePoints(),9);
		assertTrue(e.canEndTurn(p.getid()));
		//assertFalse(p.getPlaying());
		
	}
	
}