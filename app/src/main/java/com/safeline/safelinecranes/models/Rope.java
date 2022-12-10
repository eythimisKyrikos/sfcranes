package com.safeline.safelinecranes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Rope implements Parcelable {
    private int id;
    private String date;
    private String serialNr;
    private int workHours;
    private int type_id;
    private Integer position_id;
    private boolean sync;
    private byte[] byteImage;
    private boolean hasRetire;
    private String retireDate;
    private String tag;

    public Rope() { }

    public Rope(int id, String date, String serialNr, int workHours, int type_id,
                Integer position_id, boolean sync, byte[] byteImage, boolean hasRetire, String retireDate, String tag){
        this.id = id;
        this.date = date;
        this.serialNr = serialNr;
        this.workHours = workHours;
        this.type_id = type_id;
        this.position_id = position_id;
        this.sync = sync;
        this.byteImage = byteImage;
        this.hasRetire = hasRetire;
        this.retireDate = retireDate;
        this.tag = tag;
    }

    protected Rope(Parcel in) {
        id = in.readInt();
        date = in.readString();
        serialNr = in.readString();
        workHours = in.readInt();
        type_id = in.readInt();
        if (in.readByte() == 0) {
            position_id = null;
        } else {
            position_id = in.readInt();
        }
        sync = in.readByte() != 0;
        byteImage = in.createByteArray();
        hasRetire = in.readByte() != 0;
        retireDate = in.readString();
        tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(serialNr);
        dest.writeInt(workHours);
        dest.writeInt(type_id);
        if (position_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(position_id);
        }
        dest.writeByte((byte) (sync ? 1 : 0));
        dest.writeByteArray(byteImage);
        dest.writeByte((byte) (hasRetire ? 1 : 0));
        dest.writeString(retireDate);
        dest.writeString(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rope> CREATOR = new Creator<Rope>() {
        @Override
        public Rope createFromParcel(Parcel in) {
            return new Rope(in);
        }

        @Override
        public Rope[] newArray(int size) {
            return new Rope[size];
        }
    };

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSerialNr() {
        return serialNr;
    }

    public void setSerialNr(String serialNr) {
        this.serialNr = serialNr;
    }

    public int getWorkHours() {
        return workHours;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public Integer getPosition_id() {
        return position_id;
    }

    public void setPosition_id(Integer position_id) {
        this.position_id = position_id;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isHasRetire() {
        return hasRetire;
    }

    public void setHasRetire(boolean hasRetire) {
        this.hasRetire = hasRetire;
    }

    public String getRetireDate() {
        return retireDate;
    }

    public void setRetireDate(String retireDate) {
        this.retireDate = retireDate;
    }

    @Override
    public String toString() {
        return this.serialNr;
    }

}
