package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private List<Card> cards;
	private boolean isDiscard;
	
	private Deck() {
		cards = new ArrayList<Card>();
		isDiscard = false;
	}
	
	public Deck createDeck() {
		Deck theDeck = new Deck();
		theDeck.isDiscard = false;
		return theDeck;
	}
	
	public Deck createGraveyard() {
		Deck theDeck = new Deck();
		theDeck.isDiscard = true;
		return theDeck;
	}
	
	public Card draw() { 
		return null;
	}
}
