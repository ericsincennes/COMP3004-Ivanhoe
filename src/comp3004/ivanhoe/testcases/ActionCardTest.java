package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.ActionCard;
import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.Player;
import comp3004.ivanhoe.RulesEngine;
import comp3004.ivanhoe.SupporterCard;
import comp3004.ivanhoe.Card.CardColour;

public class ActionCardTest {
	RulesEngine rules;
	
	@Before
	public void setUp() throws Exception {
		rules = RulesEngine.testRuleEngine(2);
		rules.registerThread(1);
		rules.registerThread(2);
		
		rules.initFirstTournament();
		assertFalse(rules.isTournamentRunning());
		rules.initTournament();
		assertEquals(8, rules.getPlayerById(1).getHandSize());
		assertEquals(8, rules.getPlayerById(2).getHandSize());
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testUnhorse() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.actionHandler(new ActionCard("Unhorse"), rules.getPlayerById(1), CardColour.Blue);
		assertTrue(rules.getTournamentColour().equals(CardColour.Blue));
		
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.actionHandler(new ActionCard("Unhorse"), rules.getPlayerById(1), CardColour.Red);
		assertFalse(rules.getTournamentColour().equals(CardColour.Red));
	}
	
	@Test
	public void testChangeWeapon() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.actionHandler(new ActionCard("Change Weapon"), rules.getPlayerById(1), CardColour.Red);
		assertTrue(rules.getTournamentColour().equals(CardColour.Red));
		
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		rules.actionHandler(new ActionCard("Change Weapon"), rules.getPlayerById(1), CardColour.Red);
		assertFalse(rules.getTournamentColour().equals(CardColour.Red));
	}
	
	@Test
	public void testDropWeapon() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.actionHandler(new ActionCard("Drop Weapon"), rules.getPlayerById(1));
		assertFalse(rules.getTournamentColour().equals(CardColour.Green));
		
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Yellow);
		rules.actionHandler(new ActionCard("Drop Weapon"), rules.getPlayerById(1));
		assertTrue(rules.getTournamentColour().equals(CardColour.Green));
	}
	
	@Test
	public void testBreakLance() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 4));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 7));
		rules.actionHandler(new ActionCard("Break Lance"), rules.getPlayerById(1), rules.getPlayerById(2));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Purple 4"));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Purple 5"));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Purple 7"));
	}
	
	@Test
	public void testRiposte() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Purple);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 7));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 4));
		rules.actionHandler(new ActionCard("Riposte"), rules.getPlayerById(1), rules.getPlayerById(2));
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Purple 4"));
		assertTrue(rules.getPlayerById(1).getDisplay().contains("Purple 4"));
	}
	
	@Test
	public void testDodge() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		rules.actionHandler(new ActionCard("Dodge"), rules.getPlayerById(1), rules.getPlayerById(2), "Blue 3");
		assertFalse(rules.getPlayerById(2).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Blue 4"));
	}
	
	@Test
	public void testRetreat() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Blue, 4));
		rules.actionHandler(new ActionCard("Retreat"), rules.getPlayerById(1), "Blue 3");
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Blue 3"));
		assertTrue(rules.getPlayerById(1).getHand().contains("Blue 3"));
	}
	
	@Test
	public void testKnockDown() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Red);
		int oppHandSize = rules.getPlayerById(2).getHandSize();
		int playHandSize = rules.getPlayerById(1).getHandSize();
		rules.actionHandler(new ActionCard("Knock Down"), rules.getPlayerById(1), rules.getPlayerById(2));
		assertEquals(rules.getPlayerById(2).getHandSize(), oppHandSize-1);
		assertEquals(rules.getPlayerById(1).getHandSize(), playHandSize+1);
	}
	
	@Test
	public void testOutmaneuver() {
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Green);
		rules.getPlayerById(1).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Green, 1));
		rules.actionHandler(new ActionCard("Outmaneuver"), rules.getPlayerById(2));
		assertFalse(rules.getPlayerById(1).getDisplay().contains("Green 1"));
		assertTrue(rules.getPlayerById(2).getDisplay().contains("Green 1"));

	}
	
	@Test
	public void testCharge() {
		fail();
	}
	
	@Test
	public void testCounterCharge() {
		fail();
	}
	
	@Test
	public void testDisgrace() {
		fail();
	}
	
	@Test
	public void testAdapt() {
		fail();
	}
	
	@Test
	public void testOutwit() {
		fail();
	}
	
	@Test
	public void testShield() {
		fail();
	}
	
	@Test
	public void testStunned() {
		fail();
	}
	
	@Test
	public void testIvanhoe() {
		fail();
	}
	
	
}
