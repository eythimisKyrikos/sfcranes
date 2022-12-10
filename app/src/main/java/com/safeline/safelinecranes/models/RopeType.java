package com.safeline.safelinecranes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RopeType implements Parcelable {
    private int id;
    private String type;
    private String manufacturer;
    private String model;
    private float diameter;
    private float length;
    private String material;
    private float breakingForce;
    private boolean sync;

    public RopeType() {}

    public RopeType(int id, String type, String manufacturer, String model, float diameter, float length, String material, float breakingForce, boolean sync) {
        this.id = id;
        this.type = type;
        this.manufacturer = manufacturer;
        this.model = model;
        this.diameter = diameter;
        this.length = length;
        this.material = material;
        this.breakingForce = breakingForce;
        this.sync = sync;
    }

    protected RopeType(Parcel in) {
        id = in.readInt();
        type = in.readString();
        manufacturer = in.readString();
        model = in.readString();
        diameter = in.readFloat();
        length = in.readFloat();
        material = in.readString();
        breakingForce = in.readFloat();
        sync = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(manufacturer);
        dest.writeString(model);
        dest.writeFloat(diameter);
        dest.writeFloat(length);
        dest.writeString(material);
        dest.writeFloat(breakingForce);
        dest.writeByte((byte) (sync ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RopeType> CREATOR = new Creator<RopeType>() {
        @Override
        public RopeType createFromParcel(Parcel in) {
            return new RopeType(in);
        }

        @Override
        public RopeType[] newArray(int size) {
            return new RopeType[size];
        }
    };

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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public float getBreakingForce() {
        return breakingForce;
    }

    public void setBreakingForce(float breakingForce) {
        this.breakingForce = breakingForce;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @Override
    public String toString() {
        return this.type + " " + this.model;
    }
}
