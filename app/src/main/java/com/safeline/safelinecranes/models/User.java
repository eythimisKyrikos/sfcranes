package com.safeline.safelinecranes.models;
import java.util.Date;

public class User {
//    @SerializedName("id")
    private int id;
//    @SerializedName("username")
    private String username;
//    @SerializedName("vessel")
    private String vessel;
//    @SerializedName("date")
    private Date datetime;
//    @SerializedName("password")
    private String password;
//    @SerializedName("license")
    private String license;
//    @SerializedName("device")
    private String device;

    public User() { }

    public User(int id, String username, String license, String vessel, Date datetime, String device, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.license = license;
        this.vessel = vessel;
        this.datetime = datetime;
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}

