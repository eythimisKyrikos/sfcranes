package com.safeline.safelinecranes.models;

public class DeletedObject {
    private int id;
    private String type;
    private int key;

    public DeletedObject() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
