package comp3004.ivanhoe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;

public final class BoardState implements Serializable{
	
	public long owner; //player by threadID
	public List<Long> players; //list of players in game, starting with current player
	public List<Card> hand;
	//next 3 board states corresponds to players list
	public List<List<Card>> boards;
	public List<List<Card>> actionBoards; //what actioncards have been played
	public List<Integer> points;
	public CardColour currColour;
	public List<Card> discarded;
	
	public BoardState(Player pOwner, List<Player> thePlayers, Hand pHand, CardColour tourneyColour, Deck theDeck) {
		owner = pOwner.getID();
		
		int pListRot = thePlayers.indexOf(pOwner);
		int pListSize = thePlayers.size();
		players = new ArrayList<Long>();
		boards = new ArrayList<List<Card>>();
		actionBoards = new ArrayList<List<Card>>();
		points = new ArrayList<Integer>();
		for (int i = pListRot; i < pListSize; i=(i+1)%pListSize) {
			players.add(thePlayers.get(i).getID());
			List oneBoard = new ArrayList<Card>(thePlayers.get(i).getDisplay().getCards());
			List oneActionBoard = new ArrayList<Card>(thePlayers.get(i).getDisplay().getActionCards());
			boards.add(oneBoard);
			actionBoards.add(oneActionBoard);
			points.add(thePlayers.get(i).getDisplay().calculatePoints());
		}
		hand = new ArrayList<Card>(pHand.getHand());
		currColour = tourneyColour;
		discarded = new ArrayList<Card>(theDeck.viewDiscard());
	}
	
}
