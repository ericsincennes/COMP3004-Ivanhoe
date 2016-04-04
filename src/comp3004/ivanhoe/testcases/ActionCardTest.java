package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.ActionCard;
import comp3004.ivanhoe.Card;
import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.Player;
import comp3004.ivanhoe.RulesEngine;
import comp3004.ivanhoe.SupporterCard;
import comp3004.ivanhoe.Card.CardColour;

public class ActionCardTest {
	RulesEngine rules;
	List<Object> toSend = new ArrayList<Object>();
	long p1, p2, p3;
	
	@Before
	public void setUp() throws Exception {
		rules = RulesEngine.testRuleEngine(3);
		rules.registerThread(1);
		p1 = 1;
		rules.registerThread(2);
		p2 = 2;
		rules.registerThread(3);
		p3 = 3;
		
		rules.initFirstTournament();
		assertFalse(rules.isTournamentRunning());
		rules.initTournament();
		assertEquals(8, rules.getPlayerById(1).getHandSize());
		assertEquals(8, rules.getPlayerById(2).getHandSize());
		assertEquals(8, rules.getPlayerById(3).getHandSize());
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testUnhorse() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.getPlayerById(1).addCard(new ActionCard("Unhorse"));
		toSend.add(CardColour.Blue);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		assertTrue(rules.getTournamentColour().equals(CardColour.Blue));
		toSend.clear();
		
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(1).addCard(new ActionCard("Unhorse"));
		toSend.add(CardColour.Red);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		assertFalse(rules.getTournamentColour().equals(CardColour.Red));
		toSend.clear();
	}
	
