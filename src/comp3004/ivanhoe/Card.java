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


	public void setCardtype(CardType cardtype) {
		this.cardtype = cardtype;
	}


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
}
