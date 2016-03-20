package comp3004.ivanhoe;

public class Optcodes {
	//Server to Client Opt codes
	public static final int ClientGetHand 			= 101;
	public static final int ClientupdateBoardState 	= 102;
	public static final int ClientGetCardsToBePlayed= 103;
	public static final int ClientGetColourChoice 	= 104;
	public static final int ClientGetPlayerList		= 105;
	public static final int ClientEndTurn			= 106;
	public static final int ClientWithdraw			= 107;
	public static final int ClientGetTokenChoice	= 108;
	public static final int ClientGetPoints			= 109;
	
	public static final int InvalidCard				= 301;
	public static final int SuccessfulCardPlay		= 302;
	public static final int TournamentColour		= 303;
	
	//Client to Server Opt codes
	public static final int ServerSendColourChoice 	= 201;
	//public static final int ServerGetCardsToBePlayed= 202;
	public static final int ServerWithdrawClient  	= 203;
	public static final int ServerEndClientTurn	 	= 204;
}
