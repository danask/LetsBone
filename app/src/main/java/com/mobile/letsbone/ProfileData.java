package com.mobile.letsbone;

public class ProfileData {

    private String mName;
    private String mAge;
    private String mLocation;
    private int mPhoto;

    public ProfileData(String mName, String mAge, String mLocation, int mPhoto) {
        this.mName = mName;
        this.mPhoto = mPhoto;
        this.mAge = mAge;
        this.mLocation = mLocation;
    }

    //Getters

    public String getmName() {
        return mName;
    }

    public String getmAge() {
        return mAge;
    }

    public String getmLocation() {
        return mLocation;
    }

    public int getmPhoto() {
        return mPhoto;
    }

    //Setters

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }


    public void setmPhoto(int mPhoto) { this.mPhoto = mPhoto; }


}
