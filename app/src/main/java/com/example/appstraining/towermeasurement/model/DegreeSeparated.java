package com.example.appstraining.towermeasurement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DegreeSeparated implements Parcelable {
    private int degreeRight, degreeLeft;
    private int minuteRight, minuteLeft;
    private int secondRight, secondLeft;

    public DegreeSeparated(int degreeLeft, int minuteLeft, int secondLeft,
                           int degreeRight, int minuteRight, int secondRight) {
        this.degreeLeft = degreeLeft;
        this.minuteLeft = minuteLeft;
        this.secondLeft = secondLeft;
        this.degreeRight = degreeRight;
        this.minuteRight = minuteRight;
        this.secondRight = secondRight;
    }

    public DegreeSeparated(int[] leftAngle, int[] rightAngle) {
        this.degreeLeft = leftAngle[0];
        this.minuteLeft = leftAngle[1];
        this.secondLeft = leftAngle[2];
        this.degreeRight = rightAngle[0];
        this.minuteRight = rightAngle[1];
        this.secondRight = rightAngle[2];
    }

    public int getDegreeRight() {
        return degreeRight;
    }

    public void setDegreeRight(int degreeRight) {
        this.degreeRight = degreeRight;
    }

    public int getDegreeLeft() {
        return degreeLeft;
    }

    public void setDegreeLeft(int degreeLeft) {
        this.degreeLeft = degreeLeft;
    }

    public int getMinuteRight() {
        return minuteRight;
    }

    public void setMinuteRight(int minuteRight) {
        this.minuteRight = minuteRight;
    }

    public int getMinuteLeft() {
        return minuteLeft;
    }

    public void setMinuteLeft(int minuteLeft) {
        this.minuteLeft = minuteLeft;
    }

    public int getSecondRight() {
        return secondRight;
    }

    public void setSecondRight(int secondRight) {
        this.secondRight = secondRight;
    }

    public int getSecondLeft() {
        return secondLeft;
    }

    public void setSecondLeft(int secondLeft) {
        this.secondLeft = secondLeft;
    }

    protected DegreeSeparated(Parcel in) {
        degreeLeft = in.readInt();
        minuteLeft = in.readInt();
        secondLeft = in.readInt();
        degreeRight = in.readInt();
        minuteRight = in.readInt();
        secondRight = in.readInt();
    }

    public static final Creator<DegreeSeparated> CREATOR = new Creator<DegreeSeparated>() {
        @Override
        public DegreeSeparated createFromParcel(Parcel in) {
            return new DegreeSeparated(in);
        }

        @Override
        public DegreeSeparated[] newArray(int size) {
            return new DegreeSeparated[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(degreeLeft);
        dest.writeInt(minuteLeft);
        dest.writeInt(secondLeft);
        dest.writeInt(degreeRight);
        dest.writeInt(minuteRight);
        dest.writeInt(secondRight);
    }
}
