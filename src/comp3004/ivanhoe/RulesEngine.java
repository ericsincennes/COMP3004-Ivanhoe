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
	public boolean registerThread(long ID){
		//is game full?
		if(numPlayers >= 4){
			return false;
		}
		//check if id already registered
		for(long i=0; i<players.length; i++){
			if(i == ID){
				return false;
			}
		}
		
		players[numPlayers] = ID;
		numPlayers++;
		return true;
	}
	
	public long[] getPlayers(){
		return players;
	}
}
