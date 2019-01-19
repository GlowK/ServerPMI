package com.glowinski;

import java.io.*;
import java.net.Socket;


class ClientConnection {

    private Socket socket;
    private String username;

    //private BufferedReader input;
    //private PrintWriter output;
    //private BufferedInputStream inFromClient;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private ClientConnection(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            //this.outToClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //this.inFromClient = new BufferedInputStream(new ObjectInputStream(socket.getInputStream()));
            //this.output = new PrintWriter(socket.getOutputStream(),true);
            this.outToClient = new ObjectOutputStream(socket.getOutputStream());
            this.inFromClient = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }

    //Alternatywny konstruktor, gdy nie podany został username
    ClientConnection(Socket socket) {
        this(socket, "Anonymous");
    }


   // BufferedReader getInputBufferedReader() {
   //     return this.input;
   //}

   // PrintWriter getOutputPrintWriter() {
   //     return this.output;
   // }

    ObjectOutputStream getObjectOutputStream(){
        return this.outToClient;
    }

    ObjectInputStream getObjectInputStream(){
        return this.inFromClient;
    }

    public Socket getSocket() {
        return socket;
    }

    String getUsername() {
        return this.username;
    }

    void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }
}
