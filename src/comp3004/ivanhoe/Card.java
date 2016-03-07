package comp3004.ivanhoe;

import java.io.Serializable;

public abstract class Card implements Serializable{
	
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
