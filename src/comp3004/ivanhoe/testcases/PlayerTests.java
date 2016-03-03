
package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.Player;
import comp3004.ivanhoe.Card;
import comp3004.ivanhoe.Card.CardColour;

public class PlayerTests {
	Player p;
	
	@Before
	public void setUp() throws Exception {
		p = new Player();
	}
	
	@Test
	public void addCardToHand() {
		p.addCard(new ColourCard(CardColour.Blue, 6));
		assertTrue(p.hasInHand("Blue 6"));
	}
	
	@Test
	public void playColourCardtoBoard(){
		p.addCard(new ColourCard(CardColour.Blue, 2));
		p.getDisplay().setColour(CardColour.Blue);
		p.playColourCard("Blue 2");
		Card q = p.getDisplay().getLastPlayed();
		assertEquals("Blue 2", q.getCardName());
	}
	
	@Test
	public void recievePurpleToken(){
		p.recieveToken(CardColour.Purple);
		int i = p.getTokenCount();
		assertTrue(i != 0);
	}
	
	@Test
	public void recieveGreenToken(){
		p.recieveToken(CardColour.Green);
		int i = p.getTokenCount();
		assertTrue(i != 0);
	}
	
	@Test
	public void recieveRedToken(){
		p.recieveToken(CardColour.Red);
		int i = p.getTokenCount();
		assertTrue(i != 0);
	}
	
	@Test
	public void recieveYellowToken(){
		p.recieveToken(CardColour.Yellow);
		int i = p.getTokenCount();
		assertTrue(i != 0);
	}
	
	@Test
	public void recieveBlueToken(){
		p.recieveToken(CardColour.Blue);
		int i = p.getTokenCount();
		assertTrue(i != 0);
	}
}
