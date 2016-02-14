package comp3004.ivanhoe;

public abstract class Card {
	
	public enum CardType {
	    Colour, Supporter, Action, Ivanhoe
	}
	
	public enum CardColour {
		Purple, Green, Red, Blue, Yellow, White
	}
	
	protected String cardName;
	protected CardType cardType;
		
	public CardType getCardType() {
		return cardType;
	}

	public String getCardName() {
		return cardName;
	}
	
}
