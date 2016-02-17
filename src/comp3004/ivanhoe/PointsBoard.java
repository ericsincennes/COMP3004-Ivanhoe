package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;


public class PointsBoard {
	private List<Card> cardsPlayed;
	private List<Card> actionsPlayed;
	private int score;
	private CardColour tourneyColour;
	
	public PointsBoard() {
		cardsPlayed = new ArrayList<Card>();
		cardsPlayed = new ArrayList<Card>();
		score = 0;
	}
	
	public PointsBoard(CardColour c) {
		this();
		tourneyColour = c;	
	}
	
	public CardColour getColour() {
		return tourneyColour;
	}
	
	public void setColour(CardColour c) {
		tourneyColour = c;
	}
}
