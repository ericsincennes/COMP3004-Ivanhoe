package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import comp3004.ivanhoe.*;
import comp3004.ivanhoe.Card.CardColour;

public class HandTest {
	Hand h;
	
	@Before
	public void setUp() throws Exception {
		h = new Hand();
	}

	@After
	public void tearDown() throws Exception {}

	
	@Test
	public void createHand() {
		assertTrue(h != null);
	}

	@Test
	public void addCard(){
		for(int i = 1; i < 11; i++){
			ColourCard c = new ColourCard(CardColour.Blue , i);
			h.add(c);
		}
		assertEquals(h.getNumCards(), 10);
	}
	
	@Test
	public void removeByCard(){
		h.remove(new ColourCard(CardColour.Blue, 1));		
		assertEquals(h.contains(new ColourCard(CardColour.Blue, 1)), false);
	}
	
	@Test
	public void removeByIndex(){
		addCard();
		h.remove(0);
		assertEquals(h.getNumCards(), 9);
	}
	
	@Test
	public void getCard(){
		Card q = h.getCard(new ColourCard(CardColour.Blue, 1));
		assertEquals(q , null);
	}
	
	@Test
	public void getCardFromHand(){
		Card temp = new ColourCard(CardColour.Blue, 2); 
		h.add(temp);
		
		Card c = h.playCard("Blue 2");
		
		assertEquals(c.getCardName(), "Blue 2");
		assertFalse(h.contains("Blue 2"));
	}
}
