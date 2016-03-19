package comp3004.ivanhoe;

public class SupporterCard extends ColourCard {
	public SupporterCard (int value) {
		super(CardColour.White, value);
		cardType = CardType.Supporter;
		cardName = (value == 2 || value == 3) ? "Squire" + String.valueOf(value) : (value == 6) ? "Maiden" : "";
	}
}
