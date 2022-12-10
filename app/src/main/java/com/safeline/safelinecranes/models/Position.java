package com.safeline.safelinecranes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Position implements Parcelable {
    private int id;
    private String name;
    private int x;
    private int y;
    private boolean sync;
    private boolean isStorage;
    private String type_of_work;

    public Position() {}

    public Position(int id, String name,int x, int y, boolean sync, boolean isStorage, String typeOfWork) {
        this.id = id;
        this.name = name;
        this.x =x;
        this.y = y;
        this.sync = sync;
        this.isStorage = isStorage;
        this.type_of_work = typeOfWork;
    }

    protected Position(Parcel in) {
        id = in.readInt();
        name = in.readString();
        x = in.readInt();
        y = in.readInt();
        sync = in.readByte() != 0;
        isStorage = in.readByte() != 0;
        type_of_work = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeByte((byte) (sync ? 1 : 0));
        dest.writeByte((byte) (isStorage ? 1 : 0));
        dest.writeString(type_of_work);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Position> CREATOR = new Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    public String getType_of_work() {
        return type_of_work;
    }

    public void setType_of_work(String type_of_work) {
        this.type_of_work = type_of_work;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isStorage() {
        return isStorage;
    }

    public void setStorage(boolean storage) {
        isStorage = storage;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
