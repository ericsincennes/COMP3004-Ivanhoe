package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;


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
		h.add(new ColourCard(CardColour.Blue, 2));
		h.add(new ColourCard(CardColour.Blue, 3));
		h.remove("Blue 3");		
		assertFalse(h.contains("Blue 3"));
		assertEquals(h.getNumCards(),1);
	}
	
	@Test
	public void removeByIndex(){
		addCard();
		h.removeByIndex(0);
		assertFalse(h.contains("Blue 1"));
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
		
		Card c = h.getCardByName("Blue 2");
		
		assertEquals(c.getCardName(), "Blue 2");
		assertTrue(h.contains("Blue 2"));
	}
}
