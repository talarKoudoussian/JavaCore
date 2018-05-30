package com.talar.server;

import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		int port = 3000;
		
		//create the socket server object
		ServerSocket server = new ServerSocket(port);
		
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true) {
        	
            System.out.println("Waiting for client request");
            
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            System.out.println("Accepted");
            
            //read from socket to ObjectInputStream object
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            
            //convert ObjectInputStream object to String
            String message = (String) inputStream.readObject();
            System.out.println("Message Received: " + message);
            
            //create ObjectOutputStream object
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            
            //write object to Socket
            outputStream.writeObject("Hi Client "+ message);
            
            //close resources
            inputStream.close();
            outputStream.close();
            socket.close();
            
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) {
            	break;
            }            
        }
        
        System.out.println("Shutting down Socket server!!");
        
        //close the ServerSocket object
        server.close();

	}
}
