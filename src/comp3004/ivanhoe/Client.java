package comp3004.ivanhoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

import org.hamcrest.core.Is;

import comp3004.ivanhoe.Card.CardColour;

public class Client {
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	int playerNum = -1;
	
	public static void main(String[] args){
		new Client();
	}

	public Client(){
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
			Object o = get();
			if(o instanceof byte[]){
				if((byte[]) o == new byte[]{0,0,0,0}){
					send(getTournementColor());
				}
			}
		}
	}
	
	/**
	 * Gets the player input for tournament colour
	 * @return Card.CardColour
	 */
	private Card.CardColour getTournementColor(){
		Scanner i = new Scanner(System.in);
		int choice = -1;
		
		print("Choose the color of the tournement");
		print("{1) - Purple");
		print("{2) - Green");
		print("{3) - Red");
		print("{4) - Blue");
		print("{5) - Yellow");
		print("{6) - White");
		
		do{
			choice = i.nextInt();
			if(choice < 1 || choice > 6){
				print("Please choose a number between 1 and 6");
			}
		}while(choice < 1 || choice > 6);
		
		if(choice == 1){ return Card.CardColour.Purple; }
		else if (choice == 2){return Card.CardColour.Green; }
		else if (choice == 3){return Card.CardColour.Red; }
		else if (choice == 4){return Card.CardColour.Blue; }
		else if (choice == 5){return Card.CardColour.Yellow; }
		else if (choice == 6){return Card.CardColour.White; }
		else { return null; }
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


