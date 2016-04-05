
package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.Player;
import comp3004.ivanhoe.SupporterCard;
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
		assertTrue(p.getHand().contains("Blue 6"));
	}
	
	@Test
	public void playColourCardtoBoard(){
		p.addCard(new SupporterCard(6));
		p.addCard(new ColourCard(CardColour.Blue, 2));
		p.getDisplay().setColour(CardColour.Blue);
		p.playColourCard(1);
		Card q = p.getDisplay().getLastPlayed();
		assertEquals("Blue 2", q.getCardName());
		
		p.playColourCard(0);
		Card r = p.getDisplay().getLastPlayed();
		assertEquals("Maiden", r.getCardName());
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
	
	@Test
	public void receiveMultiTokensOfSameColour() {
		p.recieveToken(CardColour.Blue);
		p.recieveToken(CardColour.Blue);
		assertTrue(p.getTokenCount() == 1);
	}
}
