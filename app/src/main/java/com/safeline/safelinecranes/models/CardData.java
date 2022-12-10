package com.safeline.safelinecranes.models;

public class CardData {
    public int gaugeLvl;
    public String action;
    public String header;

    public CardData() { }

    public CardData(int gaugeLvl, String action, String header) {
        this.gaugeLvl = gaugeLvl;
        this.action = action;
        this.header = header;
    }

    public int getGaugeLvl() {
        return gaugeLvl;
    }

    public void setGaugeLvl(int gaugeLvl) {
        this.gaugeLvl = gaugeLvl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}