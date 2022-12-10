package com.safeline.safelinecranes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
    private int id;
    private String header;
    private String details;
    private String recommendations;
    private int question_id;
    private int value;
    private String image1;
    private String image2;

    public Answer() { }

    public Answer(int id, String header, String details, String recommendations, int question_id, int value, String image1, String image2) {
        this.id = id;
        this.header = header;
        this.details = details;
        this.recommendations = recommendations;
        this.question_id = question_id;
        this.value = value;
        this.image1 = image1;
        this.image2 = image2;
    }


    protected Answer(Parcel in) {
        id = in.readInt();
        header = in.readString();
        details = in.readString();
        recommendations = in.readString();
        question_id = in.readInt();
        value = in.readInt();
        image1 = in.readString();
        image2 = in.readString();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(header);
        parcel.writeString(details);
        parcel.writeString(recommendations);
        parcel.writeInt(question_id);
        parcel.writeInt(value);
        parcel.writeString(image1);
        parcel.writeString(image2);
    }
}
