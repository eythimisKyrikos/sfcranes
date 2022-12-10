package com.safeline.safelinecranes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable {
    private int id;
    private int rope_id;
    private String ropeSerial;
    private String typeOfChange;
    private boolean sync;
    private int pos_id;
    private String positionName;
    private String previousPositionName;
    private int workHours_in_position;
    private boolean retired;
    private int workHours_until_retire;
    private String date;

    public History() { }

    public History(int id, int rope_id, int pos_id, String positionName, String date, String ropeSerial,
                   int workHours_in_position, String typeOfChange, boolean sync, boolean retired, int workHours_until_retire, String previousPositionName) {
        this.id = id;
        this.rope_id = rope_id;
        this.pos_id = pos_id;
        this.positionName = positionName;
        this.date = date;
        this.ropeSerial = ropeSerial;
        this.workHours_in_position = workHours_in_position;
        this.typeOfChange = typeOfChange;
        this.sync = sync;
        this.retired = retired;
        this.workHours_until_retire = workHours_until_retire;
        this.previousPositionName = previousPositionName;
    }

    protected History(Parcel in) {
        id = in.readInt();
        rope_id = in.readInt();
        ropeSerial = in.readString();
        typeOfChange = in.readString();
        sync = in.readByte() != 0;
        pos_id = in.readInt();
        positionName = in.readString();
        previousPositionName = in.readString();
        workHours_in_position = in.readInt();
        retired = in.readByte() != 0;
        workHours_until_retire = in.readInt();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(rope_id);
        dest.writeString(ropeSerial);
        dest.writeString(typeOfChange);
        dest.writeByte((byte) (sync ? 1 : 0));
        dest.writeInt(pos_id);
        dest.writeString(positionName);
        dest.writeString(previousPositionName);
        dest.writeInt(workHours_in_position);
        dest.writeByte((byte) (retired ? 1 : 0));
        dest.writeInt(workHours_until_retire);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public String getPreviousPositionName() {
        return previousPositionName;
    }

    public void setPreviousPositionName(String previousPositionName) {
        this.previousPositionName = previousPositionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRope_id() {
        return rope_id;
    }

    public void setRope_id(int rope_id) {
        this.rope_id = rope_id;
    }

    public int getPos_id() {
        return pos_id;
    }

    public void setPos_id(int pos_id) {
        this.pos_id = pos_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRopeSerial() {
        return ropeSerial;
    }

    public void setRopeSerial(String ropeSerial) {
        this.ropeSerial = ropeSerial;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getWorkHours_in_position() {
        return workHours_in_position;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public void setWorkHours_in_position(int workHours_in_position) {
        this.workHours_in_position = workHours_in_position;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public int getWorkHours_until_retire() {
        return workHours_until_retire;
    }

    public void setWorkHours_until_retire(int workHours_until_retire) {
        this.workHours_until_retire = workHours_until_retire;
    }
    public String getTypeOfChange() {
        return typeOfChange;
    }

    public void setTypeOfChange(String typeOfChange) {
        this.typeOfChange = typeOfChange;
    }

}

