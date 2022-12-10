package com.safeline.safelinecranes.models;

public class Question {
    private int id;
    private String header;
    private String details;
    private String test;
    private boolean isRoutine;
    private String materialType;
    private String outcome;
    private int order;
    private String operation;
    private String conditionReply;
    private String risk;

    public Question() { }

    public Question(int id, String header, String details, String test, boolean isRoutine, String materialType, String outcome, int order, String operation, String conditionReply, String risk) {
        this.id = id;
        this.header = header;
        this.details = details;
        this.test = test;
        this.isRoutine = isRoutine;
        this.materialType = materialType;
        this.outcome = outcome;
        this.order = order;
        this.operation = operation;
        this.conditionReply = conditionReply;
        this.risk = risk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public boolean isRoutine() {
        return isRoutine;
    }

    public void setRoutine(boolean routine) {
        isRoutine = routine;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getConditionReply() {
        return conditionReply;
    }

    public void setConditionReply(String conditionReply) {
        this.conditionReply = conditionReply;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
