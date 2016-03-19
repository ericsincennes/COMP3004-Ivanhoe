package comp3004.ivanhoe;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.testcases.Log;
import comp3004.ivanhoe.Optcodes;

public class Server{

	private int 			port = 0;
	private int 			numplayers;
	private boolean 		isAcceptingConnections = true;
	private ServerSocket 	listeningSocket;
	//private Log				log = new Log(this.getClass().getName(), "ServerLog");
	private RulesEngine		rules;


	public Server(){
		Scanner in = new Scanner(System.in);

		while (port == 0){
			print("Enter port to listen on");
			port = in.nextInt();
		}

		while(numplayers < 2 || numplayers > 5){
			print("Enter number of players to play (between 2 and 5)");
			numplayers = in.nextInt();
		}
		in.close();
		rules = new RulesEngine(numplayers);
		connectAndRecieve(numplayers);
	}

	private void connectAndRecieve(int count){
		try{

			print(getTimestamp() + ": server listening on port " + port);
			//log.logmsg(getTimestamp() + " : server listening on port " + port);
			listeningSocket = new ServerSocket(port);
			ArrayList<PlayerThread> threads = new ArrayList<PlayerThread>();
			while(isAcceptingConnections){

				Socket clientSocket = listeningSocket.accept();

				print(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());
				//log.logmsg(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());

				count--;
				threads.add(new PlayerThread(clientSocket));

				if(count == 0){
					listeningSocket.close();
					isAcceptingConnections = false;
					break;
				}
			}

			print(getTimestamp() +": Expected number of clients connected. Starting Game");
			rules.initFirstTournament();
			for(PlayerThread p : threads){
				p.start();
			}

		} catch(IOException e){
			error(getTimestamp() + ": Server socket unable to connect to port" + port);
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new Server();
	}

	/**
	 * creates a time stamp with the format Year.Month.Day.Hour.Min.Sec
	 * @return String representation of time stamp
	 */
	public String getTimestamp(){
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}

	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	public void print(String s){
		System.out.println(s);
	}

	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	public void error(String s){
		System.err.println(s);
	}

	class PlayerThread extends Thread {

		private Socket client;
		private int port;
		private InetAddress addr;
		private boolean isRunning = true;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private long threadID = getId(); //used to identify the individual threads in the rules/logic engine
		private int playerNum;

		public PlayerThread(Socket c){
			client = c;
			port = c.getPort();
			addr = c.getInetAddress();
			playerNum = rules.registerThread(threadID);
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}

		public void run(){
			/*
				if no tournament running
					run init tournament
						deals players hands
						sets their status to playing (instead of withdrawn)
						colourChosen = false
				endif

				start turn
						draw a card
				if colourchosen = false
					check if can start tournament with a colour
					set tournament colour to that colour
					if cant start tournament, 
						colourchosen = false
						reveal hand
					endif
				endif
				play rest of turn
				end turn or withdraw
			 */

			//log.logmsg(threadID + ": Main loop started");
			print(threadID + "");

			//Send player their player number
			send(playerNum);

			print(threadID + ": isRunning");
			while(isRunning){

				if (rules.getPlayerList().get(0).getID() != threadID) {
					try {
						//updateClientBoardState();
						Thread.sleep(500);
						continue;
					}
					catch (InterruptedException ie) {
						ie.printStackTrace();
						continue;
					}
				}

				if (rules.isTournamentRunning()) {
					//Start client turn and draw a card
					rules.startTurn(threadID);
					SendClientHand();
					sendPlayersList();
					//Is the tournament running AND not first turn in tournament
					if (!rules.isColourChosen()) {
						//choose colour
						if (rules.canStartTournament(threadID)) {
							CardColour c;
							c = GetTournamentColourFromClient();
							while(!rules.initializeTournamentColour(threadID, c)) {
								//send some message about bad colour input
								c = GetTournamentColourFromClient();
							}	
						}
						else {
							rules.failInitTournamentColour();
							//TODO tell client it cant start tournament
							continue;
						}
					}
					//Send updated hand to client
					SendClientHand();

					//get what cards the client wants to play
					int cardIndex = getCardsToBePlayed();
					while(cardIndex != -3){ //while not endturn optcode
						cardIndex = getCardsToBePlayed();

						if(cardIndex == -2){ //Client withdrawing
							if (rules.withdrawPlayer(threadID)) {
								CardColour c = GetTournamentColourFromClient();
								rules.getPlayerById(threadID).removeToken(c); //may need validation
							}
							long winner = rules.withdrawCleanup(threadID); //now its winner's turn, they'll get a choice of token when their loop hits code
							
							cardIndex = -3;
						} else if(cardIndex == -3) { //end turn optcode received
							rules.endTurn(threadID);
							cardIndex = -3;
						} else if(cardIndex != -1){
							rules.playCard(cardIndex, threadID);
							SendClientHand();
							updateClientBoardState();
						}
					}
				}
				
				else {
					if (rules.getPlayerById(threadID).getPlaying()) { //then you are winner of previous tourney
						CardColour c = GetTournamentColourFromClient();
						while(!rules.giveToken(threadID, c)) {
							//send some message about bad colour input
							c = GetTournamentColourFromClient();
						}
					}
					rules.roundCleanup();
					rules.initTournament();
				}
			}
		}



		/**
		 * Gets the displays for all players and sends it to the client
		 * Currently does not send ActionCards in the display
		 */
		private void updateClientBoardState(){
			ArrayList<List<Card>> board = new ArrayList<List<Card>>();
			List<Card> nonActionCards = rules.getPlayerById(threadID).getDisplay().getCards();
			//List<Card> actionCards = rules.getPlayerById(threadID).getDisplay().getActionCards();

			board.add(nonActionCards);

			for( Player p : rules.getPlayerList()){
				if(p.getID() != threadID){
					board.add(p.getDisplay().getCards());
				}
			}

			send(Optcodes.ClientupdateBoardState);
			send(board);
		}

		/**
		 * Checks if the client sent the withdraw optcode
		 * @param opt Integer
		 * @return Boolean
		 */
		private boolean isClientWithdrawing(int opt){
			if(opt == Optcodes.ClientWithdraw){
				return true;
			}
			return false;
		}

		/**
		 * Get the index of the card to be played and plays the card
		 */
		private int getCardsToBePlayed(){
			send(Optcodes.ClientGetCardsToBePlayed);
			int index = (int) get();
			String cardname = "";

			if(isClientWithdrawing(index)){ //Client withdraws
				return -2;
			}
			if (index == Optcodes.ClientEndTurn){ //client calls end turn
				return -3;
			}

			if(index < rules.getPlayerById(threadID).getHandSize()){
				cardname = rules.getPlayerById(threadID).getHand().getCardbyIndex(index).getCardName();
			}


			//if client sent a card
			if(cardname != ""){
				if(rules.validatePlay(cardname, threadID)){
					send(Optcodes.SuccessfulCardPlay);
					return index;
				} else {
					send(Optcodes.InvalidCard);
					return -1;
				}
			}
			return -1;
		}

		/**
		 * Sends the thread id representing the player in the rules engine to the player
		 */
		private void sendPlayerId(){
			//Register Thread with the rules engine
			int b = rules.registerThread(threadID);

			//if game full close connection
			if(b != -1){ 
				send(b); 
			} else {
				isRunning = false;
			}
		}

		private void sendPlayersList() {
			int me_index = 0;
			int listSize = rules.getPlayerList().size();
			List<Long> playerIDs = new ArrayList<Long>();
			for (; me_index < listSize; me_index++) {
				if (rules.getPlayerList().get(me_index).getID() == threadID) {
					break;
				}
			}
			for (int i=0; i < listSize; i++) {
				playerIDs.add(rules.getPlayerList().get((me_index+i)%listSize).getID());
			}
			print("Thread " + threadID + ": Sending playerlist to client");
			send(Optcodes.ClientGetPlayerList);
			send(playerIDs);

		}

		/**
		 * Gets the hand from the Player class and sends it to the client
		 */
		private void SendClientHand(){
			print("Thread " + threadID + ": Sending hand to client");
			List<Card> hand = rules.getPlayerById(threadID).getHand().getHand();
			send(Optcodes.ClientGetHand);
			send(hand);
		}

		/**
		 * Gets the tournament colour from the client
		 * @return CardColour
		 */
		private CardColour GetTournamentColourFromClient(){
			print("Thread " + threadID + ": getting tournament colour from client");
			send(Optcodes.ClientGetColourChoice);
			CardColour colour = null;

			int o = (int) get(); //get colour from client

			switch (o) {
			case 1: colour = Card.CardColour.Purple;
			break;
			case 2: colour = Card.CardColour.Green;
			break;
			case 3: colour = Card.CardColour.Red;
			break;
			case 4: colour = Card.CardColour.Blue;
			break;
			case 5: colour = Card.CardColour.Yellow;
			break;
			default:
				break;
			}

			return colour;
		}

		/**
		 * Gets an object from the client
		 * Does not verify the typeOf an object
		 * @return
		 */
		private Object get(){
			Object o = new Object();
			try {
				o = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			return o;
		}

		/**
		 * Sends an object to the client
		 * @param o Object to be sent
		 * @return boolean if successful
		 */
		private boolean send(Object o){
			try {
				out.writeObject(o);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
}
