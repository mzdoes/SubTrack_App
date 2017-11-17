package com.example.mzdoes.subtrack;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by per6 on 11/17/17.
 */

public class Subscription implements Parcelable {

    //instance variables
    private String name;
    private String desc;
    private double price;
    //private int nextMonth, nextDay, ogMonth, ogDay;

    //constructor
    public Subscription(String name, String desc, double price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
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

    public void setName(String name) {
        this.name = name;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setPrice(double price) {
        this.price = price;
    }


    protected Subscription(Parcel in) {
        name = in.readString();
        desc = in.readString();
        price = in.readDouble();
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
