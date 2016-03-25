package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.BoardState;
import comp3004.ivanhoe.RulesEngine;
import comp3004.ivanhoe.Card.CardColour;

public class BoardStateTest {
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
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoardState() {
		BoardState b = rules.makeBoardState(rules.getPlayerById(1));

		assertTrue(b != null);
	}

}
