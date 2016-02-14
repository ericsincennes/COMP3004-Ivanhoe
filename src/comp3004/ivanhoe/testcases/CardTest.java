package comp3004.ivanhoe.testcases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.Card;
import comp3004.ivanhoe.Card.*;
import comp3004.ivanhoe.ColourCard;

public class CardTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ColourCardConstructor() {
		Card ctest = new ColourCard(CardColour.Green,1);
		assert (ctest.getCardType() == CardType.Colour);
		assert (((ColourCard)ctest).getColour() == CardColour.Green);
		assert (((ColourCard)ctest).getValue() == 1);
	}

}
