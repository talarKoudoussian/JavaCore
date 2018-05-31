package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client {

        public static void main(String[] args) throws UnknownHostException, IOException {

                Socket socket = new Socket("localhost", 3000);
                System.out.println("Connected");

                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                String[] requestMessages = { "hello\0", "time\0", "thank you\0", "weather\0", "exit\0", "kill\0" };

                Random rand = new Random();
                int randNbr = rand.nextInt(requestMessages.length);
                String message = requestMessages[randNbr];
                System.out.println(message);


                bos.write(message.getBytes());
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

                System.out.println("Client Received: " + sb.toString());
        }
}
