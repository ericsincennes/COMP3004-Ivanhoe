package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;
import comp3004.ivanhoe.*;
import comp3004.ivanhoe.Card.CardColour;

public class HandTest {
	Hand h = new Hand();

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
		assertTrue(h.getNumCards() == 10);
	}
	
	@Test
	public void removeByCard(){
		h.remove(new ColourCard(CardColour.Blue, 1));		
		assertTrue(h.contains(new ColourCard(CardColour.Blue, 1)) == false);
	}
	
	@Test
	public void removeByIndex(){
		h.remove(0);;
		assertTrue(h.getNumCards() == 9);
	}
	
	@Test
	public void getCard(){
		Card q = h.getCard(new ColourCard(CardColour.Blue, 1));
		assertTrue(q != null);
	}
}
