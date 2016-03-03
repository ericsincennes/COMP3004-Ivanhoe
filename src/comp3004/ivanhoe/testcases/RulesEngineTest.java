package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.lang.reflect.GenericArrayType;

import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.*;
import comp3004.ivanhoe.Card.CardColour;

public class RulesEngineTest {
	RulesEngine rules;
	
	@Before
	public void setUp() throws Exception {
		rules = new RulesEngine(2);
		rules.registerThread(1);
		rules.registerThread(2);
		//e.registerThread(3);
		//e.registerThread(4);
		//e.registerThread(5);
	}
	
	@Test
	public void choosefirstTournement(){
		long a = rules.chooseFirstTournament();
		assertTrue(a >= 1 && a <= 2);
	}
	
	@Test
	public void InitTourneyColours(){
		choosefirstTournement();
		
		rules.initializeTournamentColour(CardColour.Blue);
		assertEquals(CardColour.Blue, rules.getPlayerById(1).getDisplay().getColour());
	}
	
	@Test
	public void dealHand(){
		rules.dealHand();
		assertEquals(8, rules.getPlayerById(2).getHandSize());
	}
		
	public void validatePlay(){
		rules.initializeTournamentColour(CardColour.Blue);
		rules.getPlayerById(1).addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(rules.validatePlay("Blue 4", (long) 1));
	}
	
	@Test
	public void playCard(){
		Player p = rules.getPlayerById(2);
		rules.initializeTournamentColour(CardColour.Blue);
		p.addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(rules.playCard("Blue 4", p.getid()));
	}
	 
	@Test
	public void playTurn(){
		rules.chooseFirstTournament();
		rules.initializeTournamentColour(CardColour.Red);
		rules.dealHand();
		Player p;
		
		p = rules.getPlayerList().get(0);
		//System.out.println(p.getid());
		rules.startTurn(p.getid()); //draw card
		assertEquals(p.getHandSize(), 9);
		assertTrue(rules.playCard("Squire", p.getid()));
		assertEquals(p.getDisplay().calculatePoints(), 2);
		assertTrue(rules.endTurn(p.getid()));
		
		//turn 2 - testing end turn checks after plays have already been made
		p = rules.getPlayerList().get(0);
		//System.out.println(p.getid());
		assertTrue(rules.playCard("Squire", p.getid()));
		assertFalse(rules.endTurn(p.getid()));
		assertTrue(rules.playCard("Squire", p.getid()));
		assertTrue(rules.endTurn(p.getid()));
		
	}
	
	@Test
	public void tournementTest(){
		rules.chooseFirstTournament();
		rules.initializeTournamentColour(CardColour.Purple);
		rules.dealHand();
		Player p;
		int turn = 0;
		
		while(turn < 20){
			p = rules.getPlayerList().get(0);
			rules.startTurn(p.getid());
			
			do{
				rules.playCard("Squire", p.getid());
				if(p.getHandSize() == 0){
					Long a = rules.withdrawPlayer(p.getid());
					if(a != null){
						rules.getPlayerById(a).recieveToken(rules.getTournamentColor());
					}
				}
			}while(p.getPlaying() && (!rules.endTurn(p.getid())));
			
			rules.endTurn(p.getid());
			
			turn++;
		}
	}
	
}