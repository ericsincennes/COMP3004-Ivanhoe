
package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.Hand;
import comp3004.ivanhoe.Player;
import comp3004.ivanhoe.Card;
import comp3004.ivanhoe.Card.CardColour;

public class PlayerTests {
	Player p;
	
	@Before
	public void setUp() throws Exception {
		p = new Player();
	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void addCardToHand() {
		p.drawCard(new ColourCard(CardColour.Blue, 6));
		assertTrue(p.hasInHand("Blue 6"));
	}
	
	@Test
	public void playColourCardtoBoard(){
		addCardToHand();
		p.getDisplay().setColour(CardColour.Blue);
		p.playColorCard("Blue 6");
		Card q = p.getDisplay().getLastPlayed();
		assertEquals("Blue 6", q.getCardName());
	}
	
	@Test
	public void recieveBlueToken(){
		
	}
}
