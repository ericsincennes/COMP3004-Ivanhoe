package comp3004.ivanhoe;

public abstract class Card {
	protected int value;
	protected String colour;
	protected String cardName;
	protected CardType cardtype;
	
	public Card(int value, String colour, String name, CardType type){
		this.value = value;
		this.colour = colour;
		this.cardName = name;
		this.cardtype = type;
	}
	
	public CardType getCardtype() {
		return cardtype;
	}

	public int getValue() {
		return value;
	}

	public String getColour() {
		return colour;
	}

	public String getCardName() {
		return cardName;
	}

}
