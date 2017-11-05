package com.example.android.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;


public class Trailer implements Parcelable{
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {

        public Trailer createFromParcel(Parcel in) {

            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public Trailer() {

    }

    // parcel construtor
    protected Trailer(Parcel p) {
        id = p.readString();
        key = p.readString();
        name = p.readString();
        site = p.readString();
        type = p.readString();


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Trailer: " + getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);

    }


}
