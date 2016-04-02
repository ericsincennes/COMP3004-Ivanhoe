package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.lang.reflect.GenericArrayType;

import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.*;
import comp3004.ivanhoe.ActionCard;
import comp3004.ivanhoe.Card.CardColour;

public class RulesEngineTest {
	RulesEngine rules;
	
	@Before
	public void setUp() throws Exception {
		rules = RulesEngine.testRuleEngine(3);
		rules.registerThread(1);
		rules.registerThread(2);
		rules.registerThread(3);
		//rules.registerThread(4);
		//rules.registerThread(5);
	}
	
	@Test
	public void tournamentSetup() {
		rules.initFirstTournament();
		assertFalse(rules.isTournamentRunning());
		rules.initTournament();
		assertEquals(8, rules.getPlayerById(1).getHandSize());
		assertEquals(8, rules.getPlayerById(2).getHandSize());
		assertEquals(8, rules.getPlayerById(3).getHandSize());
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		assertEquals(CardColour.Blue, rules.getPlayerById(1).getDisplay().getColour());
	}
	
	@Test
	public void cantStartTournament() {
		assertFalse(rules.isTournamentRunning());
		rules.initTournament();
		Player nextPlayer = rules.getPlayerList().get(1);
		rules.getPlayerList().get(0).getHand().discardHand();
		rules.getPlayerList().get(0).addCard(new ActionCard("Unhorse"));
		assertFalse(rules.canStartTournament(rules.getPlayerList().get(0).getID()));
		assertEquals(rules.getPlayerList().get(0).getHand().getNumCards(),1);
		rules.failInitTournamentColour();
		assertEquals(nextPlayer, rules.getPlayerList().get(0));
		
	}
		
	@Test
	public void validatePlay(){
		tournamentSetup();
		rules.getPlayerById(1).addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(rules.validatePlay("Blue 4", (long) 1));
	}
	
	@Test
	public void playCard(){
		tournamentSetup();
		Player p = rules.getPlayerById(2);
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().getLastPlayed().getCardName(), "Squire 2"); //playing supporter card
		
