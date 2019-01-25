package com.glowinski;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Service extends Thread {

    private Server server;
    private ClientConnection clientConnection;
    private ArrayList<Question> shortQuestionList = new ArrayList<>();
    private ArrayList<Answer> shortAnswerList = new ArrayList<>();
    private ArrayList<Answer> clientAnswerList = new ArrayList<>();

    private static final int NUMBER_OF_QUESTIONS = 10;
    private static final int NUMBER_OF_QUESTIONS_IN_DB = 317; //317

    Service(Server server, ClientConnection clientConnection) {
        this.server = server;
        this.clientConnection = clientConnection;
    }

    @Override
    public void run() {
        ObjectInputStream objectInput = clientConnection.getObjectInputStream();
        MySQLConnection dbz = new MySQLConnection();
        dbz.initializeConnectionToDB();

        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
            new Question();
            new Answer();
            int randomNum = ThreadLocalRandom.current().nextInt(1, NUMBER_OF_QUESTIONS_IN_DB);
            Question pytanko = dbz.getQuestionFromDB(randomNum);
            Answer odpowiedz = dbz.getAnswerFromDB(randomNum);
            shortQuestionList.add(pytanko);
            shortAnswerList.add(odpowiedz);
        }

        System.out.println("Question set received...");
        System.out.println("Answer set received...");
        int iterator = 0;

        try {
            while (true) {
                    Object obj;
                    obj = (Object) objectInput.readObject();
                    if (obj instanceof Message) {
                        Message mes = (Message) obj;
                        if (mes.getMessage().equals("question++")) {
                            server.sendQuestion(shortQuestionList.get(iterator++), clientConnection);
                        }else if (mes.getMessage().equals("check")){
                            System.out.println("Client resutlt: " + compareAnswers(shortAnswerList,clientAnswerList) + " Sending...");
                            server.sendMessage(returnClientResult(compareAnswers(shortAnswerList,clientAnswerList)), clientConnection);
                        }
                    }else if (obj instanceof Answer){
                        Answer ans = (Answer)obj;
                        clientAnswerList.add(ans);
                        //printClientAnswers(clientAnswerList);
                    }
            }
        }catch (Exception e1){
                //e1.printStackTrace();
        }finally {
            server.closeConnection(clientConnection);
            clientConnection.closeConnection();
        }
    }

    private void printClientAnswers(ArrayList<Answer> cl){
        System.out.println("Drukuje otrzymane odpowiedzi");
        for( Answer ans : cl){
            System.out.println(ans.toString());
        }
    }

    private int compareAnswers(ArrayList<Answer> serverList, ArrayList<Answer> clientList){
        int result = 0;
        int iterator = 0;
        for(Answer ans : serverList){
            if(compareTwoSpecificAnswers(ans, clientList.get(iterator))){
                //System.out.println("sukces");
                //System.out.println(ans.toString());
                //System.out.println(clientList.get(iterator));
                result++;
                iterator++;
            }else{
                iterator++;
            }
        }
        return result;
    }

    private String returnClientResult(int result){
        String overallResult;
        overallResult = "Your overall result: "+ result +"/"+NUMBER_OF_QUESTIONS;
        return overallResult;
    }

    private boolean compareTwoSpecificAnswers(Answer a, Answer b){
        if((a.isA1() == b.isA1()
                && (a.isA2() == b.isA2())
                && (a.isA3() == b.isA3())
                && (a.isA4() == b.isA4())
                && (a.isA5() == b.isA5()))){
            return true;
        }else{
                return false;
        }
    }
}
