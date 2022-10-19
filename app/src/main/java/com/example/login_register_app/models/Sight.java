package com.example.login_register_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Sight implements Parcelable {

    private String name, city;
    private double longitude, latitude;
    private int sightImg;

    public Sight() {}

    public Sight(String name, String city, double latitude, double longitude, int sightImg) {
        this.name = name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sightImg = sightImg;
    }

    protected Sight(Parcel in) {
        name = in.readString();
        city = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        sightImg = in.readInt();
    }

    public static final Creator<Sight> CREATOR = new Creator<Sight>() {
        @Override
        public Sight createFromParcel(Parcel in) {
            return new Sight(in);
        }

        @Override
        public Sight[] newArray(int size) {
            return new Sight[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getSightImg() {
        return sightImg;
    }

    public void setSightImg(int sightImg) {
        this.sightImg = sightImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(city);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeInt(sightImg);
    }
}
