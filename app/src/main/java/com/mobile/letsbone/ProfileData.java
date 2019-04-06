package com.mobile.letsbone;

public class ProfileData {
    private String userId;
    private String fName;
    private String lName;
    private String gender;
    private String age;
    private String lookingFor;
    private String imageUrl;

    public ProfileData(String userId, String fName, String lName, String gender, String age, String lookingFor, String imageUrl) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.age = age;
        this.lookingFor = lookingFor;
        this.imageUrl = imageUrl;
    }

    //Getters
    public String getUserId() {return userId;}

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

    public String getLookingFor() {
        return lookingFor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    //Setters
    public void setUserId(String userId) {this.userId = userId;}

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

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
