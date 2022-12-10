package com.safeline.safelinecranes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Inspection implements Parcelable {
    private int id;
    private boolean isFinished;
    private Integer step;
    private String date_created;
    private String date_finished;
    private int workHours;
    private int position;

    public Inspection() { }

    public Inspection(boolean isFinished, String date_created, String date_finished, int workHours, int position) {

        this.isFinished = isFinished;
        this.date_created = date_created;
        this.date_finished = date_finished;
        this.workHours = workHours;
        this.position = position;
    }

    public Inspection(int id, boolean isFinished, Integer step, String date_created, String date_finished, int workHours, int position) {
        this.id = id;
        this.isFinished = isFinished;
        this.step = step;
        this.date_created = date_created;
        this.date_finished = date_finished;
        this.workHours = workHours;
        this.position = position;
    }

    protected Inspection(Parcel in) {
        id = in.readInt();
        isFinished = in.readByte() != 0;
        if (in.readByte() == 0) {
            step = null;
        } else {
            step = in.readInt();
        }
        date_created = in.readString();
        date_finished = in.readString();
        workHours = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isFinished ? 1 : 0));
        if (step == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(step);
        }
        dest.writeString(date_created);
        dest.writeString(date_finished);
        dest.writeInt(workHours);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Inspection> CREATOR = new Creator<Inspection>() {
        @Override
        public Inspection createFromParcel(Parcel in) {
            return new Inspection(in);
        }

        @Override
        public Inspection[] newArray(int size) {
            return new Inspection[size];
        }
    };

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_finished() {
        return date_finished;
    }

    public void setDate_finished(String date_finished) {
        this.date_finished = date_finished;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public int getWorkHours() {
        return workHours;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int positionId) {
        this.position = positionId;
    }

}
