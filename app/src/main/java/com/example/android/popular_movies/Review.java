package com.example.android.popular_movies;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable{
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

        public Review createFromParcel(Parcel in) {

            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    private String id;
    private String author;
    private String content;

    public Review() {

    }

    // parcel construtor
    protected Review(Parcel p) {
        id = p.readString();
       author = p.readString();
        content = p.readString();



    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    @Override
    public String toString() {
        return "Author: " + getAuthor() +
                "\n" + getContent();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);


    }


}
