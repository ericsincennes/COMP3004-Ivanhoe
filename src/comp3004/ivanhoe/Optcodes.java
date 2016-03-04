package comp3004.ivanhoe;

public class Optcodes {
	//Server to Client Opt codes
	public static final byte[] ClientGetHand 			= {0,0,1};
	public static final byte[] ClientupdateBoardState 	= {0,0,2};
	public static final byte[] ClientFirstTournament 	= {0,0,3};
	public static final byte[] ClientGetColourChoice 	= {0,0,4};
	
	//Client to Server Opt codes
	public static final byte[] ServerSendColourChoice 	= {1,0,1};
	public static final byte[] ServerGetCardsToBePlayed = {1,0,3};
	public static final byte[] ServerWithdrawClient  	= {1,0,4};
	public static final byte[] ServerEndClientTurn	 	= {1,0,5};
}
