package com.safeline.safelinecranes.models;

public class SelectPosition {

    private Position position;
    private boolean isSelected;

    public SelectPosition() { }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
