package comp3004.ivanhoe;

import java.util.HashMap;

import comp3004.ivanhoe.Card.CardColour;

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
	
	public boolean hasInHand(String s){
		return hand.contains(s);
	}
	
	public void drawCard(Card c){
		hand.add(c);
	}
	
	public void playCard(Card c){
		display.addCard(c);
	}
	
	public PointsBoard getDisplay(){
		return display;
	}
	
	public long getid(){
		return id;
	}
	
	public void setid(long i){
		id = i;
	}
	
	public void recieveToken(String colour){
		if(tokens.containsKey(colour)){
			tokens.put(colour, tokens.get(colour)+1);
		}
	}
}
