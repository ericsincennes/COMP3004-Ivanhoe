package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<Card> cards; //front is index 0
	private boolean isDiscard;
	private Deck discardPile;
	
	private Deck() {
		cards = new ArrayList<Card>();
		isDiscard = false;
		discardPile = null;
	}
	
	public static Deck createDeck(Deck discard) {
		Deck theDeck = new Deck();
		theDeck.isDiscard = false;
		theDeck.discardPile = discard;
		return theDeck;
	}
	
	public static Deck createDiscard() {
		Deck theDeck = new Deck();
		theDeck.isDiscard = true;
		return theDeck;
	}
	
	public void addCard(Card c) {
		cards.add(c);
	}
	
	public void shuffle() {
		if (!isDiscard) {
			Collections.shuffle(cards);
		}
	}
	
	public List<Card> viewCards() {
		List <Card> viewedCards;
		if (isDiscard) {
			viewedCards = new ArrayList<Card>(cards.size());
			Collections.copy(viewedCards, cards);
		}
		else {
			viewedCards = new ArrayList<Card>(1);
			viewedCards.add(cards.get(0));
		}
		return viewedCards;
	}
	
	public Card draw() { 
		if (isDiscard) {
			return null;
		}
		if (cards.isEmpty()) {
			cards.addAll(discardPile.cards);
			discardPile.cards.clear();
			shuffle();
		}
		
		return (cards.isEmpty()) ? null : cards.get(0);
	}
}
