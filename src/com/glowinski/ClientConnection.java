package com.glowinski;

import java.io.*;
import java.net.Socket;


class ClientConnection {

    private Socket socket;
    private String username;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private ClientConnection(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.outToClient = new ObjectOutputStream(socket.getOutputStream());
            this.inFromClient = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }

    ClientConnection(Socket socket) {
        this(socket, "Anonymous");
    }

    ObjectOutputStream getObjectOutputStream(){
        return this.outToClient;
    }

    ObjectInputStream getObjectInputStream(){
        return this.inFromClient;
    }

    public Socket getSocket() {
        return socket;
    }

    void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }
}
