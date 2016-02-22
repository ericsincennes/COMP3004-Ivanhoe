package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.Test;
import comp3004.ivanhoe.*;

public class DeckTest {

	@Test
	public void createDiscard() {
		assertTrue(Deck.createDiscard() != null);
	}
	
	@Test
	public void createDeck() {
		assertTrue(Deck.createDeck(Deck.createDiscard()) != null);
	}
	
	//@Test
	
	
}
