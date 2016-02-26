package comp3004.ivanhoe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import comp3004.ivanhoe.Card.CardType;

public class Player {
	private Hand hand;
	private PointsBoard display;
	private HashMap<String, Integer> tokens;
	private long id;
	
	public Player(){
		tokens = new HashMap<String,Integer>();
		display = new PointsBoard();
		hand = new Hand();
		
		tokens.put("Purple", 0);
		tokens.put("Green", 0);
		tokens.put("Red", 0);
		tokens.put("Blue", 0);
		tokens.put("Yellow", 0);
	}
	
	public List<Card> getHand(){
		return hand.getHand();
	}
	
	/**
	 * Returns the number of tokens in the players possession
	 * @return int
	 */
	public int getTokenCount(){
		int temp = 0;
		Collection<Integer> values = tokens.values();
		for (Iterator<Integer> it = values.iterator(); it.hasNext();) {
			temp += it.next();
		}
		return temp;
	}
	
	/**
	 * Checks if the card is in the players hand
	 * @param s name of card
	 * @return boolean
	 */
	public boolean hasInHand(String s){
		return hand.contains(s);
	}
	
	/**
	 * Draws a card  and adds it to hand
	 * @param c Card
	 */
	public void addCard(Card c){
		hand.add(c);
	}
	
	/**
	 * Adds a Color card to the display
	 * @param cardname name of card
	 */
	public void playColorCard(String cardname){
		Card c = hand.getCardByName(cardname);
		if(c.cardType == CardType.Colour){
			display.addCard(c);
		}
	}
	
	/**
	 * Plays the Action card to the discard pile and causes the effect
	 * @param cardname
	 * @param id
	 */
	public void playActionCard(String cardname){
		Card card = hand.getCardByName(cardname);
		if(card.getCardName() == "Ivanhoe" || card.getCardName() == "Shield"){
			
		} else if(card.getCardName() == "Stunned"){
			
		} else {
			
		}
	}
	
	public int getHandSize(){
		return hand.getNumCards();
	}
	
	public PointsBoard getDisplay(){
		return display;
	}
	
	/**
	 * Returns the thread ID that the player class is associated with
	 * @return lond ID
	 */
	public long getid(){
		return id;
	}
	
	public void setid(long i){
		id = i;
	}
	
	/**
	 * Adds a token to the players collection
	 * @param colour Colour of the token
	 */
	public void recieveToken(String colour){
		if(tokens.containsKey(colour)){
			tokens.put(colour, tokens.get(colour)+1);
		}
	}
}
