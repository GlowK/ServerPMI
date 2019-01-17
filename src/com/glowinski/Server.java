package com.glowinski;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

    private int port;
    private List<ClientConnection> clientConnections;

    public static void main(String[] args) {
        Server server = new Server(1099);
        server.startServer();
    }

    private Server(int port) {
        this.port = port;
        this.clientConnections = new ArrayList<>();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server initialized");


            while(true) {

                Socket clientConnection = serverSocket.accept();
                System.out.println("New connection established with: " + clientConnection.getInetAddress().getHostAddress());

                ClientConnection newClientConnection = new ClientConnection(clientConnection);
                this.clientConnections.add(newClientConnection);

                Service service = new Service(this, newClientConnection);
                service.start();
            }
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }

    void sendMessage(String message, ClientConnection sender) {
        //Pętla for-each powtarzająca ten sam kod dla każdego aktualnie podłączonego klienta
        for(ClientConnection clientConnection : this.clientConnections) {
            String fullMessage;

            //TODO: Print local time, not server time.
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            fullMessage = dateFormat.format(date) + " " + sender.getUsername() + ": " + message;

            //Przeslanie wiadomosci po wczeniejsym dodaniu do niej aktualnej godziny i username'a klienta ktory nadał wiadomość.
            clientConnection.getOutputPrintWriter().println(fullMessage);
        }
    }

    void sendQuestion(Question question, ClientConnection sender){
        try {
            sender.getObjectOutputStream().writeObject(question);
            sender.getObjectOutputStream().reset();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void closeConnection(ClientConnection clientConnection) {
        System.out.println("ClientConnection closed with user");
        this.clientConnections.remove(clientConnection);
    }

}
