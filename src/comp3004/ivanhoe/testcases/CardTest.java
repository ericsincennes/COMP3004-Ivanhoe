package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.Card;
import comp3004.ivanhoe.Card.*;
import comp3004.ivanhoe.ActionCard;
import comp3004.ivanhoe.ColourCard;
import comp3004.ivanhoe.SupporterCard;

public class CardTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ColourCardConstructor() {
		Card green = new ColourCard(CardColour.Green,1);
		assertTrue(green.getCardType() == CardType.Colour);
		assertTrue(((ColourCard)green).getColour() == CardColour.Green);
		assertTrue(((ColourCard)green).getValue() == 1);
		assertEquals(green.getCardName(),"Green 1");
		
		Card blue = new ColourCard(CardColour.Blue,6);
		assertTrue(blue.getCardType() == CardType.Colour);
		assertTrue(((ColourCard)blue).getColour() == CardColour.Blue);
		assertTrue(((ColourCard)blue).getValue() == 6);
		
		Card red = new ColourCard(CardColour.Red,4);
		assertTrue(red.getCardType() == CardType.Colour);
		assertTrue(((ColourCard)red).getColour() == CardColour.Red);
		assertTrue(((ColourCard)red).getValue() == 4);
		
		Card yellow = new ColourCard(CardColour.Yellow,2);
		assertTrue(yellow.getCardType() == CardType.Colour);
		assertTrue(((ColourCard)yellow).getColour() == CardColour.Yellow);
		assertTrue(((ColourCard)yellow).getValue() == 2);
		
		Card purple = new ColourCard(CardColour.Purple,7);
		assertTrue(purple.getCardType() == CardType.Colour);
		assertTrue(((ColourCard)purple).getColour() == CardColour.Purple);
		assertTrue(((ColourCard)purple).getValue() == 7);
	}
	
	@Test
	public void SupportCardConstructor() {
		Card squire = new SupporterCard(3);
		assertTrue(squire.getCardType() == CardType.Supporter);
		assertEquals(squire.getCardName(), "Squire 3");
		assertTrue(((SupporterCard)squire).getValue() == 3);
		Card maiden = new SupporterCard(6);
		assertTrue(maiden.getCardType() == CardType.Supporter);
		assertTrue(maiden.getCardName() == "Maiden");
		assertTrue(((ColourCard)maiden).getValue() == 6);
	}
	
	@Test
	public void ActionCardConstructor() {
		Card unhorse = new ActionCard("Unhorse");
		assertTrue(unhorse.getCardName() == "Unhorse");
		
		Card ivanhoe = new ActionCard("Ivanhoe");
		assertTrue(ivanhoe.getCardName() == "Ivanhoe");
	}

}
