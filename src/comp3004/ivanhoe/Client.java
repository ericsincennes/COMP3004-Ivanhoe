package src.comp3004.ivanhoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Client {
	Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	public static void main(String[] args){}

	public boolean connect(int port){
		InetAddress host;
		
		try {
			host = InetAddress.getByName("localhost");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return false;
		}
			
		try{
			socket = new Socket(host, port);
			System.out.println("Connection to " + host + " on port "+ port);
			return true;
		} catch(IOException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	private void print(String s){
		System.out.println(s);
	}

	private void start(){
		Scanner scan = new Scanner(System.in);
		//System.out.println("Enter port to connect on");
		//int p = in.nextInt();
		
		connect(2244);
		
		while(true){
			int selection = -1;
			print("Select what to send to server");
			print("(1)	- String");
			print("(2)	- int");
			print("(3)	- Array");
			
			while(selection < 0 || selection > 3){
				selection = scan.nextInt();
			}
			
			if(selection == 1){
				
			}
			else if(selection == 2){
				
			}
			else if(selection == 3){
				
			}
		}
	}
}

