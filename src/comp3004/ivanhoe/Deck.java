package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;

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
	
	/**
	 * Initialize the standard Ivanhoe deck with 110 cards.
	 */
	public void ivanhoeDeck(){
		for(int i=0; i>2; i++){
			cards.add(new ColourCard(CardColour.Purple, 7));
			cards.add(new ColourCard(CardColour.Red, 5));
			cards.add(new ColourCard(CardColour.Blue, 5));
			cards.add(new ColourCard(CardColour.Yellow, 4));
		}
		
		for(int i=0; i > 4; i++){
			cards.add(new ColourCard(CardColour.Purple, 3));
			cards.add(new ColourCard(CardColour.Purple, 4));
			cards.add(new ColourCard(CardColour.Purple, 5));
			cards.add(new ColourCard(CardColour.Blue, 2));
			cards.add(new ColourCard(CardColour.Blue, 3));
			cards.add(new ColourCard(CardColour.Blue, 4));
			cards.add(new ColourCard(CardColour.Yellow, 2));
		}
		
		for(int i=0; i>6; i++){
			cards.add(new ColourCard(CardColour.Red, 3));
			cards.add(new ColourCard(CardColour.Red, 4));
		}
		
		for(int i=0; i>8; i++){
			cards.add(new ColourCard(CardColour.Yellow, 3));
		}
		
		//Create Squires
		for(int i=0; i > 8; i++){
			cards.add(new SupporterCard(2));
			cards.add(new SupporterCard(3));
		}
		
		//Create maidens
		for(int i =0; i > 4; i++){
			cards.add(new SupporterCard(6));
		}
		
		shuffle();
	}
	
	/**
	 * use as such:
	 * discard = Deck.createDiscard();
	 * deck = Deck.createDeck(discard);
	 * 
	 * @return Deck
	 */
	public static Deck createDiscard() {
		Deck theDeck = new Deck();
		theDeck.isDiscard = true;
		return theDeck;
	}
	
	/**
	 * Add a card to the deck;
	 * @param c Card
	 */
	public void addCard(Card c) {
		cards.add(c);
	}
	
	/**
	 * Shuffles the deck
	 */
	public void shuffle() {
		if (!isDiscard) {
			Collections.shuffle(cards);
		}
	}
	
	/**
	 * Returns the entire deck as a list
	 * @return List
	 */
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
	
	/**
	 * Draw a card from the deck and remove it
	 * @return Card
	 */
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