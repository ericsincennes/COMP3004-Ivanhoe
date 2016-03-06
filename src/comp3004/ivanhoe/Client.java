package comp3004.ivanhoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Optcodes;

public class Client {
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	int playerNum = -1;
	List<Card> CardsInHand;	//ArrayList of current hand

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

		try {
			host = InetAddress.getByName("localhost");

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
				sendCardsToBePlayed();
				break;
			default: new Exception("Unexpected Value");
				break;
			}
		}
	}
	
	/**
	 * Prints the Players Display and the display of all other players playiing
	 */
	private void printBoardState(){
		
	}
	
	/**
	 * Gets input of what cards are goint to be sent to the server to be played
	 * If card is an action card then gets input of the card's targets 
	 */
	private void sendCardsToBePlayed(){
		Scanner in = new Scanner(System.in);
		int choice = -1;
		
		do {
			choice = in.nextInt();
			if (choice < 1 || choice > CardsInHand.size()) {
				print("Choose a number corresponding to a card in your hand");
			}
		} while (choice < 1 || choice > CardsInHand.size());
		
		send(CardsInHand.get((choice-1)));
		in.close();
	}
	
	/**
	 * Updates the state of the display and opponents display for the player
	 */
	private void handleUpdateBoardState(){
		//calls printboard state and get hand
		printBoardState();
		handleGetHand();
	}
	
	/**
	 * Gets the hand from the server and displays it to the player
	 */
	private void handleGetHand(){
		CardsInHand = (List<Card>) get();
		print("Cards currently in hand:");
		for (Card c: CardsInHand){
			print("{" + (CardsInHand.indexOf(c)+1) + ") - " + c.getCardName());
		}
		print("Choose a card to play or withdraw");
	}			

	/**
	 * Gets the player input for tournament colour
	 * @return Card.CardColour
	 */
	private void handleGetTournamentColour(){
		Scanner in = new Scanner(System.in);
		int choice = -1;

		print("Choose the color of the tournement");
		print("{1) - Purple");
		print("{2) - Green");
		print("{3) - Red");
		print("{4) - Blue");
		print("{5) - Yellow");

		do{
			choice = in.nextInt();
			if (choice < 1 || choice > 5){
				print("Please choose a number between 1 and 5");
			}
		} while (choice < 1 || choice > 5);

		switch (choice) {
			case 1: send(Card.CardColour.Purple);
					break;
			case 2: send(Card.CardColour.Green);
					break;
			case 3: send(Card.CardColour.Red);
					break;
			case 4: send(Card.CardColour.Blue);
					break;
			case 5: send(Card.CardColour.Yellow);
					break;
			default:
					break;
		}
		in.close();
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

	public int getPlayerNum(){
		return playerNum;
	}
}


