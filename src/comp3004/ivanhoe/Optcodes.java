package comp3004.ivanhoe;

public class Optcodes {
	//Server to Client Opt codes
	public static final int ClientGetHand 			= 101;
	public static final int ClientupdateBoardState 	= 102;
	public static final int ClientGetCardsToBePlayed= 103;
	public static final int ClientGetColourChoice 	= 104;
	
	//Client to Server Opt codes
	public static final int ServerSendColourChoice 	= 201;
	//public static final int ServerGetCardsToBePlayed= 202;
	public static final int ServerWithdrawClient  	= 203;
	public static final int ServerEndClientTurn	 	= 204;
}
