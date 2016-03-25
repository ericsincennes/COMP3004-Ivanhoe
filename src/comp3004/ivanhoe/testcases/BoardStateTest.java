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
import comp3004.ivanhoe.ColourCard;

public class BoardStateTest {
	RulesEngine rules;
	
	@Before
	public void setUp() throws Exception {
		rules = RulesEngine.testRuleEngine(5);
		rules.registerThread(1);
		rules.registerThread(2);
		rules.registerThread(3);
		rules.registerThread(4);
		rules.registerThread(5);
		
		rules.initFirstTournament();
		assertFalse(rules.isTournamentRunning());
		rules.initTournament();
		assertEquals(8, rules.getPlayerById(1).getHandSize());
		assertEquals(8, rules.getPlayerById(2).getHandSize());
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(2).addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(2).playColourCard(8);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoardState() {
		BoardState b = rules.makeBoardState(rules.getPlayerById(1));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(b);
			oos.close();
			System.out.println("Size of boardstate is: " + baos.size());
		} catch (Exception e) {
			System.out.println("Error");
		}
		assertEquals(CardColour.Blue, b.currColour);
		assertEquals(rules.getPlayerById(1).getID(), b.owner);
		assertEquals(b.players.get(0), Long.valueOf(b.owner));
		assertEquals(5, b.players.size());
		assertEquals(8, b.hand.size());
		assertEquals("Blue 3", b.boards.get(1).get(0).getCardName());
	}

}
