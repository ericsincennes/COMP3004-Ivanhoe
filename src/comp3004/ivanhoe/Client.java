package comp3004.ivanhoe;

import java.io.IOException;
import java.net.*;

public class Client {
	
	public static void main(String[] args){
		Socket s;
		int port = 4488;
		InetAddress host = null;
		
		try{
			host = InetAddress.getByName("localhost");
		} catch(UnknownHostException e){
			e.printStackTrace();
		}
		
		System.out.println("Connection to " + host + " on port "+ port);
		
		try{
			s = new Socket(host, port);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
