package comp3004.ivanhoe;

public class Optcodes {
	//Server to Client Opt codes
	public static final int ClientUpdateBoardState 		= 102;
	public static final int ClientGetCardsToBePlayed	= 103;
	public static final int ClientGetColourChoice 		= 104;
	public static final int ClientEndTurn				= 106;
	public static final int ClientWithdraw				= 107;
	public static final int ClientGetTokenChoice		= 108;
	public static final int ClientGetActionCardTarget 	= 110;
	
	public static final int ClientNotActiveTurn			= 150;
	public static final int ClientActiveTurn 			= 151;
	
	public static final int InvalidCard					= 301;
	public static final int SuccessfulCardPlay			= 302;
	public static final int TournamentColour			= 303;
	
	public static final int GameWinner 					= 9001;
	public static final int GameOver					= 8999;
	
	public static final int Ivanhoe						= 9999;
}
