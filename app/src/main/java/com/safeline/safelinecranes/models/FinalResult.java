package com.safeline.safelinecranes.models;

import java.util.List;

public class FinalResult {
    public Inspection inspection;
    public Rope rope;
    public List<Action> actions;

    public FinalResult() { }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Rope getRope() {
        return rope;
    }

    public void setRope(Rope rope) {
        this.rope = rope;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public class Action {
        public int severity_level;
        public String action;

        public Action() {}

        public Action(int severity_level, String action) {
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
}
