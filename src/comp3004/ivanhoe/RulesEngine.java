package comp3004.ivanhoe;

public class RulesEngine {
	long[] players;
	int numPlayers = 0;
	Card.CardColour TournementColor = null;
	long TournementColorChooser;

	public RulesEngine(){
		players = new long[5];
	}

	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return boolean of success
	 */
	public synchronized int registerThread(long ID){
		//is game full?
		if(numPlayers >= 4){
			notify();
			return -1;
		}
		//check if id already registered
		for(int i=0; i<players.length; i++){
			if(players[i] == ID){
				return -1;
			}
		}
		players[numPlayers] = ID;
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
				return players[0];
			} else {
				return players[i];
			}
		} else {
			return -1;
		}
	}
	
	/**
	 * Deals a hand of 
	 * @return
	 */
	public synchronized Hand dealHand(){
		return null;
	}
	
	public long[] getPlayers(){
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
