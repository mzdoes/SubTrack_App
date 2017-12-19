package com.example.mzdoes.subtrack;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by per6 on 11/17/17.
 */

public class Subscription implements Parcelable, Serializable {

    //instance variables
    private String name;
    private String desc;
    private double price;
    private int nextMonth;
    private int nextDay;


    //constructor
    public Subscription(String name, String desc, double price, int nextMonth, int nextDay) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.nextMonth = nextMonth;
        this.nextDay = nextDay;
    }


    //getters and setters
    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public double getPrice() {
        return price;
    }
    public int getNextMonth() {
        return nextMonth;
    }
    public int getNextDay() {
        return nextDay;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setNextMonth(int nextMonth) {
        this.nextMonth = nextMonth;
    }
    public void setNextDay(int nextDay) {
        this.nextDay = nextDay;
    }


    public void setDueDate() {
        nextMonth++;
        if (nextMonth > 12) {nextMonth = 1;}
    }

    protected Subscription(Parcel in) {
        name = in.readString();
        desc = in.readString();
        price = in.readDouble();
        nextMonth = in.readInt();
        nextDay = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeDouble(price);
        dest.writeInt(nextMonth);
        dest.writeInt(nextDay);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Subscription> CREATOR = new Parcelable.Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };
}
