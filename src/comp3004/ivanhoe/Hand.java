package comp3004.ivanhoe;

import java.util.ArrayList;

public class Hand {
	
	ArrayList<Card> hand = new ArrayList<Card>();
	
	public Hand(){
		
	}
	
	/**
	 * Removes all cards from hand
	 */
	public void clear(){
		
	}
	
	/**
	 * Add card c to player hand. C cannot be null
	 * @param c Card to be added
	 */
	public void add(Card c){
		if(c == null){
			throw new NullPointerException();
		}
	}
	
	/**
	 * Removes a specified card from the hand
	 * @param c Card to be removed
	 */
	public void removeCard(Card c){
		
	}
	
	/**
	 * Removes the card from the position
	 * @param pos
	 */
	public void removeCard(int pos){
		
	}
	
	/**
	 * returns the number of cards in the players hand
	 * @return int
	 */
	public int getNumCards(){
		return hand.size();
	}
	
	/**
	 * Returns a card if it exists in the hand
	 * @return Card
	 */
	public Card getCard(){
		return null;
	}
	
	
}
