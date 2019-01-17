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
    private List<Connection> connections;

    public static void main(String[] args) {
        Server server = new Server(1099);
        server.startServer();
    }

    private Server(int port) {
        this.port = port;
        this.connections = new ArrayList<>();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Server initialized");


            while(true) {

                Socket clientConnection = serverSocket.accept();
                System.out.println("New connection established with: " + clientConnection.getInetAddress().getHostAddress());

                Connection newConnection = new Connection(clientConnection);
                this.connections.add(newConnection);

                //Wysłanie wiadomości powitalnej do klienta który właśnie się połączył
                //TODO: Add info for other users that someone joined.
                //newConnection.getOutputPrintWriter().println("Welcome to MMOP Server! User count: " + newConnection.getNumberOfConnections());
                //Utworzenie nowej instancji klasy Service, która pozwala wielowątkowo obsługiwać nowe połączenie
                //W tym wypadku jest to klasa dziedzicząca po klasie Thread, dlatego po utowrzeniu wywołujemy metodę start()

                Service service = new Service(this, newConnection);
                service.start();
            }
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
    }

    //Głowne działąnie serwera - pozwól klientom na zlecenie rozesłania wiadomości do wszytskich
    //Metoda rozsyłająca wiadomość do wszytskich aktualnie połączonych klientów.
    void sendMessage(String message, Connection sender) {
        //Pętla for-each powtarzająca ten sam kod dla każdego aktualnie podłączonego klienta
        for(Connection connection : this.connections) {
            String fullMessage;

            //TODO: Print local time, not server time.
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            fullMessage = dateFormat.format(date) + " " + sender.getUsername() + ": " + message;

            //Przeslanie wiadomosci po wczeniejsym dodaniu do niej aktualnej godziny i username'a klienta ktory nadał wiadomość.
            connection.getOutputPrintWriter().println(fullMessage);
        }
    }

    void sendQuestion(Question question, Connection sender){
        try {
            sender.getObjectOutputStream().writeObject(question);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void closeConnection(Connection chatConnection) {
        System.out.println("Connection closed with user");
        this.connections.remove(chatConnection);
    }

}
