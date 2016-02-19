package comp3004.ivanhoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

import org.hamcrest.core.Is;

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
	
	public int getPlayerNum(){
		return playerNum;
	}
}


