package com.glowinski;

import java.sql.*;

public class MySQLConnection {

    private Connection con;

    public MySQLConnection(){

    }

    boolean driverInitialization() {
        System.out.print("DB Driver check:");
        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    static Connection connectToDatabase(String adress, String dataBaseName, String userName, String password) {
        System.out.print("\nConnecting to DB :");
        String baza = "jdbc:mysql://" + adress + "/" + dataBaseName;
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(baza, userName, password);
        } catch (SQLException e) {
            System.out.println(e);
            System.exit(1);
        }
        return connection;
    }

    static void closeConnection(Connection connection, Statement s) {
        System.out.print("\n Closing DB connection:");
        try {
            s.close();
            connection.close();
        } catch (SQLException e) {
            System.out
                    .println("Error: " + e.toString());
            System.exit(4);
        }
        System.out.print(" Close OK");
    }

    void initializeConnectionToDB(){
        if (driverInitialization())
            System.out.print(" driver OK");
        else
            System.exit(1);

        con = connectToDatabase("18.197.197.247",
                "PMI", "root", "Lamora");
        if (con != null)
            System.out.print(" Connection OK\n");
    }

    private static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
        return null;
    }

    private static ResultSet executeQuery(Statement s, String sql) {
        try {
            return s.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    Question getQuestionFromDB(int i){
        Question q = new Question();
        Statement s = createStatement(this.con);
        String sql = "SELECT question, answerA, answerB, answerC, answerD, answerE FROM PMI.Questions where questionID=" + i + ";";
        ResultSet r = executeQuery(s, sql);
        try{
            while(r.next()){
                q.setQuestion(r.getString("question"));
                if(r.wasNull()){
                    q.setQuestion("");
                }
                q.setAnswer1(r.getString("answerA"));
                if(r.wasNull()){
                    q.setAnswer1("");
                }
                q.setAnswer2(r.getString("answerB"));
                if(r.wasNull()){
                    q.setAnswer2("");
                }
                q.setAnswer3(r.getString("answerC"));
                if(r.wasNull()){
                    q.setAnswer3("");
                }
                q.setAnswer4(r.getString("answerD"));
                if(r.wasNull()){
                    q.setAnswer4("");
                }
                q.setAnswer5(r.getString("answerE"));
                if(r.wasNull()){
                    q.setAnswer5("");
                }
                System.out.println(q.toString());
        }
        }catch(Exception e){
            System.out.println("SQL error" + e);
        }
        return q;
    }

    Answer getAnswerFromDB(int i){
        Answer a = new Answer();
        Statement s = createStatement(this.con);
        String sql = "SELECT a1, a2, a3, a4, a5 from PMI.Answers where question_id =" + i + ";";
        ResultSet r = executeQuery(s, sql);
        try{
            while(r.next()){
                a.setA1(r.getBoolean("a1"));
                a.setA2(r.getBoolean("a2"));
                a.setA3(r.getBoolean("a3"));
                a.setA4(r.getBoolean("a4"));
                a.setA5(r.getBoolean("a5"));
                System.out.println(a.toString());
            }
        }catch(Exception e){
            System.out.println("SQL error" + e);
        }
        return a;
    }
}
