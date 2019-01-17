package com.glowinski;

import java.io.BufferedReader;
import java.io.IOException;

public class Service extends Thread {

    private Server server;
    private Connection connection;

    Service(Server server, Connection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void run() {
        String message;
        BufferedReader input = connection.getInputBufferedReader();

        //Główne czynność obsługująca klienta
        //W nieskończonej pętli czekaj na nadchodzącą wiadomośc a następnie zleć serwerowi rozesłąnie tej wiadomości do wszystkich
        try {
            while(true) {
                message = input.readLine();
                //TODO: Need better disconnection/closing handle.

                if(message.equals("exit"))
                    break;
                else if(message.equals("question")){
                    server.sendMessage("Pytanie 1: dupa,dupa", connection);
                    Question pytanko = new Question("babab","gruba");
                    server.sendQuestion(pytanko,connection);
                }
                else
                    //Roześlij do wsztskich połączonych klientów poprzez serwer
                    server.sendMessage(message, connection);
            }
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
        finally {
            server.closeConnection(connection);
            connection.closeConnection();
        }
    }
}
