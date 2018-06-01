package com.talar.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ServerProcess extends Thread {

        private Socket socket = null;
        private BufferedInputStream bis = null;
        private BufferedOutputStream bos = null;
        private boolean clientOn = false;
        private boolean serverOn = false;

        public ServerProcess(Socket socket) {
                this.socket = socket;
                clientOn = true;
                serverOn = true;
                
                InetAddress addr = socket.getInetAddress();
                String ipAddr = addr.getHostAddress();
                System.out.println("Client connected " + ipAddr + ":" + socket.getLocalPort());
        }
        
        public void init() throws IOException {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(out);
        }

        @Override
        public void run() {
                while(isClientOn()) {
                        String message = readInput();
                        System.out.println("Server Received: " + message);

                        String response = getResponse(message);
                        System.out.println("Response: " + response);
                        writeOutput(response);
                }

                close();
        }

        private String readInput() {
                String message = "";

                try {
                        StringBuilder sb = new StringBuilder();
                        
                        int c = 0;
                        while(true) {
                                
                                c = bis.read();

                                if(c == 0) {
                                        break;
                                }

                                sb.append((char)c);
                        }

                        message = sb.toString();
                }
                catch(IOException e) {
                        e.printStackTrace();
                }

                return message;
        }

        private void writeOutput(String response) {
                try {
                        bos.write(response.getBytes());
                        bos.flush();
                }
                catch(IOException e) {
                        e.printStackTrace();
                }

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
                        case "Date;": {
                                response = getCustomDate(format);
                                break;
                        }
                        case "Time": {
                                response = getTime();
                                break;
                        }
                        case "exit": {
                                exit();
                                response = "Exiting..";
                                break;
                        }
                        case "kill": {
                                kill();
                                response = "killing..";
                                break;
                        }
                        default: {
                                response = "Message Unknown";
                                break;
                        }
                }
                response += "\0";
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
                String response = "";
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                Date date = new Date();
                response = sdf.format(date);
                return response;
        }
        
        private void exit() {
                clientOn = false;
        }
        
        private void close() {
                try {
                        bis.close();
                        bos.close();
                        socket.close();
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
        }
        
        private void kill() {
                clientOn = false;
                serverOn = false;
        }
        
        public boolean isClientOn() {
                return this.clientOn;
        }
        
        public boolean isServerOn() {
                return this.serverOn;
        }
}
