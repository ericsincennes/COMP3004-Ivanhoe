package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.BoardState;
import comp3004.ivanhoe.RulesEngine;
import comp3004.ivanhoe.SupporterCard;
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
		assertEquals(8, rules.getPlayerById(2).getHandSize());
		assertEquals(8, rules.getPlayerById(4).getHandSize());
		rules.initializeTournamentColour(rules.getPlayerById(1).getID(), CardColour.Blue);
		rules.getPlayerById(2).addCard(new ColourCard(CardColour.Blue, 3));
		rules.getPlayerById(2).playColourCard(8);
		rules.getPlayerById(4).addCard(new ColourCard(CardColour.Blue, 4));
		rules.getPlayerById(4).playColourCard(8);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoardState() {
		BoardState b = rules.makeBoardState(rules.getPlayerById(2));
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
		assertEquals(rules.getPlayerById(2).getID(), b.owner);
		assertEquals(b.players.get(0), Long.valueOf(b.owner));
		assertEquals(5, b.players.size());
		assertEquals(8, b.hand.size());
		assertEquals(1, b.boards.get(0).size());
		assertEquals("Blue 3", b.boards.get(0).get(0).getCardName());
		for (int i=1; i<b.players.size(); i++) {
			if (b.players.get(i) == 4) {
				assertEquals(1, b.boards.get(i).size());
				assertEquals("Blue 4", b.boards.get(i).get(0).getCardName());
			}
			else {
				assertEquals(0, b.boards.get(i).size());
			}
		}
	}
	
	@Test
	public void testEquality() {
		BoardState a1 = rules.makeBoardState(rules.getPlayerById(1));
		BoardState a2 = rules.makeBoardState(rules.getPlayerById(1));
		BoardState b1 = rules.makeBoardState(rules.getPlayerById(4));
		
		assertFalse(a1.equals(null));
		assertFalse(a1.equals(new SupporterCard(3)));
		assertEquals(a1, a2);
		assertEquals(a2, a1);
		assertNotEquals(a1, b1);
		assertNotEquals(b1, a2);
	}

}
