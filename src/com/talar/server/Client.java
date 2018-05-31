package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

        public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {

                Socket socket = new Socket("localhost", 3000);
                System.out.println("Connected");
                
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                
                bos.write("Hi how are you?\0".getBytes());
                bos.flush();
                
                System.out.println("waiting for read");
                
                StringBuilder sb = new StringBuilder();
                while(true) {
                        int c = bis.read();
                        if(c == 0) {
                                break;
                        }
                        
                        sb.append((char)c);
                }
                
                System.out.println("Received: "+sb.toString());
        }
}
