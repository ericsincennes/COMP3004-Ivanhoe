
package comp3004.ivanhoe;

public class RulesEngine {
	private Player[] players;
	private int numPlayers = 0;
	private Card.CardColour TournementColor = null;
	private Deck deck, discard;
	
	
	public RulesEngine(){
		players = new Player[5];
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
	}

	public RulesEngine(int i){
		players = new Player[i];
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
		if(numPlayers >= players.length){
			notify();
			return -1;
		}
		//check if id already registered
		for(int i=0; i<players.length; i++){
			if(players[i].getid() == ID){
				return -1;
			}
		}
		players[numPlayers].setid(ID);
		numPlayers++;
		return numPlayers;
	}
	
	/**
	 * Choose who starts the first tournament
	 * @return player number of the first tournament starter
	 */
	public long firstTournement(){
		if(numPlayers > 1){
			int i = randRange(0, numPlayers);
			if(i == numPlayers){
				return players[0].getid();
			} else {
				return players[i].getid();
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
		for(int i =0; i<players.length; i++){
			
		}
	}
	
	public Player[] getPlayers(){
		return players;
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