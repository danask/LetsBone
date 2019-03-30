package com.mobile.letsbone;

public class CardData {

    private String mUserKey;
    private String mFirstName;
    private String mLastName;
    private String mAge;
    private String mGender;
    private int mProfileImage;

    public CardData(String mUserKey, String mFirstName, String mLastName, String mAge, String mGender, int mProfileImage) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mAge = mAge;
        this.mGender = mGender;
        this.mProfileImage = mProfileImage;
    }

    //Getters
    public String getmUserKey() {
        return mUserKey;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmAge() {
        return mAge;
    }

    public String getmGender() {
        return mGender;
    }

    public int getmProfileImage() {
        return mProfileImage;
    }

    //Setters
    public void setmUserKey(String mUserKey) {
        this.mUserKey = mUserKey;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public void setmProfileImage(int mProfileImage) {
        this.mProfileImage = mProfileImage;
    }
}
