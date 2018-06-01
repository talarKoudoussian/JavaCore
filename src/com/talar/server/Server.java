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
                                
                                InetAddress addr = socket.getInetAddress();
                                String ipAddr = addr.getHostAddress();
                                System.out.println("Client connected " + ipAddr + ":" + socket.getLocalPort());
                                
                                boolean incoming = true;
                                while(incoming) {
                                        InputStream in = socket.getInputStream();
                                        OutputStream out = socket.getOutputStream();
                                        bis = new BufferedInputStream(in);
                                        bos = new BufferedOutputStream(out);
        
                                        StringBuilder sb = new StringBuilder();
                                        
                                        while(true) {
                                                int c = bis.read();
        
                                                if(c == 0) {
                                                        break;
                                                }
        
                                                sb.append((char)c);
                                        }
        
                                        String receivedMessage = sb.toString();
                                        String outMessage = "Server Received: \0" + sb.toString();
                                        System.out.println(outMessage);
                                        String response = getResponse(receivedMessage);
                                        response += "\0";
                                        bos.write(response.getBytes());
                                        bos.flush();
        
                                        sb = null;
        
                                        switch(receivedMessage) {
                                                case "exit": {
                                                        System.out.println("Received Response: " + response);
                                                        bis.close();
                                                        bos.close();
                                                        socket.close();
                                                        incoming = false;
                                                        break;
                                                }
        
                                                case "kill": {
                                                        System.out.println("Received Response: " + response);
                                                        bis.close();
                                                        bos.close();
                                                        socket.close();
                                                        incoming = false;
                                                        loop = false;
                                                        break;
                                                }
        
                                                default: {
                                                        System.out.println("Received Response: " + response);
                                                        break;
                                                }
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
                String format = "";
                
                if(isFormatDefined(message)) {
                        format = getDateFormat(message);
                        message = "Date;";
                }
               
                switch(message) {
                        case "Date": {
                                response = getDate();
                                break;
                        }
                        case "Date:ISO8601": {
                                response = getISODate();
                                break;
                        }
                        case "DateTime:ISO8601": {
                                response = getISODateTime();
                                break;
                        }
                        case "Date;" : {
                                response = getCustomDate(format);
                                break;
                        }
                        case "Time" : {
                                response = getTime();
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
                                response = "Message Unknown";
                                break;
                        }
                }
                
                return response;
        }
        
        private String getDate() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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
        
        private String getCustomDate(String format) {
                String response = "";
                LocalDateTime dateNow = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
                response = dateNow.format(dtf);
                return response;
        }
        
        private String getDateFormat(String message) {
                String dateFormat = "";
                
                if(isFormatDefined(message)) {
                        String[] args = message.split(";");
                        dateFormat = args[1];
                }
                
                return dateFormat;
        }
        
        private boolean isFormatDefined(String message) {
                boolean isDefined = false;
                String[] args = message.split(";");
                
                if(args.length > 1) {
                        isDefined = true;
                }
                
                return isDefined;
        }
        
        private String getTime() {
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                Date date = new Date();
                String response = sdf.format(date);
                return response;
        }

        public static void main(String... args) {
                Server srv = new Server();
                srv.setPort(3000);
                srv.start();
                System.out.println("Exited");

        }
}
