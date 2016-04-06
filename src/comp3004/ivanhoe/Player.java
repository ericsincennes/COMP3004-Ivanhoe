package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;

public class Player {
	private Hand hand;
	private PointsBoard display;
	private HashMap<CardColour, Integer> tokens;
	private long id;
	private boolean isPlaying = false;
	public boolean hasPlayedToBoard = false;
	
	
	public Player(){
		tokens = new HashMap<CardColour,Integer>();
		display = new PointsBoard();
		hand = new Hand();
		
		tokens.put(CardColour.Purple, 0);
		tokens.put(CardColour.Green, 0);
		tokens.put(CardColour.Red, 0);
		tokens.put(CardColour.Blue, 0);
		tokens.put(CardColour.Yellow, 0);
	}
	
	public Hand getHand(){
		return hand;
	}
	
	public boolean getPlaying(){
		return isPlaying;
	}
	
	public void removeCard(String s) {
		hand.remove(s);
	}
	
	public void setPlaying(boolean s){
		isPlaying = s;
	}
	
	/**
	 * Returns the number of tokens in the players possession
	 * @return int
	 */
	public int getTokenCount(){
		return getTokens().size();
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
	 * @param position of card in hand
	 */
	public void playColourCard(int posinhand){
		Card c = hand.getCardbyIndex(posinhand);
		if(c.cardType == CardType.Colour || c.cardType == CardType.Supporter){
			display.addCard(c);
			getHand().removeByIndex(posinhand);
		}
	}
	
	/**
	 * Plays the Action card to the discard pile and causes the effect
	 * @param position of card in hand
	 * @return the card played
	 */
	public Card playActionCard(int posinhand){
		Card card = hand.getCardbyIndex(posinhand);
		if (card.getCardName().equals("Shield")) {
			display.addCard(card);
			return null;
		}
		getHand().removeByIndex(posinhand);
		return card;
	}
	
	public int getHandSize(){
		return hand.getNumCards();
	}
	
	public PointsBoard getDisplay(){
		return display;
	}
	
	public List<CardColour> getTokens() {
		List<CardColour> theList = new ArrayList<CardColour>(5);
		if (tokens.get(CardColour.Purple) == 1) theList.add(CardColour.Purple);
		if (tokens.get(CardColour.Green) == 1) theList.add(CardColour.Green);
		if (tokens.get(CardColour.Red) == 1) theList.add(CardColour.Red);
		if (tokens.get(CardColour.Blue) == 1) theList.add(CardColour.Blue);
		if (tokens.get(CardColour.Yellow) == 1) theList.add(CardColour.Yellow);
		return theList;
	}
	
	public boolean isShielded() {
		return display.contains("Shield");
	}
	
	public boolean isStunned() {
		return display.contains("Stunned");
	}
	
	/**
	 * Returns the thread ID that the player class is associated with
	 * @return long ID
	 */
	public long getID(){
		return id;
	}
	
	public void setid(long i){
		id = i;
	}
	
	/**
	 * Adds a token to the players collection
	 * @param colour Colour of the token
	 */
	public void recieveToken(CardColour colour){
		if(tokens.containsKey(colour)){
			tokens.put(colour, 1);
		}
	}
	/**
	 * Remove a token from the player
	 * @param colour
	 */
	public void removeToken(CardColour colour){
		if(tokens.containsKey(colour) && tokens.get(colour) == 1){
			tokens.put(colour, 0);
		}
	}
}
