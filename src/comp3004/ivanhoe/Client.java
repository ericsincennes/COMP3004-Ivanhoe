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
import comp3004.ivanhoe.Optcodes;

public class Client {
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	int playerNum = -1;
	List<Card> CardsInHand;	//ArrayList of current hand
	List<Long> PlayersList;
	ArrayList<List<Card>> BoardState;
	Scanner scan = new Scanner(System.in);

	public static void main(String[] args){
		new Client();
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
			int optcode = (int) get();

			switch(optcode) {
			case Optcodes.ClientGetColourChoice:
				handleGetTournamentColour();
				break;
			case Optcodes.ClientGetHand:
				handleGetHand();
				break;
			case Optcodes.ClientupdateBoardState:
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
			default: new Exception("Unexpected Value");
				break;
			}
		}
	}
	
	private void getPlayersList() {
		PlayersList = (List<Long>) get();
	}
	
	private void handleGetHand(){
		CardsInHand = (ArrayList<Card>) get();

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
			} else if (choice < 1 || choice > CardsInHand.size()) {
				print("Choose a number corresponding to a card in your hand");
			}
		} while (choice < 1 || choice > CardsInHand.size());
		
		send(choice-1);
	}
	
	/**
	 * Updates the state of the display and opponents display for the player
	 * Should print displays and hand together as both will be updated on action
	 */
	private void handleUpdateBoardState(){
		//calls printboard state and get hand
		BoardState = (ArrayList<List<Card>>) get();
		
		for (ListIterator<List<Card>> it = BoardState.listIterator(BoardState.size()); it.hasPrevious();) {
			List<Card> l = it.previous();
			System.out.println("Player " + (BoardState.indexOf(l)+1) + "'s board");
			for(Card c: l){
				System.out.print(c.getCardName() + " - ");
			}
			System.out.println("\n");
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

		switch (choice) {
			case 1: send(1);
					break;
			case 2: send(2);
					break;
			case 3: send(3);
					break;
			case 4: send(4);
					break;
			case 5: send(5);
					break;
			default:
					break;
		}
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

		switch (choice) {
			case 1: send(1);
					break;
			case 2: send(2);
					break;
			case 3: send(3);
					break;
			case 4: send(4);
					break;
			case 5: send(5);
					break;
			default:
					break;
		}
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


