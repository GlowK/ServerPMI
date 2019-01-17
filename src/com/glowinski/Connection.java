package com.glowinski;

import java.io.*;
import java.net.Socket;


class Connection {

    private Socket socket;
    private String username;
    //Strumienie wejścia i wyjścia aktualnego połączenia służace do wysyłąnia i odbierania wiadomości
    private BufferedReader input;
    private PrintWriter output;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private Connection(Socket socket, String username) {

        try {
            this.socket = socket;
            this.username = username;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(),true);
            this.outToClient = new ObjectOutputStream(socket.getOutputStream());
            this.inFromClient = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }

    //Alternatywny konstruktor, gdy nie podany został username
    Connection(Socket socket) {
        this(socket, "Anonymous");
    }

    //Klika getterów, żeby inne klasy miały dostęp do atrybutów tej klasy. W tym wypadku package protected.
    BufferedReader getInputBufferedReader() {
        return this.input;
    }

    PrintWriter getOutputPrintWriter() {
        return this.output;
    }

    ObjectOutputStream getObjectOutputStream(){
        return this.outToClient;
    }

    ObjectInputStream getObjectInputStream(){
        return this.inFromClient;
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