		//blue tournament
		p.addCard(new ColourCard(CardColour.Red, 3));
		assertFalse(rules.playCard(p.getHandSize()-1, p.getID())); //colour restriction
		p.addCard(new ColourCard(CardColour.Blue, 4));
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().getLastPlayed().getCardName(), "Blue 4"); //playing on colour
		
		//red tournament
		rules.initializeTournamentColour(p.getID(), CardColour.Red);
		p.addCard(new ColourCard(CardColour.Yellow, 3));
		assertFalse(rules.playCard(p.getHandSize()-1, p.getID())); //colour restriction
		p.addCard(new ColourCard(CardColour.Red, 4));
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().getLastPlayed().getCardName(), "Red 4"); //playing on colour
		
		//yellow tournament
		rules.initializeTournamentColour(p.getID(), CardColour.Yellow);
		p.addCard(new ColourCard(CardColour.Green, 1));
		assertFalse(rules.playCard(p.getHandSize()-1, p.getID())); //colour restriction
		p.addCard(new ColourCard(CardColour.Yellow, 4));
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().getLastPlayed().getCardName(), "Yellow 4"); //playing on colour
		
		//green tournament
		rules.initializeTournamentColour(p.getID(), CardColour.Green);
		p.addCard(new ColourCard(CardColour.Purple, 5));
		assertFalse(rules.playCard(p.getHandSize()-1, p.getID())); //colour restriction
		p.addCard(new ColourCard(CardColour.Green, 1));
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().getLastPlayed().getCardName(), "Green 1"); //playing on colour
		
		//purple tournament
		rules.initializeTournamentColour(p.getID(), CardColour.Purple);
		p.addCard(new ColourCard(CardColour.Blue, 3));
		assertFalse(rules.playCard(p.getHandSize()-1, p.getID())); //colour restriction
		p.addCard(new ColourCard(CardColour.Purple, 7));
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().getLastPlayed().getCardName(), "Purple 7"); //playing on colour
	}
	 
	@Test
	public void playTurn(){
		tournamentSetup();
		Player p, p2;
		p = rules.getPlayerList().get(0);
		p2 = rules.getPlayerList().get(1);
		//System.out.println(p.getid());
		rules.startTurn(p.getID()); //draw card
		assertEquals(p.getHandSize(), 9);
		assertFalse(rules.canEndTurn(p.getID()));
		assertTrue(rules.playCard(0, p.getID()));
		assertEquals(p.getDisplay().calculatePoints(), 2);
		assertEquals(p.getHandSize(), 8);
		assertTrue(rules.endTurn(p.getID()));
		
		//turn 2 - testing end turn checks after plays have already been made
		p = rules.getPlayerList().get(0);
		//System.out.println(p.getid());
		assertEquals(p, p2);
		rules.startTurn(p.getID());
		assertEquals(9, p.getHandSize());
		assertTrue(rules.playCard(0, p.getID()));
		assertEquals(8, p.getHandSize());
		assertFalse(rules.endTurn(p.getID()));
		p.addCard(new ColourCard(CardColour.Blue, 3));
		assertEquals(9, p.getHandSize());
		assertTrue(rules.playCard(p.getHandSize()-1, p.getID()));
		assertEquals(p.getDisplay().calculatePoints(), 5);
		assertEquals(p.getHandSize(), 8);
		assertTrue(rules.endTurn(p.getID()));
		
	}
	
	@Test
	public void withdrawTest() {
		tournamentSetup();
		Player p1, p2, p3;
		p1 = rules.getPlayerList().get(0);
		p2 = rules.getPlayerList().get(1);
		p3 = rules.getPlayerList().get(2);
		
		//withdraw should not set a high score
		rules.startTurn(rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		assertFalse(rules.withdrawPlayer(p1.getID()));
		assertTrue(rules.withdrawCleanup(p1.getID())<0); //no winner
		assertEquals(rules.getHighestScore(),0);
		
		//withdrawn player should be skipped over
		assertEquals(rules.getPlayerList().get(0), p2);
		rules.startTurn(rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		rules.endTurn(rules.getPlayerList().get(0).getID());
		assertEquals(rules.getPlayerList().get(0), p3);
		rules.startTurn(rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		rules.playCard(0, rules.getPlayerList().get(0).getID());
		rules.endTurn(rules.getPlayerList().get(0).getID());
		assertEquals(rules.getPlayerList().get(0), p1);
		assertFalse(rules.startTurn(rules.getPlayerList().get(0).getID()));
		
		//withdrawing should return the winner in withdrawCleanup
		assertEquals(rules.getPlayerList().get(0), p2);
		rules.startTurn(rules.getPlayerList().get(0).getID());
		assertFalse(rules.withdrawPlayer(p2.getID()));
		assertEquals(p3.getID(), rules.withdrawCleanup(p2.getID()));
		
		//withdrawing with maiden should cause withdrawPlayer to return true, so player can lose point
		rules.initTournament();
		rules.initializeTournamentColour(rules.getPlayerList().get(0).getID(), CardColour.Blue);
		assertEquals(rules.getPlayerList().get(0), p3);
		rules.startTurn(p3.getID());
		p3.addCard(new SupporterCard(6));
		rules.playCard(p3.getHandSize()-1, p3.getID());
		assertEquals(p3.getDisplay().getLastPlayed().getCardName(), "Maiden");
		assertTrue(rules.withdrawPlayer(p3.getID()));
		assertEquals(-1, rules.withdrawCleanup(p2.getID())); //return -1 because no winner yet
	}
	
	/** 
	 * used to give a player point cards of each colour plus supporter cards
	 * will also remove previous hand from game 
	*/
	private void addPointsCards(Player p) {
		p.getHand().discardHand();
		for (int i=0; i<3; i++) {
			p.addCard(new ColourCard(CardColour.Purple, 7));
			p.addCard(new ColourCard(CardColour.Red, i+3));
			p.addCard(new ColourCard(CardColour.Blue, (i == 2) ? 5 : i+2)); //2,3,5
			p.addCard(new ColourCard(CardColour.Yellow, i+2));
			p.addCard(new ColourCard(CardColour.Green, 1));
		}
		p.addCard(new SupporterCard(2));
		p.addCard(new SupporterCard(3));
		p.addCard(new SupporterCard(6));
	}
	
	@Test
	public void blueTournamentTest(){
		tournamentSetup();
		Player p1 = rules.getPlayerList().get(0);
		Player p2 = rules.getPlayerList().get(1);
		Player p3 = rules.getPlayerList().get(2);
		Player p;
		
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		rules.withdrawCleanup(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		assertTrue(rules.endTurn(p.getID()));
		assertEquals(rules.getHighestScore(), 8); // 1 squire + 3 squire = 8 pts
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		Long winner = rules.withdrawCleanup(p.getID());
		assertEquals(winner, Long.valueOf(p1.getID()));
	
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //winner is now 1st in list
		p.recieveToken(rules.getTournamentColour());
		assertEquals(p.getTokenCount(),1);
		assertFalse(rules.isTournamentRunning());
		
		//cleanup
		int beforecleanupDiscard = rules.getDeck().viewDiscard().size();
		rules.roundCleanup();
		assertFalse(rules.isColourChosen());
		assertEquals(0, p1.getDisplay().getCards().size());
		assertEquals(0, p1.getDisplay().getActionCards().size());
		assertEquals(0, p2.getDisplay().getCards().size());
		assertEquals(0, p2.getDisplay().getActionCards().size());
		assertEquals(0, p3.getDisplay().getCards().size());
		assertEquals(0, p3.getDisplay().getActionCards().size());
		assertTrue(rules.getDeck().viewDiscard().size() > beforecleanupDiscard);
		
	}
	
	@Test
	public void redTournamentTest(){
		tournamentSetup();
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Red);
		Player p1 = rules.getPlayerList().get(0);
		Player p2 = rules.getPlayerList().get(1);
		Player p3 = rules.getPlayerList().get(2);
		Player p;
		
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		rules.withdrawCleanup(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		assertTrue(rules.endTurn(p.getID()));
		assertEquals(rules.getHighestScore(), 8); // 1 squire + 3 squire = 8 pts
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		Long winner = rules.withdrawCleanup(p.getID());
		assertEquals(winner, Long.valueOf(p1.getID()));
	
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //winner is now 1st in list
		p.recieveToken(rules.getTournamentColour());
		assertEquals(p.getTokenCount(),1);
		assertFalse(rules.isTournamentRunning());
		
		//cleanup
		int beforecleanupDiscard = rules.getDeck().viewDiscard().size();
		rules.roundCleanup();
		assertFalse(rules.isColourChosen());
		assertEquals(0, p1.getDisplay().getCards().size());
		assertEquals(0, p1.getDisplay().getActionCards().size());
		assertEquals(0, p2.getDisplay().getCards().size());
		assertEquals(0, p2.getDisplay().getActionCards().size());
		assertEquals(0, p3.getDisplay().getCards().size());
		assertEquals(0, p3.getDisplay().getActionCards().size());
		assertTrue(rules.getDeck().viewDiscard().size() > beforecleanupDiscard);
	}
	
	@Test
	public void yellowTournamentTest(){ 
		tournamentSetup();
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Yellow);
		Player p1 = rules.getPlayerList().get(0);
		Player p2 = rules.getPlayerList().get(1);
		Player p3 = rules.getPlayerList().get(2);
		Player p;
		
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		rules.withdrawCleanup(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		assertTrue(rules.endTurn(p.getID()));
		assertEquals(rules.getHighestScore(), 8); // 1 squire + 3 squire = 8 pts
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		Long winner = rules.withdrawCleanup(p.getID());
		assertEquals(winner, Long.valueOf(p1.getID()));
	
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //winner is now 1st in list
		p.recieveToken(rules.getTournamentColour());
		assertEquals(p.getTokenCount(),1);
		assertFalse(rules.isTournamentRunning());
		
		//cleanup
		int beforecleanupDiscard = rules.getDeck().viewDiscard().size();
		rules.roundCleanup();
		assertFalse(rules.isColourChosen());
		assertEquals(0, p1.getDisplay().getCards().size());
		assertEquals(0, p1.getDisplay().getActionCards().size());
		assertEquals(0, p2.getDisplay().getCards().size());
		assertEquals(0, p2.getDisplay().getActionCards().size());
		assertEquals(0, p3.getDisplay().getCards().size());
		assertEquals(0, p3.getDisplay().getActionCards().size());
		assertTrue(rules.getDeck().viewDiscard().size() > beforecleanupDiscard);
	}
	
	@Test
	public void purpleTournamentTest(){
		tournamentSetup();
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		Player p1 = rules.getPlayerList().get(0);
		Player p2 = rules.getPlayerList().get(1);
		Player p3 = rules.getPlayerList().get(2);
		Player p;
		
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		rules.withdrawCleanup(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		assertTrue(rules.endTurn(p.getID()));
		assertEquals(rules.getHighestScore(), 8); // 1 squire + 3 squire = 8 pts
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		Long winner = rules.withdrawCleanup(p.getID());
		assertEquals(winner, Long.valueOf(p1.getID()));
	
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //winner is now 1st in list
		p.recieveToken(rules.getTournamentColour());
		assertEquals(p.getTokenCount(),1);
		assertFalse(rules.isTournamentRunning());
		
		//cleanup
		int beforecleanupDiscard = rules.getDeck().viewDiscard().size();
		rules.roundCleanup();
		assertFalse(rules.isColourChosen());
		assertEquals(0, p1.getDisplay().getCards().size());
		assertEquals(0, p1.getDisplay().getActionCards().size());
		assertEquals(0, p2.getDisplay().getCards().size());
		assertEquals(0, p2.getDisplay().getActionCards().size());
		assertEquals(0, p3.getDisplay().getCards().size());
		assertEquals(0, p3.getDisplay().getActionCards().size());
		assertTrue(rules.getDeck().viewDiscard().size() > beforecleanupDiscard);
	}
	
	@Test
	public void greenTournamentTest() {
		tournamentSetup();
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		Player p1 = rules.getPlayerList().get(0);
		Player p2 = rules.getPlayerList().get(1);
		Player p3 = rules.getPlayerList().get(2);
		Player p;
		
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		rules.endTurn(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p3.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		rules.withdrawCleanup(p.getID());
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.playCard(0, p.getID());
		rules.playCard(0, p.getID());
		assertTrue(rules.endTurn(p.getID()));
		assertEquals(rules.getHighestScore(), 3); //green tourney, so squires should be 1 each. 1+2 = 3 pts
		
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p2.getID()); //check player turn order
		rules.startTurn(p.getID());
		rules.withdrawPlayer(p.getID());
		Long winner = rules.withdrawCleanup(p.getID());
		assertEquals(winner, Long.valueOf(p1.getID()));
	
		p = rules.getPlayerList().get(0);
		assertEquals(p.getID(), p1.getID()); //winner is now 1st in list
		p.recieveToken(rules.getTournamentColour());
		assertEquals(p.getTokenCount(),1);
		assertFalse(rules.isTournamentRunning());
		
		//cleanup
		int beforecleanupDiscard = rules.getDeck().viewDiscard().size();
		rules.roundCleanup();
		assertFalse(rules.isColourChosen());
		assertEquals(0, p1.getDisplay().getCards().size());
		assertEquals(0, p1.getDisplay().getActionCards().size());
		assertEquals(0, p2.getDisplay().getCards().size());
		assertEquals(0, p2.getDisplay().getActionCards().size());
		assertEquals(0, p3.getDisplay().getCards().size());
		assertEquals(0, p3.getDisplay().getActionCards().size());
		assertTrue(rules.getDeck().viewDiscard().size() > beforecleanupDiscard);
	}
	
}
