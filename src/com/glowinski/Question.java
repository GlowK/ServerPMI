package com.glowinski;

import java.io.Serializable;

public class Question implements Serializable {

    private String question;
    private String answer1;

    public Question(){
    }

    public Question(String a, String b){
        question = a;
        answer1 = b;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer1='" + answer1 + '\'' +
                '}';
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }
}
