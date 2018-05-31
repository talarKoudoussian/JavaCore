package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Server {

        private int port = 0;
        private ServerSocket server = null;
        private BufferedInputStream bis = null;
        private BufferedOutputStream bos = null;
        private boolean loop = false;
        private Socket socket = null;

        public Server() {

        }

        public void start() {
                socket = null;

                try {
                        server = new ServerSocket(port);
                        loop = true;

                        while(loop) {
                                System.out.println("Server started on port " + getPort());

                                socket = server.accept();

                                InetAddress addr = socket.getInetAddress();
                                String ipAddr = addr.getHostAddress();
                                System.out.println("Client connected " + ipAddr + ":" + socket.getLocalPort());

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

                                String received = sb.toString();
                                String outMessage = "Server Received: \0" + sb.toString();
                                System.out.println(outMessage);
                                bos.write(received.getBytes());
                                String response = respond(received); // getResponse(received);
                                bos.write(response.getBytes());
                                bos.flush();

                                sb = null;

                                switch(received) {
                                        case "exit": {
                                                System.out.println("Received: " + response);
                                                bis.close();
                                                bos.close();
                                                socket.close();
                                                break;
                                        }

                                        case "kill": {
                                                System.out.println("Received: " + response);
                                                bis.close();
                                                bos.close();
                                                socket.close();
                                                loop = false;
                                                break;
                                        }

                                        default: {
                                                System.out.println("Received: " + response);
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

        private String getResponse(String message) {
                String response = "";
                switch(message) {
                        case "hello": {
                                response = "Hello how can I help?";
                                break;
                        }
                        case "time": {
                                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                                Date date = new Date();
                                response = sdf.format(date);
                                break;
                        }
                        case "thank you": {
                                response = "You're welcome";
                                break;
                        }
                        case "exit": {
                                response = "Exiting..";
                                break;
                        }
                        case "kill": {
                                response = "killing..";
                                break;
                        }
                        default: {
                                response = "Message unknown";
                                break;
                        }
                }

                return response;
        }
        
        private String respond(String message) {
                message = message.toLowerCase();
                String response = "";
               
                switch(message) {
                        case "date": {
                                response = getDate();
                                break;
                        }
                        case "date:iso8601": {
                                response = getISODate();
                                break;
                        }
                        case "datetime:iso8601": {
                                response = getISODateTime();
                                break;
                        }
                        default: {
                                response = "Message Unknown";
                                break;
                        }
                }
                
                return response;
        }
        
        private String getDate() {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                Date date = new Date();
                String response = sdf.format(date);
                return response;
        }

        private String getISODate() {
                String response = "";
                LocalDateTime dateNow = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
                response = dateNow.format(dtf);
                return response;
        }
        
        private String getISODateTime() {
                String response = "";
                LocalDateTime dateNow = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
                response = dateNow.format(dtf);
                return response;
        }
        
        public static void main(String... args) {
                Server srv = new Server();
                srv.setPort(3000);
                srv.start();
                System.out.println("Exited");

        }
}
