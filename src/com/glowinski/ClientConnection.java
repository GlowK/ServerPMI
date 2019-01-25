package com.glowinski;

import java.io.*;
import java.net.Socket;


class ClientConnection {

    private Socket socket;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    public ClientConnection(Socket socket) {
        try {
            this.socket = socket;
            this.outToClient = new ObjectOutputStream(socket.getOutputStream());
            this.inFromClient = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
