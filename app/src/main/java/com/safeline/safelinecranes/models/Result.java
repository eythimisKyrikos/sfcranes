package com.safeline.safelinecranes.models;

public class Result {
    private int id;
    private int inspectionId;
    private Integer positionId;
    private int questionId;
    private Integer answerId;
    private Integer ropeId;
    private boolean isGlobal;

    public Result() { }

    public Result(int id, int inspectionId, Integer positionId, int questionId, Integer answerId, Integer ropeId, boolean isGlobal) {
        this.id = id;
        this.inspectionId = inspectionId;
        this.positionId = positionId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.ropeId = ropeId;
        this.isGlobal = isGlobal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(int inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        if(positionId == null || positionId.intValue() == 0) this.positionId = null;
        else  this.positionId = new Integer(positionId.intValue());
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        if(answerId == null || answerId.intValue() == 0) this.answerId = null;
        else  this.answerId = new Integer(answerId.intValue());
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public Integer getRopeId() {
        return ropeId;
    }

    public void setRopeId(Integer ropeId) {
        if(ropeId == null || ropeId.intValue() == 0) this.ropeId = null;
        else  this.ropeId = new Integer(ropeId.intValue());
    }
}
