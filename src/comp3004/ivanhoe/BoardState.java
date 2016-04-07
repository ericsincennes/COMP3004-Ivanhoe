package comp3004.ivanhoe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;

/**
 * Represents the boardstate one player can see. Used to give this info to the game client.
 *
 */
public final class BoardState implements Serializable{
	
	public long owner; //player by threadID
	public List<Long> players; //list of players in game, starting with current player
	public List<Card> hand;
	//next 4 board states corresponds to players list
	public List<List<Card>> boards;
	public List<List<Card>> actionBoards; //what actioncards have been played
	public List<Integer> points;
	public List<List<CardColour>> tokens;
	public CardColour currColour;
	//public List<Card> discarded;
	
	public BoardState(Player pOwner, List<Player> thePlayers, Hand pHand, CardColour tourneyColour, Deck theDeck) {
		owner = pOwner.getID();
		
		int pListRot = thePlayers.indexOf(pOwner);
		int pListSize = thePlayers.size();
		players = new ArrayList<Long>(pListSize);
		boards = new ArrayList<List<Card>>(pListSize);
		actionBoards = new ArrayList<List<Card>>(pListSize);
		points = new ArrayList<Integer>(pListSize);
		tokens = new ArrayList<List<CardColour>>(pListSize);
		for (int i = 0; i < pListSize; i++) {
			players.add(thePlayers.get((i+pListRot)%pListSize).getID());
			boards.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().getCards());
			actionBoards.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().getActionCards());
			points.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().calculatePoints());
			tokens.add(thePlayers.get((i+pListRot)%pListSize).getTokens());
		}
		hand = pHand.getHand();
		currColour = tourneyColour;
		//discarded = theDeck.viewDiscard();
	}
	
	@Override
	public boolean equals(Object otherobj) {
		if (!(otherobj instanceof BoardState)) {
			return false;
		}
		BoardState other = (BoardState) otherobj;
		boolean a,b,c,d,e,f,g,h;
		a = this.owner == other.owner;
		b = this.players.equals(other.players);
		c = this.hand.equals(other.hand);
		d = this.boards.equals(other.boards);
		e = this.actionBoards.equals(other.actionBoards);
		f = this.points.equals(other.points);
		g = this.tokens.equals(other.tokens);
		h = this.currColour != null && this.currColour.equals(other.currColour);
		return a && b && c && d && e && f && g && h;
	}
	
}
