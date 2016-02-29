package comp3004.ivanhoe;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Hand {
	
	private List<Card> hand;
	
	public Hand(){
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Returns the whole hand
	 * @return List<Card>
	 */
	public List<Card> getHand(){
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
	 * Removes the first instance of a specified card from the hand
	 * @param c Card to be removed
	 */
	public void remove(String cardName){
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			if (cardName.equals(it.next().getCardName())) {
				it.remove();
				break;
			}
		}
	}
	
	/**
	 * Removes the card from the specified index
	 * @param pos
	 */
	public void removeByIndex(int pos){
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
	 * Returns the first instance of a card given its name
	 * @param cardname name of card (e.g. Blue 2)
	 * @return Card or null if not found
	 */
	public Card getCardByName(String cardname){
		Card c;
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			c = it.next();
			if (cardname.equals(c.getCardName())) {
				//it.remove();
				return c;
			}
		}
		return null;
	}
	
	
	/**
	 * Returns a card if it exists in the hand
	 * @return Card or null if no card found
	 */
	public Card getCard(Card card){
		Card temp;
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			temp = it.next();
			if (card.equals(it.next())) {
				it.remove();
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Returns a card from a specific index
	 * @param index index of card
	 * @return Card or null if index is out of bounds
	 */
	public Card getCardbyIndex(int index){
		if(index < hand.size()){
			Card c = hand.get(index);
			hand.remove(index);
			return c;
		}
		return null;
	}
	
	/**
	 * Checks if an instance of the card is in the hand
	 * @param name name of card
	 * @return boolean
	 */
	public boolean contains(String name){
		for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
			if (name.equals(it.next().getCardName())) {
				return true;
			}
		}
		return false;
	}
}