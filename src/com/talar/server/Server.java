package com.talar.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

        private int port = 0;
        private ServerSocket server = null;

        public Server() {

        }

        public void start() {
                Socket socket = null;
                boolean loop = false;

                try {
                        server = new ServerSocket(port);
                        loop = true;

                        while(loop) {
                                System.out.println("Server started on port " + getPort());

                                socket = server.accept();

                                ServerProcess sp = new ServerProcess(socket);

                                InetAddress addr = socket.getInetAddress();
                                String ipAddr = addr.getHostAddress();
                                System.out.println("Client connected " + ipAddr + ":" + socket.getLocalPort());

                                while(sp.isClientOn()) {
                                        sp.run();
                                        loop = sp.isServerOn();
                                }
                        }
                }
                catch(IOException ex) {
                        ex.printStackTrace();
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
