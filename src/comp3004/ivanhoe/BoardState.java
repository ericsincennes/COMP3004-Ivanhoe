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
	//public List<Card> discarded;
	
	public BoardState(Player pOwner, List<Player> thePlayers, Hand pHand, CardColour tourneyColour, Deck theDeck) {
		owner = pOwner.getID();
		
		int pListRot = thePlayers.indexOf(pOwner);
		int pListSize = thePlayers.size();
		players = new ArrayList<Long>(pListSize);
		boards = new ArrayList<List<Card>>(pListSize);
		actionBoards = new ArrayList<List<Card>>(pListSize);
		points = new ArrayList<Integer>(pListSize);
		for (int i = 0; i < pListSize; i++) {
			players.add(thePlayers.get((i+pListRot)%pListSize).getID());
			boards.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().getCards());
			actionBoards.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().getActionCards());
			points.add(thePlayers.get((i+pListRot)%pListSize).getDisplay().calculatePoints());
		}
		hand = pHand.getHand();
		currColour = tourneyColour;
		//discarded = theDeck.viewDiscard();
	}
	
}
