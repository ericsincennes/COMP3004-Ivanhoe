package comp3004.ivanhoe;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import comp3004.ivanhoe.testcases.Log;

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

			while(true){

				Socket clientSocket = listeningSocket.accept();

				print(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());
				//log.logmsg(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());

				count--;

				Player p = new Player(clientSocket);
				p.start();
				/*
				if(count == 0){
					listeningSocket.close();
					isAcceptingConnections = false;
				}
				*/
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

	class Player extends Thread {

		private Socket client;
		private int port;
		private InetAddress addr;
		private boolean isRunning = true;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private Hand hand = new Hand();
		private long threadID = this.currentThread().getId();	//used to identify the individual threads in the rules/logic engine
		Player player;
		
		public Player(Socket c){
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
			
			int b = rules.registerThread(threadID);
			if(b != -1){ 
				send(b); 
			} else {
				isRunning = false;
			}
			
			while(isRunning){
				Object o = get();
				if(o instanceof byte[]){
					if(o.equals(new byte[]{1,1,1,1})){
						rules.initializeTournamentColour(null);
					}
				}
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
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
}
