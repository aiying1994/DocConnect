package com.example.docconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Premise implements Parcelable {
    private  String name, description, location, premiseId;
    private  Long rating, ratingTimes;

    public Premise() {
    }

    protected Premise(Parcel in) {
        name = in.readString();
        description = in.readString();
        location = in.readString();
        premiseId = in.readString();
        rating = in.readLong();
        ratingTimes = in.readLong();
    }

    public static final Creator<Premise> CREATOR = new Creator<Premise>() {
        @Override
        public Premise createFromParcel(Parcel in) {
            return new Premise(in);
        }

        @Override
        public Premise[] newArray(int size) {
            return new Premise[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPremiseId() {
        return premiseId;
    }

    public void setPremiseId(String premiseId) {
        this.premiseId = premiseId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(premiseId);
        dest.writeLong(rating);
        dest.writeLong(ratingTimes);
    }
}
