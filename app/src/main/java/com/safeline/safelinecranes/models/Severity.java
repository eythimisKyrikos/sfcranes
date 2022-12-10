package com.safeline.safelinecranes.models;

public class Severity {
    public int id;
    public int severity_level;
    public String code;
    public String friendly_text;
    public String type;

    public Severity() { }

    public Severity(int id, int severity_level, String code, String friendly_text, String type) {
        this.id = id;
        this.severity_level = severity_level;
        this.code = code;
        this.friendly_text = friendly_text;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeverity_level() {
        return severity_level;
    }

    public void setSeverity_level(int severity_level) {
        this.severity_level = severity_level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFriendly_text() {
        return friendly_text;
    }

    public void setFriendly_text(String friendly_text) {
        this.friendly_text = friendly_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
