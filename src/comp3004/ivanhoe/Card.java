package comp3004.ivanhoe;

import java.io.Serializable;

/**
 * Representation of Cards in Ivanhoe.
 * Implements Serializable for transfer over a network to the client.
 */
public abstract class Card implements Serializable{
	
	/**
	 * Represents the possible type of cards in Ivanhoe.
	 * 
	 */
	public enum CardType {
	    Colour, Supporter, Action, Ivanhoe
	}
	
	/**
	 * Representation of all possible card colours in Ivanhoe.
	 */
	public enum CardColour {
		Purple, Green, Red, Blue, Yellow, White
	}
	
	protected String cardName;
	protected CardType cardType;
	
	/**
	 * Returns the enum CardType associated with specific card
	 * @return cardType Enum
	 */
	public CardType getCardType() {
		return cardType;
	}
	
	/**
	 * Getter for the name of the card
	 * @return name
	 */
	public String getCardName() {
		return cardName;
	}
	
}
