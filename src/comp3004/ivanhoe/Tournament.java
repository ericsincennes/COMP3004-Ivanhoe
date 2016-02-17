package comp3004.ivanhoe;

public class Tournament {
	
	private TournamentColour tc;
	
	public enum TournamentColour {
		Purple, Green, Red, Blue, Yellow, White
	}
	
	public Tournament(TournamentColour colour){}
	
	public void startNewTournament(TournamentColour colour){
		tc = colour;
		
	}
	
	public void runTournamnet(){
		
	}
	
	public boolean verifyCard(Card c){
		return false;
	}
	
}
