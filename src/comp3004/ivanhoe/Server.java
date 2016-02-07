package comp3004.ivanhoe;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server{

	protected int 			port;
	protected int 			numplayers;
	protected boolean 		isAcceptingConnections = true;
	protected ServerSocket 	listeningSocket;

	public Server(){
		connect();
	}

	public static void main(String[] args){
		new Server();
	}

	private void connect(){

		Scanner in = new Scanner(System.in);
		int count = numplayers;

		System.out.println("Enter port to be used");
		port = in.nextInt();

		while(numplayers < 2 || numplayers > 5){
			print("Enter number of players to play (between 2 and 5)");
			numplayers = in.nextInt();
		}
		in.close();

		try{
			listeningSocket = new ServerSocket(port);
			while(isAcceptingConnections){
				print(getTimestamp() + " : server listening on port " + port);
				Socket clientSocket = listeningSocket.accept();

				print(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());
				count--;
				new clientConnection(clientSocket);
				if(count == 0){
					listeningSocket.close();
					isAcceptingConnections = false;
				}
			}
		} catch(IOException e){
			error("Connect: Server socket unable to connect to port" );
			e.printStackTrace();
		}
	}

	/**
	 * creates a time stamp with the format Year.Month.Day.Hour.Min.Sec
	 * @return String representation of time stamp
	 */
	public synchronized String getTimestamp(){
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}

	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	public synchronized void print(String s){
		System.out.println(s);
	}

	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	public synchronized void error(String s){
		System.err.println(s);
	}

	class clientConnection extends Thread {

		protected Socket client;

		public clientConnection(Socket c){
			client = c;
			print("new thread started");
			start();
		}

		public void run(){

		}
	}
}
