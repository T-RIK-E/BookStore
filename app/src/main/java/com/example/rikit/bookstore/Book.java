package com.example.rikit.bookstore;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{

    private String author;
    private String title ;
    private int imgID;
    private int publishYear;
    private String description;
    private double priceSoft;
    private double priceHard;
    private boolean checkedOut;
    private String url;
    private boolean hardCover;


    //Src code for parcelable: https://en.proft.me/2017/02/28/pass-object-between-activities-android-parcelable/
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(author);
        dest.writeString(title);
        dest.writeInt(imgID);
        dest.writeInt(publishYear);
        dest.writeString(description);
        dest.writeDouble(priceSoft);
        dest.writeDouble(priceHard);
        dest.writeByte((byte) (checkedOut ? 1 : 0));
        dest.writeString(url);
        dest.writeByte((byte) (hardCover ? 1 : 0));
    }
    private Book(Parcel in){
        this.author = in.readString();
        this.title = in.readString();
        this.imgID = in.readInt();
        this.publishYear = in.readInt();
        this.description = in.readString();
        this.priceSoft = in.readDouble();
        this.priceHard = in.readDouble();
        this.checkedOut = in.readByte() != 0;
        this.url = in.readString();
        this.hardCover = in.readByte() != 0;
    }
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book(String title, String author, int imgID, int publishYear, String  description, double priceSoft, double priceHard, String url){
        this.author = author;
        this.title = title;
        this.imgID = imgID;
        this.publishYear = publishYear;
        this.description = description;
        this.priceSoft = priceSoft;
        this.priceHard = priceHard;
        this.checkedOut = false;
        this.url = url;
        this.hardCover = true;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getImgID() {
        return imgID;
    }

    public int getPublishYear(){return publishYear;}

    public String getDescription() { return description; }

    public double getPriceSoft() { return priceSoft; }

    public double getPriceHard(){return priceHard;}

    public boolean isCheckedOut() { return checkedOut; }
    public void setCheckedOut(boolean b){this.checkedOut = b;}

    public String getUrl() {
        return url;
    }

    public boolean isHardCover() {
        return hardCover;
    }

    public void setHardCover(boolean hardCover) {
        this.hardCover = hardCover;
    }
}

