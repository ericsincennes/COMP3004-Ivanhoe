package comp3004.ivanhoe;

import java.io.IOException;
import java.net.*;

public class Client {
	Socket socket;
	
	public static void main(String[] args){}

	public boolean connect(String IP, int port){
		InetAddress host;
		
		try {
			host = InetAddress.getByName(IP);
		} catch (UnknownHostException e1) {
			//e1.printStackTrace();
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
}

