package com.safeline.safelinecranes.models;

public class GlobalAction {
    public int severity_level;
    public String action;

    public GlobalAction() {}

    public GlobalAction(int severity_level, String action) {
        this.severity_level = severity_level;
        this.action = action;
    }

    public int getSeverity_level() {
        return severity_level;
    }

    public void setSeverity_level(int severity_level) {
        this.severity_level = severity_level;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
