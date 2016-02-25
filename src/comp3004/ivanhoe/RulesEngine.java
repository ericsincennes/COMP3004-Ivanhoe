
package comp3004.ivanhoe;

import java.util.HashMap;

public class RulesEngine {
	private HashMap<Long, Player> players;
	private int numPlayers = 0, expectedPlayers;
	private Card.CardColour TournementColor = null;
	private Deck deck, discard;
	
	public RulesEngine(int i){
		expectedPlayers = i;
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
	}
	
	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return boolean of success
	 */
	public synchronized int registerThread(long ID){
		//is game full?
		if(numPlayers >= expectedPlayers){
			notifyAll();
			return -1;
		}
		//check if id already registered
		for(int i=0; i<players.keySet().size(); i++){
			if(players.containsKey(ID)){
				return -1;
			}
		}
		Player p = new Player();
		p.setid(ID);
		players.put(ID, p);
		numPlayers++;
		return numPlayers;
	}
	
	/**
	 * Choose who starts the first tournament
	 * @return player number of the first tournament starter
	 */
	public long firstTournament(){
		if(numPlayers > 1){
			int i = randRange(0, numPlayers);
			Long[] a = (Long[]) players.keySet().toArray();
			if(i == numPlayers){
				return a[0];
			} else {
				return a[i];
			}
		} else {
			return -1;
		}
	}
	
	/**
	 * Deals a hand to each player
	 * @return 
	 */
	public void dealHand(){
		for(int i =0; i<players.keySet().size(); i++){
			
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