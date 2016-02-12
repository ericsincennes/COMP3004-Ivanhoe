package comp3004.ivanhoe;

public class ColourCard extends Card {
	protected CardColour cardColour;
	protected int cardValue;
	
	
	protected ColourCard(CardColour colour, int value) {
		cardType = CardType.Colour;
		cardColour = colour;
		cardValue = value;
		cardName = colour.name() + " " + value;
	}

}
