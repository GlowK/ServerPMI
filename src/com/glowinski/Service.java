package com.glowinski;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Service extends Thread {

    private Server server;
    private ClientConnection clientConnection;
    private ArrayList<Question> shortQuestionList = new ArrayList<>();
    private ArrayList<Answer> shortAnswerList = new ArrayList<>();

    private static final int NUMBER_OF_QUESTIONS = 10;
    private static final int NUMBER_OF_QUESTIONS_IN_DB = 10; //317

    Service(Server server, ClientConnection clientConnection) {
        this.server = server;
        this.clientConnection = clientConnection;
    }

    @Override
    public void run() {
        String message;
        BufferedReader input = clientConnection.getInputBufferedReader();

        MySQLConnection dbz = new MySQLConnection();
        dbz.initializeConnectionToDB();


        for(int i = 0; i<NUMBER_OF_QUESTIONS ; i++){
            new Question();
            new Answer();
            int randomNum = ThreadLocalRandom.current().nextInt(0, NUMBER_OF_QUESTIONS_IN_DB);
            Question pytanko = dbz.getQuestionFromDB(randomNum);
            Answer odpowiedz = dbz.getAnswerFromDB(randomNum);
            shortQuestionList.add(pytanko);
            shortAnswerList.add(odpowiedz);
        }

        //System.out.println(shortQuestionList.size());
        int iterator = 0;

        //Główne czynność obsługująca klienta
        //W nieskończonej pętli czekaj na nadchodzącą wiadomośc a następnie zleć serwerowi rozesłąnie tej wiadomości do wszystkich
        try {
            while(true) {
                message = input.readLine();
                //TODO: Need better disconnection/closing handle.

                if(message.equals("exit"))
                    break;
                else if(message.equals("question++")){
                    //server.sendMessage("Pytanie 1: dupa,dupa", clientConnection);
                    //Question pytanko = new Question("Pytanie","Odpowiedz1", "Odpowiedz2", "Odpowiedz3", "Odpowiedz4");
                    server.sendQuestion(shortQuestionList.get(iterator++), clientConnection);
                                    }
                //else
                    //Roześlij do wsztskich połączonych klientów poprzez serwer
                    //server.sendMessage(message, clientConnection);
            }
        }
        catch(IOException error) {
            System.out.println("Server error: " + error.getMessage());
        }
        finally {
            server.closeConnection(clientConnection);
            clientConnection.closeConnection();
        }
    }
}
