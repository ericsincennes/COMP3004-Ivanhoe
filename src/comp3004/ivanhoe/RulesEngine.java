
package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;

public class RulesEngine {
	private HashMap<Long, Player> players;
	private List<Player> playersList;
	private int numPlayers = 0, expectedPlayers;
	private Card.CardColour TournementColor = null;
	private Deck deck, discard;

	public RulesEngine(int i){
		expectedPlayers = i;
		players = new HashMap<Long, Player>();
		playersList = new ArrayList<Player>();
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
		deck.ivanhoeDeck();
	}

	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return boolean of success
	 */
	public synchronized int registerThread(long ID){
		//is game full?	
		if(players.containsKey(ID)){
			//notifyAll();
			return -1;
		}
		else if(numPlayers >= expectedPlayers){
			//notifyAll();
			return -1;
		}

		Player p = new Player();
		p.setid(ID);
		players.put(ID, p);
		playersList.add(p);
		numPlayers++;
		return numPlayers;
	}

	/**
	 * Returns the player if the id exists
	 * @param id Long 
	 * @return Player
	 */
	public Player getPlayerById(long id){
		if(players.containsKey(id)){
			return players.get(id);
		}
		return null;
	}
	
	/**
	 * Choose who starts the first tournament
	 * @return player number of the first tournament starter
	 */
	public long chooseFirstTournament(){
		int i = randRange(0, numPlayers);
		Collections.rotate(playersList, i);
		return playersList.get(0).getid();
	}

	public boolean initializeTournementColour(CardColour colour){
		if(TournementColor == CardColour.Purple){
			return false;
		}
		
		for(Player p : playersList){
			p.getDisplay().setColour(colour);
		}
		return true;
	}
	
	/**
	 * Function Assumes players have connected and tournament colour has been chosen.
	 */
	public void runTournament(){
		
	}
	
	
	
	/**
	 * Deals a hand of 8 cards to each player
	 */
	public void dealHand(){
		for(Player p : playersList){
			for(int q =0; q < 8; q++){
				p.addCard(deck.draw());
			}
		}		
	}

	/**
	 * Returns a random number between min and max inclusive
	 * @param min
	 * @param max
	 * @return random int between min inclusive and max inclusive
	 */
	private int randRange(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
}