	@Test
	public void testChangeWeapon() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(1).addCard(new ActionCard("Change Weapon"));
		toSend.add(CardColour.Red);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		assertTrue(rules.getTournamentColour().equals(CardColour.Red));
		toSend.clear();
		
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		rules.getPlayerById(1).addCard(new ActionCard("Change Weapon"));
		toSend.add(CardColour.Red);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		assertFalse(rules.getTournamentColour().equals(CardColour.Red));
		toSend.clear();
	}
	
	@Test
	public void testDropWeapon() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.getPlayerById(1).addCard(new ActionCard("Drop Weapon"));
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		assertFalse(rules.getTournamentColour().equals(CardColour.Green));
		
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Yellow);
		rules.getPlayerById(1).addCard(new ActionCard("Drop Weapon"));
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		assertTrue(rules.getTournamentColour().equals(CardColour.Green));
	}
	
	@Test
	public void testBreakLance() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 4));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 7));
		
		rules.getPlayerById(1).addCard(new ActionCard("Break Lance"));
		toSend.add(p2);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Purple 4"));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Purple 7"));
		toSend.clear();
	}
	
	@Test
	public void testRiposte() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 7));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 4));
		
		rules.getPlayerById(1).addCard(new ActionCard("Riposte"));
		toSend.add(p2);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Purple 4"));
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Purple 4"));
		toSend.clear();
	}

	@Test
	public void testDodge() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		
		rules.getPlayerById(1).addCard(new ActionCard("Dodge"));
		toSend.add("Blue 3");
		toSend.add(p2);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Blue 4"));
		toSend.clear();
	}
	
	@Test
	public void testRetreat() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		
		rules.getPlayerById(1).addCard(new ActionCard("Retreat"));
		toSend.add("Blue 3");
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(1).getHand().contains("Blue 3"));
		toSend.clear();
	}
	
	@Test
	public void testKnockDown() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Red);
		int playHandSize = rules.getPlayerById(1).getHandSize();
		int oppHandSize = rules.getPlayerById(2).getHandSize();
		
		rules.getPlayerById(1).addCard(new ActionCard("Knock Down"));
		toSend.add(p2);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertEquals(rules.getPlayerById(2).getHandSize(), oppHandSize-1);
		assertEquals(rules.getPlayerById(1).getHandSize(), playHandSize+1);
		toSend.clear();
	}
	
	@Test
	public void testOutmaneuver() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		rules.getPlayerById(1).getDisplay().addCard(new SupporterCard(2));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		rules.getPlayerById(2).getDisplay().addCard(new SupporterCard(3));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		rules.getPlayerById(3).getDisplay().addCard(new SupporterCard(3));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		
		rules.getPlayerById(2).addCard(new ActionCard("Outmaneuver"));
		rules.actionHandler(rules.getPlayerById(2).getHandSize()-1, rules.getPlayerById(2), toSend);
		
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Green 1"));
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Squire 2"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Green 1"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Squire 3"));
		assertFalse(rules.getPlayerById(3).getDisplay().contains("Green 1"));
		assertTrue(rules.getPlayerById(3).getDisplay().contains("Squire 3"));
	}
	
	@Test
	public void testCharge() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 5));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		
		rules.getPlayerById(2).addCard(new ActionCard("Charge"));
		rules.actionHandler(rules.getPlayerById(2).getHandSize()-1, rules.getPlayerById(2), toSend);
		
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Blue 4"));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Blue 4"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Blue 5"));
		assertFalse(rules.getPlayerById(3).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(3).getDisplay().contains("Blue 4"));
	}
	
	@Test
	public void testCounterCharge() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 5));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 5));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 5));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Blue, 5));
		
		rules.getPlayerById(2).addCard(new ActionCard("Countercharge"));
		rules.actionHandler(rules.getPlayerById(2).getHandSize()-1, rules.getPlayerById(2), toSend);
		
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Blue 4"));
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Blue 5"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Blue 3"));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Blue 5"));
		assertTrue(rules.getPlayerById(3).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(3).getDisplay().contains("Blue 4"));
		assertFalse(rules.getPlayerById(3).getDisplay().contains("Blue 5"));
	}
	
	@Test
	public void testDisgrace() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Yellow);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Yellow, 3));
		rules.getPlayerById(1).getDisplay().addCard(new SupporterCard(3));
		rules.getPlayerById(1).getDisplay().addCard(new SupporterCard(2));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Yellow, 4));
		rules.getPlayerById(2).getDisplay().addCard(new SupporterCard(6));
		rules.getPlayerById(3).getDisplay().addCard(new ColourCard(CardColour.Yellow, 3));
		rules.getPlayerById(3).getDisplay().addCard(new SupporterCard(3));
		rules.getPlayerById(3).getDisplay().addCard(new SupporterCard(2));
		
		rules.getPlayerById(2).addCard(new ActionCard("Disgrace"));
		rules.actionHandler(rules.getPlayerById(2).getHandSize()-1, rules.getPlayerById(2), toSend);
		
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Yellow 3"));
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Squire 2"));
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Squire 3"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Yellow 4"));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Maiden"));
		assertTrue(rules.getPlayerById(3).getDisplay().contains("Yellow 3"));
		assertFalse(rules.getPlayerById(3).getDisplay().contains("Squire 2"));
		assertFalse(rules.getPlayerById(3).getDisplay().contains("Squire 3"));
	}
	
	@Test
	public void testAdapt() {
		fail();
	}
	
	@Test
	public void testOutwit() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Yellow);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Yellow, 2));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Yellow, 3));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Yellow, 4));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Yellow, 4));
		
		rules.getPlayerById(1).addCard(new ActionCard("Outwit"));
		toSend.add(p2);
		toSend.add(0);
		toSend.add("Yellow 4");
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Yellow 2"));
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Yellow 4"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Yellow 2"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Yellow 4"));
		toSend.clear();
	}
	
	@Test
	public void testShield() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		
		rules.getPlayerById(1).addCard(new ActionCard("Shield"));
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Shield"));
	}
	
	@Test
	public void testStunned() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		
		rules.getPlayerById(1).addCard(new ActionCard("Stunned"));
		toSend.add(p2);
		rules.actionHandler(rules.getPlayerById(1).getHandSize()-1, rules.getPlayerById(1), toSend);
		
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Stunned"));
		toSend.clear();
	}
	
	@Test
	public void testIvanhoe() {
		fail();
	}
	
}
