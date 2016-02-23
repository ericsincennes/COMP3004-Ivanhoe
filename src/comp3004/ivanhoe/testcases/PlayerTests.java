package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.Test;

import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.Player;
import comp3004.ivanhoe.Card.CardColour;

public class PlayerTests {

	@Test
	public void test() {
		Player p = new Player();
		p.drawCard(new ColourCard(CardColour.Blue, 6));
		assertTrue(p.hasInHand("Blue 6"));

	}
}
