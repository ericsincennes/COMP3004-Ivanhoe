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
	//private Log			log = new Log(this.getClass().getName(), "ServerLog");
	private RulesEngine		rules;


	public Server(){
		Scanner in = new Scanner(System.in);
		int count = numplayers;

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
		connectAndRecieve(count);
	}

	private void connectAndRecieve(int count){
		try{

			print(getTimestamp() + ": server listening on port " + port);
			//log.logmsg(getTimestamp() + " : server listening on port " + port);
			listeningSocket = new ServerSocket(port);

			while(isAcceptingConnections){

				Socket clientSocket = listeningSocket.accept();

				print(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());
				//log.logmsg(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());

				count--;

				PlayerThread p = new PlayerThread(clientSocket);
				p.start();
				if(count == 0){
					listeningSocket.close();
					isAcceptingConnections = false;
				}
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
		private long threadID = Thread.currentThread().getId();	//used to identify the individual threads in the rules/logic engine

		public PlayerThread(Socket c){
			client = c;
			port = c.getPort();
			addr = c.getInetAddress();

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
		
			while(isRunning){
					
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
				board.add(p.getDisplay().getCards());
			}
			
			send(Optcodes.ClientupdateBoardState);
			send(board);
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
			for (me_index = 0; me_index < rules.getPlayerList().size(); me_index++) {
				if (rules.getPlayerList().get(me_index).getID() == threadID) {
					break;
				}
			}
			
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
			print("Thread " + threadID + ": getting tournamane colour from client");
			send(Optcodes.ClientGetColourChoice);
			
			CardColour o = (CardColour) get(); //get colour from client
			return o;
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
