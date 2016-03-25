package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;


public class PointsBoard {
	private List<Card> cardsPlayed;
	private List<Card> actionsPlayed;
	private int score;
	private CardColour tourneyColour;
	
	public PointsBoard() {
		cardsPlayed = new ArrayList<Card>();
		actionsPlayed = new ArrayList<Card>();
		score = 0;
		tourneyColour = null;
	}
	
	public PointsBoard(CardColour c) {
		this();
		tourneyColour = c;	
	}
	
	/**
	 * Get the colour of the current tournament
	 * @return CardColour
	 */
	public CardColour getColour() {
		return tourneyColour;
	}
	
	/**
	 * Set colour of the current tournament
	 * @param c CardColour
	 */
	public void setColour(CardColour c) {
		tourneyColour = c;
		score = calculatePoints();
	}
	
	/**
	 * Add Card to display 
	 * @param c Card to be added
	 */
	public boolean addCard(Card c) {
		if (c.getCardType() == CardType.Colour || c.getCardType() == CardType.Supporter) {
			if (((ColourCard)c).cardColour == CardColour.White || ((ColourCard)c).cardColour == tourneyColour) {
				if (c.getCardName() == "Maiden" && this.contains("Maiden")) {
					return false;
				}
				cardsPlayed.add(c);
				score = calculatePoints();
				return true;
			}
		}
		
		if (c.getCardType() == CardType.Action) {
			actionsPlayed.add(c);
			return true;
		}
		return false;
	}
	
	
	/**
	 * checks if the display board contains the card played, actions and points card
	 * @param name - name of card to check
	 */
	public boolean contains(String name) {
		for (Card c : cardsPlayed) {
			if (name == c.getCardName()) {
				return true;
			}
		}
		for (Card c : actionsPlayed) {
			if (name == c.getCardName()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return specific non-action card played to display
	 * @param index index of card to be retrieved
	 * @return Card
	 */
	public Card getCard(int index) {
		return cardsPlayed.get(index);
	}
	
	/**
	 * Return the last played card
	 * @return Card
	 */
	public Card getLastPlayed() {
		return cardsPlayed.get(cardsPlayed.size()-1);
	}
	
	/**
	 * Return all action cards played
	 * @return List<Card>
	 */
	public List<Card> getActionCards() {
		List<Card> viewedCards = new ArrayList<Card>(actionsPlayed);
		//Collections.copy(viewedCards, actionsPlayed);
		return viewedCards;
	}
	
	/**
	 * Return all non-ActionCards played to display
	 * @return List<Card>
	 */
	public List<Card> getCards() {
		List<Card> viewedCards = new ArrayList<Card>(cardsPlayed);
		//Collections.copy(viewedCards, cardsPlayed);
		return viewedCards;
	}
	
	/**
	 * Return the highest value of all cards played
	 * @return int
	 */
	public int highestValue() {
		int v = -1;
		for (Card c : cardsPlayed) {
			if (((ColourCard) c).getValue() > v) {
				v = ((ColourCard) c).getValue();
			}
		}
		return v;
	}
	
	public int lowestValue() {
		int v = 8;
		for (Card c : cardsPlayed) {
			if (((ColourCard) c).getValue() < v) {
				v = ((ColourCard) c).getValue();
			}
		}
		return v;
	}
	/**
	 * Remove card at index
	 * @param index index of card to be removed
	 * @return card removed or null if no card removed
	 */
	public Card remove(int index) {
		if (cardsPlayed.size() <= 1) {
			return null;
		}
		Card ret = cardsPlayed.remove(index);
		score = calculatePoints();
		return ret;
	}
	
	/**
	 * Remove card by name
	 * @param cardName String
	 
	 */
	public Card remove(String cardName) {
		if (cardsPlayed.size() <= 1) {
			return null;
		}
		Card ret = null;
		for (ListIterator<Card> it = cardsPlayed.listIterator(cardsPlayed.size()); it.hasPrevious();) {
			ret = it.previous();
			if (cardName == ret.getCardName()) {
				it.remove();
				break;
			}
		}
		score = calculatePoints();
		return ret;
	}
	
	/**
	 * Removes all cards of a specified colour
	 * @param c CardColour
	 */
	public void removeColour(CardColour c) {

		for (ListIterator<Card> it = cardsPlayed.listIterator(cardsPlayed.size()); 
				cardsPlayed.size() > 1 && it.hasPrevious();) {
			if (c == ((ColourCard)it.previous()).getColour()) {
				it.remove();
			}
		}
		score = calculatePoints();
	}
	
	/**
	 * Removes all cards of a specific value
	 * @param v int value to remove
	 * @return ArrayList 
	 */
	public ArrayList<Card> removeValue(int v) {
		ArrayList<Card> card = new ArrayList<Card>();
		
		for (ListIterator<Card> it = cardsPlayed.listIterator(cardsPlayed.size()); 
				cardsPlayed.size() > 1 && it.hasPrevious();) {
			if (v == ((ColourCard)it.previous()).getValue()) {
				card.add(it.previous());
				it.remove();
			}
		}
		score = calculatePoints();
		return card;
	}
	
	/**
	 * Calculate point value of all cards played
	 * @return int
	 */
	public int calculatePoints() {
		int points = 0;
		for (Card c : cardsPlayed) {
			points += ((ColourCard)c).getValue();
		}
		return (tourneyColour == CardColour.Green) ? cardsPlayed.size() : points;
	}
	
	/**
	 * Clears board and returns the cards cleared
	 */
	public List<Card> clearBoard() {
		List<Card> cleared = new ArrayList<Card>();
		cleared.addAll(cardsPlayed);
		cleared.addAll(actionsPlayed);
		cardsPlayed.clear();
		actionsPlayed.clear();
		score = 0;
		return cleared;
	}
}
