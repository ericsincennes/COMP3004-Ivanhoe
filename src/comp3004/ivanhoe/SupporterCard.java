package comp3004.ivanhoe;

import comp3004.ivanhoe.Card.CardType;





public class SupporterCard extends ColourCard {
	public SupporterCard (int value) {
		super(CardColour.White, value);
		cardType = CardType.Supporter;
		cardName = (value == 2 || value == 3) ? "Squire" : (value == 6) ? "Maiden" : "";
	}
}
