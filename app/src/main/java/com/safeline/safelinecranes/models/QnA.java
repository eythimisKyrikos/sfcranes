package com.safeline.safelinecranes.models;

public class QnA {
    private String question;
    private String answer;
    private int answerValue;

    public QnA(String question, String answer, int answerValue) {
        this.question = question;
        this.answer = answer;
        this.answerValue = answerValue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }
}
