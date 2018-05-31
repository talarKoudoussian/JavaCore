package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
        
        private int port = 0;
        private ServerSocket server = null;
        private BufferedInputStream bis = null;
        private BufferedOutputStream bos = null;
        private boolean loop = false;
        
        public Server() {
               
        }
        
        public void start() {
                Socket socket = null;
                
                try {
                        server = new ServerSocket(port);
                        loop = true;
                        
                        while(loop) {
                                System.out.println("Server started on port "+getPort());

                                socket = server.accept();
                                
                                InetAddress addr = socket.getInetAddress();
                                String ipAddr = addr.getHostAddress();
                                System.out.println("Client connected "+ipAddr+":"+socket.getLocalPort());
                                
                                InputStream in = socket.getInputStream();
                                OutputStream out = socket.getOutputStream();
                                bis = new BufferedInputStream(in);
                                bos = new BufferedOutputStream(out);
                                
                                int c = 0;
                                StringBuilder sb = new StringBuilder();
                                while(true) {
                                        c = bis.read();
                                        
                                        if(c == 0) {
                                                break;
                                        }
                                        
                                        sb.append((char)c);
                                }
                                
                                bos.write("Received\0".getBytes());
                                bos.flush();
                                
                                String msg = sb.toString();
                                sb = null;
                                
                                switch(msg) {
                                        case "exit": {
                                                bis.close();
                                                bos.close();
                                                socket.close();
                                                break;
                                        }
                                        
                                        case "kill": {
                                                bis.close();
                                                bos.close();
                                                socket.close();
                                                loop = false;
                                                break;
                                        }
                                        
                                        default: {
                                                
                                                System.out.println("Received: "+msg);
                                                break;
                                        }
                                }
                        }
                }
                catch(IOException ex) {
                        ex.printStackTrace();
                        
                        if(bis != null) {
                                try {
                                        bis.close();
                                }
                                catch(IOException e) {}
                        }

                        if(bos != null) {
                                try {
                                        bos.close();
                                }
                                catch(IOException e) {}
                        }

                        if(socket != null) {
                                try {
                                        socket.close();
                                }
                                catch(IOException e) {}
                        }
                }
                
                System.out.println("Shutting down Socket server!!");
                try {
                        server.close();
                }
                catch(IOException e) {
                        e.printStackTrace();
                }
        }
        
        public int getPort() {
                return port;
        }
        
        public void setPort(int port) {
                this.port = port;
        }

        public static void main(String... args) {
                Server srv = new Server();
                srv.setPort(3000);
                srv.start();
                System.out.println("Exited");

        }
}
