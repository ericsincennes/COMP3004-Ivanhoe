package comp3004.ivanhoe;

import java.util.ArrayList;

public class Hand {
	
	ArrayList<Card> hand;
	
	public Hand(){
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Returns the whole hand
	 * @return ArrayList<Card>
	 */
	public ArrayList<Card> getHand(){
		return hand;
	}
	
	/**
	 * Removes all cards from hand
	 */
	public void clear(){
		hand.clear();
	}
	
	/**
	 * Add card c to player hand. C cannot be null
	 * @param c Card to be added
	 */
	public void add(Card c){
		hand.add(c);
	}
	
	/**
	 * Removes a specified card from the hand
	 * @param c Card to be removed
	 */
	public void remove(Card c){
		if(hand.contains(c)){
			hand.remove(c);
		}
	}
	
	/**
	 * Removes the card from the position
	 * @param pos
	 */
	public void remove(int pos){
		hand.remove(hand.get(pos));
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
	 * @return Card or null if no card found
	 */
	public Card getCard(Card c){
		if (hand.contains(c)){
			return hand.get(hand.indexOf(c));
		}
		return null;
	}
	
	public boolean contains(Card c){
		return hand.contains(c);
	}
}