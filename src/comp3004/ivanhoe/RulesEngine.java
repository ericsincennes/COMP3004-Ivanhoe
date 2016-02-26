
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
	private Card.CardColour TournamentColor = null;
	private Deck deck, discard;
	private boolean firstTournament = true;
	
	public RulesEngine(int i){
		expectedPlayers = i;
		players = new HashMap<Long, Player>();
		playersList = new ArrayList<Player>();
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
		deck.ivanhoeDeck();
	}

	public void setTournamentColor(CardColour colour){
		TournamentColor = colour;
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

	public boolean initializeTournamentColour(CardColour colour){
		for(Player p : playersList){
			p.getDisplay().setColour(colour);
		}
		return true;
	}
	
	/**
	 * Assumes all threads that are going to play have connected
	 */
	public void runTournament(){
	}
	
	public void playerTurn(Long id){
		//player draws a card
			//rules engine draws a card for Player object
			//server sends card to player GUI
			//withdraw button in GUI is enabled
		//player gets the ability to withdraw
			//server waits for byte[]{1,1,1,1} to withdraw
			//or server waits for List<Card> of cards to be played
		//player plays cards
			//Server gives Long ID and List<Card> to update PointsBoard
			//Rules engine calculates if play is valid
				//if valid updates point totals and returns true to server
				//if not valid returns false to server
				//server forwards response to client
		//player attempts to end turn
			//if true player can end turn 
			//if false player cannot end turn and must play more or surrender
		//Server updates displays for all players
			//server sends updated boards to all players
	}
	
	/**
	 * Deals a hand of 8 cards to each player
	 */
	public void dealHand(){
		for(Player p : playersList){
			for(int i =0; i < 8; i++){
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