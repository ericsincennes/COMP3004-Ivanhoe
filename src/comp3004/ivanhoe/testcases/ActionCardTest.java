package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.ActionCard;
import comp3004.ivanhoe.BoardState;
import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.RulesEngine;
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
		rules.initializeTournamentColour(rules.getPlayerById(2).getID(), CardColour.Purple);
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 4));
		rules.getPlayerById(2).getDisplay().addCard(new ColourCard(CardColour.Purple, 7));
		rules.actionHandler(new ActionCard("Break Lance"), rules.getPlayerById(1), rules.getPlayerById(2));
		assertTrue(rules.getPlayerById(2).getDisplay().getCards().isEmpty());
	}
	
	@Test
	public void testRiposte() {
		
	}
	
	@Test
	public void testDodge() {
		
	}
	
	@Test
	public void testRetreat() {
		
	}
	
	@Test
	public void testKnockDown() {
		
	}
	
	@Test
	public void testOutmaneuver() {
		
	}
	
	@Test
	public void testCharge() {
		
	}
	
	@Test
	public void testCounterCharge() {
		
	}
	
	@Test
	public void testDisgrace() {
		
	}
	
	@Test
	public void testAdapt() {
		
	}
	
	@Test
	public void testOutwit() {
		
	}
	
	@Test
	public void testShield() {
		
	}
	
	@Test
	public void testStunned() {
		
	}
	
	@Test
	public void testIvanhoe() {
		
	}
	
	
}
