package com.mobile.letsbone;

public class ProfileData {

    private String fName;
    private String lName;
    private String gender;
    private String age;
    private String location;
    private int numLikes;
    private int mPhoto;
    private String lookingFor;

    public ProfileData(String fName, String lName, String gender, String age, String location, int numLikes, int mPhoto, String lookingFor) {
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.numLikes = numLikes;
        this.mPhoto = mPhoto;
        this.lookingFor = lookingFor;
    }

    //Getters

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public String getLookingFor() {
        return lookingFor;
    }


    //Setters

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }



    public int getmPhoto() {
        return mPhoto;
    }



    public void setmPhoto(int mPhoto) { this.mPhoto = mPhoto; }


}
