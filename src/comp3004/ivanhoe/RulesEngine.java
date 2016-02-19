package comp3004.ivanhoe;

public class RulesEngine {
	long[] players;
	int numPlayers = 0;

	public RulesEngine(){
		players = new long[5];
		
	}

	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return boolean of success
	 */
	public int registerThread(long ID){
		//is game full?
		if(numPlayers >= 4){
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
	
	private void chooseDealer(){
		if(numPlayers > 1){
			
		}
	}
	
	public long[] getPlayers(){
		return players;
	}
}
