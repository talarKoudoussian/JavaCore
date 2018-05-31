package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientBot {

        public static void main(String... args) throws UnknownHostException, IOException {

                Socket socket = new Socket("localhost", 3000);
                BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedReader receiveRead = new BufferedReader(new InputStreamReader(bis));

                String receiveMessage, sendMessage;               
                System.out.println("Enter a message");
                System.out.println("List of commands: [Date, Date:ISO8601, dateTime:ISO8601]");
                sendMessage = keyRead.readLine();
                sendMessage += "\0";
                bos.write(sendMessage.getBytes());
                bos.flush();

                receiveMessage = receiveRead.readLine();
                   
                if(receiveMessage != null)
                {
                        System.out.println(receiveMessage);
                }                
        }

}
