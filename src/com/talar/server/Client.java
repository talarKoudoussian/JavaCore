package com.talar.server;

import java.io.*;
import java.net.*;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        int port = 3000;
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        
        for(int i=0; i<5; i++){
        	
            //establish socket connection to server
            socket = new Socket(host.getHostName(), port);
            
            //write to socket using ObjectOutputStream
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            
            if(i==4) {
            	outputStream.writeObject("exit");
            }
            else {
            	outputStream.writeObject("" + i);
            }
            
            //read the server response message
            inputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) inputStream.readObject();
            System.out.println("Message: " + message);
            
            //close resources
            inputStream.close();
            outputStream.close();
            Thread.sleep(100);
        }
    }
	
}
