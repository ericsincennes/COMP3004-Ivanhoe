package comp3004.ivanhoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;
import comp3004.ivanhoe.Optcodes;

public class Client {
	protected Socket socket;
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	protected int playerNum = -1;
	protected List<Card> CardsInHand;	//ArrayList of current hand
	protected List<Long> PlayersList;
	protected List<Integer> PointsList;
	protected BoardState theBoard;
	protected boolean isActiveTurn;
	protected Scanner scan = new Scanner(System.in);
	protected String colour;

	public static void main(String[] args){
		new Client();
	}

	public boolean isActiveTurn() {
		return isActiveTurn;
	}

	public Client(){
		CardsInHand = new ArrayList<Card>();
		
		connect(2244);
		start();
	}

	public boolean connect(int port){
		InetAddress host;
		print("Connecting to server...");
		
		print("Please enter an IP: ");
		String IPaddr = scan.next();

		try {
			//host = InetAddress.getByName("localhost");
			host = InetAddress.getByName(IPaddr);
			socket = new Socket(host, port);
			print("Connection to " + host + " on port "+ port);

			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void start(){
		playerNum = (int) get();	//get player number from server

		while(true){
			Object o = get();
			print("getting optcode from server " + o.getClass().getName() + " " + o.toString());
			int optcode = (int) o;

			switch(optcode) {
			case Optcodes.ClientGetColourChoice:
				handleGetTournamentColour();
				break;
			case Optcodes.ClientGetHand:
				handleGetHand();
				break;
			case Optcodes.ClientUpdateBoardState:
				handleUpdateBoardState();
				break;
			case Optcodes.ClientGetCardsToBePlayed:
				print("Choose a card to play, type 99 to withdraw, or type 66 to end turn.");
				sendCardsToBePlayed();
				break;
			case Optcodes.ClientGetPlayerList:
				getPlayersList();
				break;
			case Optcodes.InvalidCard:
				print("Card is unable to be played");
				break;
			case Optcodes.SuccessfulCardPlay:
				print("Card was played successfully");
				break;
			case Optcodes.ClientWithdraw:
				break;
			case Optcodes.ClientGetTokenChoice:
				handleTokenChoice();
				break;
			case Optcodes.ClientGetActionCardTarget:
				getActionCardTargets();
				break;
			case Optcodes.TournamentColour:
				setColour();
				break;
			case Optcodes.ClientGetPoints:
				getPoints();
				break; 
			case Optcodes.ClientActiveTurn:
				handleActiveTurn();
				isActiveTurn = true;
				break;
			case Optcodes.ClientNotActiveTurn:
				handleNonActiveTurn();
				break;
			default: new Exception("Unexpected Value");
				break;
			}
		}
	}
	


	private void handleNonActiveTurn() {
		isActiveTurn = false;
	}

	private void handleActiveTurn() {
		// TODO Auto-generated method stub
		isActiveTurn = true;
		print("The board state: \n");
		for (int i=theBoard.players.size()-1; i>=0; i--) {
			print("Board of player ID " +theBoard.players.get(i) +". Current points: " + theBoard.points.get(i));
			List<Card> l = theBoard.boards.get(i);
			List<Card> al = theBoard.actionBoards.get(i);
			for(Card c: l){
				System.out.print(c.getCardName() + " - ");
				System.out.println("");
			}
			if (al.size() > 0) for (Card c: al) {
				System.out.print("Action Cards affecting board: ");
				System.out.print(c.getCardName() + " - ");
			}
			
		}
		System.out.println("");
		
		print("Your hand:");
		for(Card c: theBoard.hand){
			System.out.print(c.getCardName() + " - ");
			
		}
		System.out.println("");
	}

	/**
	 * Gets the target for action cards
	 */
	private void getActionCardTargets(){
		//TODO Finish this
		ArrayList<String> targets = new ArrayList<String>();
		//get index of card to get data for from server
		int index = (Integer) get();
		Card c = CardsInHand.get(index);
		
		if(c.getCardType() == CardType.Action){
			
		}
	}
	
	private void getPlayersList() {
		Object o = get(); 
		print("getPlayersList() getting " + o.getClass().getName() + " " + o.toString());
		PlayersList = (List<Long>) o;
	}
	
	private void setColour() {
		Object o = get();
		print("setColour() getting " + o.getClass().getName() + " " + o.toString());
		colour = ((CardColour) o).name();
	}
	
	private void getPoints() {
		Object o = get();
		print("getPoints() getting " + o.getClass().getName() + " " + o.toString());
		PointsList = (List<Integer>) o;
		
	}
	
	private void handleGetHand(){
		Object o = get();
		print("handleGetHand() getting " + o.getClass().getName() + " " + o.toString());
		CardsInHand = (ArrayList<Card>) o;

		 
		print("Tournament Colour: " + colour);
		print("Cards currently in hand:");
		for (Card c: CardsInHand){
			System.out.print("(" + (CardsInHand.indexOf(c)+1) + ") - " + c.getCardName() + ".  ");
		}
		print("\n");
	}
	
	
	/**
	 * Gets input of what cards are goint to be sent to the server to be played
	 * If card is an action card then gets input of the card's targets 
	 */
	private void sendCardsToBePlayed(){
		int choice = 0;
		
		do {
			choice = scan.nextInt();
			if (choice == 99) {
				send(Optcodes.ClientWithdraw);
				break;
			} else if (choice == 66) {
				send(Optcodes.ClientEndTurn);
				break;
			} else if (choice < 1 || choice > theBoard.hand.size()) {
				print("Choose a number corresponding to a card in your hand");
			}
		} while (choice < 1 || choice > theBoard.hand.size());
		
		send(choice-1);
	}
	
	/**
	 * Gets the state of the board from the server
	 */
	private void handleUpdateBoardState(){
		Object o = get();
		print("handleUpdateBoardState() getting " + o.getClass().getName() + " " + o.toString());
		BoardState btmp = (BoardState) o;
		
		if (theBoard != null && theBoard.equals(btmp)) {
			
			return;
		}
		else {
			//debug
			if (theBoard != null) {
				print("btmp == theboard? " + btmp.equals(theBoard));
			}
			
			//end debug
			theBoard = btmp;
			if (theBoard.currColour != null) print("The tournament colour is " + theBoard.currColour.name() + ".\n");
			
			print("The board state: \n");
			for (int i=theBoard.players.size()-1; i>=0; i--) {
				print("Board of player ID " +theBoard.players.get(i) +". Current points: " + theBoard.points.get(i));
				List<Card> l = theBoard.boards.get(i);
				List<Card> al = theBoard.actionBoards.get(i);
				for(Card c: l){
					System.out.print(c.getCardName() + " - ");
					System.out.println("");
				}
				if (al.size() > 0) for (Card c: al) {
					System.out.print("Action Cards affecting board: ");
					System.out.print(c.getCardName() + " - ");
				}
				
			}
			System.out.println("");
			
			print("Your hand:");
			for(Card c: theBoard.hand){
				System.out.print(c.getCardName() + " - ");
				
			}
			System.out.println("");
		}
	}		

	private void handleTokenChoice(){
		int choice = -1;

		print("Choose the colour of the token you want");
		print("{1) - Purple");
		print("{2) - Green");
		print("{3) - Red");
		print("{4) - Blue");
		print("{5) - Yellow");

		while (choice == -1) {
			choice = scan.nextInt();
			if (choice < 1 || choice > 5){
				print("Please choose a number between 1 and 5");
				choice = -1;
			} 
		}

		send(choice);
	}
	
	/**
	 * Gets the player input for tournament colour
	 * @return Card.CardColour
	 */
	private void handleGetTournamentColour(){
		int choice = -1;

		print("Choose the color of the tournement");
		print("{1) - Purple");
		print("{2) - Green");
		print("{3) - Red");
		print("{4) - Blue");
		print("{5) - Yellow");

		while (choice == -1) {
			choice = scan.nextInt();
			if (choice < 1 || choice > 5){
				print("Please choose a number between 1 and 5");
				choice = -1;
			} 
		}

		send(choice);
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
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void print(String s){
		System.out.println(s);
	}
	
	private void printlist(String s){
		System.out.print(s);
	}

	public int getPlayerNum(){
		return playerNum;
	}
}