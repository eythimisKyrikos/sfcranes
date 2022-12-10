package com.safeline.safelinecranes.models;

public class OutcomeCodes {
    private int id;
    private int severity;
    private String code_id;
    private String friendlyText;
    private String type;

    public OutcomeCodes() { }

    public OutcomeCodes(int id, int severity, String code_id, String friendlyText, String type) {
        this.id = id;
        this.severity = severity;
        this.code_id = code_id;
        this.friendlyText = friendlyText;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getFriendlyText() {
        return friendlyText;
    }

    public void setFriendlyText(String friendlyText) {
        this.friendlyText = friendlyText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

