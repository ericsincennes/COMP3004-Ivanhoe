package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.*;
import comp3004.ivanhoe.Card.*;

public class PointsBoardTest {
	PointsBoard testBoard;
	
	@Before
	public void setUp() throws Exception {
		 testBoard = new PointsBoard(CardColour.Purple);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void changeColour() {
		testBoard.setColour(CardColour.Red);
		assertEquals(testBoard.getColour(),CardColour.Red);
	}
	
	@Test
	public void playPointCard() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 7));
		assertEquals(testBoard.getLastPlayed().getCardName(), "Purple 7");
		assertEquals(testBoard.getLastPlayed().getCardType(), CardType.Colour);
		
		testBoard.addCard(new SupporterCard(3));
		assertEquals(testBoard.getLastPlayed().getCardName(), "Squire");
		assertEquals(testBoard.getLastPlayed().getCardType(), CardType.Supporter);
		
		testBoard.setColour(CardColour.Red);
		testBoard.addCard(new ColourCard(CardColour.Red, 4));
		assertEquals(testBoard.getLastPlayed().getCardName(), "Red 4");
	}
	
	@Test
	public void playActionCard() {
		testBoard.addCard(new ActionCard("Shield"));
		testBoard.addCard(new ActionCard("Stun"));
		
		assertEquals(testBoard.getActionCards().get(0).getCardName(), "Shield");
		assertEquals(testBoard.getActionCards().get(0).getCardType(), CardType.Action);
		assertEquals(testBoard.getActionCards().get(1).getCardName(), "Stun");
		assertEquals(testBoard.getActionCards().get(1).getCardType(), CardType.Action);
	}
	
	@Test
	public void findHighestValuePlayed() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new SupporterCard(3));
		assertEquals(testBoard.highestValue(), 4);
	}
	
	@Test
	public void getCardByIndex() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new SupporterCard(3));
		
		assertEquals(testBoard.getCard(1).getCardName(), "Purple 4");
		assertEquals(testBoard.getCard(1).getCardType(), CardType.Colour);
	}
	
	@Test
	public void removeByIndex() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new SupporterCard(3));
		
		testBoard.remove(1);
		assertEquals(testBoard.getCard(1).getCardName(), "Squire");
		assertEquals(testBoard.getCard(1).getCardType(), CardType.Supporter);
	}
	
	@Test
	public void removeByValue() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new SupporterCard(3));
		
		testBoard.removeValue(4);
		assertEquals(testBoard.highestValue(), 3);
	}
	
	@Test
	public void removeByColour() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new SupporterCard(3));
		
		testBoard.removeColour(CardColour.Purple);
		assertEquals(testBoard.highestValue(), 3);
	}
	
	@Test
	public void calculatePoints() {
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new ColourCard(CardColour.Purple, 4));
		testBoard.addCard(new SupporterCard(3));
		
		assertEquals(testBoard.calculatePoints(), 11);
		
		testBoard.setColour(CardColour.Green); //green special case
		assertEquals(testBoard.calculatePoints(), 3);
	}

}
