package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
	public void addCard(Card c) {
		if (c.getCardType() == CardType.Colour || c.getCardType() == CardType.Supporter) {
			if (((ColourCard)c).cardColour == CardColour.White || ((ColourCard)c).cardColour == tourneyColour) {
				cardsPlayed.add(c);
				score = calculatePoints();
			}
		}
		
		if (c.getCardType() == CardType.Action) {
			actionsPlayed.add(c);
		}
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
	
	/**
	 * Remove card at index
	 * @param index index of card to be removed
	 */
	public void remove(int index) {
		cardsPlayed.remove(index);
		score = calculatePoints();
	}
	
	/**
	 * Remove card by name
	 * @param cardName String
	 */
	public void remove(String cardName) {
		for (Iterator<Card> it = cardsPlayed.iterator(); it.hasNext();) {
			if (cardName == it.next().getCardName()) {
				it.remove();
				break;
			}
		}
		score = calculatePoints();
	}
	
	/**
	 * Removes all cards of a specified colour
	 * @param c CardColour
	 */
	public void removeColour(CardColour c) {
		for (Iterator<Card> it = cardsPlayed.iterator(); it.hasNext();) {
			if (c == ((ColourCard)it.next()).getColour()) {
				it.remove();
			}
		}
		score = calculatePoints();
	}
	
	/**
	 * Removes all cards of a specific value
	 * @param v int value
	 */
	public void removeValue(int v) {
		for (Iterator<Card> it = cardsPlayed.iterator(); it.hasNext();) {
			if (v == ((ColourCard)it.next()).getValue()) {
				it.remove();
			}
		}	
		score = calculatePoints();
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
}
