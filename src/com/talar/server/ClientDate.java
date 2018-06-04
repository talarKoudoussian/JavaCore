package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientDate {

        public static void main(String... args) throws UnknownHostException, IOException {

                Socket socket = new Socket("localhost", 3000);
                
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(bis));
                
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Enter a message");
                System.out.println("List of commands: [Date, Date:ISO8601, DateTime:ISO8601, Date;format ,Time, exit, kill]");

                boolean loop = true;
                
                while(loop) {
                        String receiveMessage;
                        String sendMessage = input.readLine();
                        
                        if(sendMessage.equals("exit") || sendMessage.equals("kill")) {
                                loop = false;
                        }
                        
                        bos.write(sendMessage.getBytes());
                        bos.write("\0".getBytes());
                        bos.flush();
        
                        StringBuilder sb = new StringBuilder();
                        
                        while(true) {
                                int c = br.read();
        
                                if(c == 0) {
                                        break;
                                }
        
                                sb.append((char)c);
                        }
                        
                        receiveMessage = sb.toString();
                        
                        if(receiveMessage != "")
                        {
                                System.out.println("Received from server: " + receiveMessage);
                        }
                }
                
                socket.close();                
        }

}
