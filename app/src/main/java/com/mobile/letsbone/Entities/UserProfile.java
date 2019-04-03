package com.mobile.letsbone.Entities;

public class UserProfile {

    private String userId;

    private Long Age;

    private String DogAge;

    private String DogBreed;

    private String DogGender;

    private String DogName;

    private String EmailAddress;

    private String FirstName;

    private String Gender;

    private String LastName;

    private Long Likes;

    private String LookingFor;

    private String Matches;

    private Long Status;

    private int profileImage;

    //Constructor
    public UserProfile(String userId, String FirstName, String LastName, int profileImage) {
        this.userId = userId;
        this.FirstName = FirstName;
        this.profileImage = profileImage;
        this.LastName = LastName;
    }

    //Getters and Setters

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Long getAge() {
        return Age;

    }

    public void setAge(Long age) {
        Age = age;
    }

    public String getDogAge() {
        return DogAge;
    }

    public void setDogAge(String dogAge) {
        DogAge = dogAge;
    }

    public void setStatus(Long status) {
        Status = status;
    }

    public Long getStatus() {
        return Status;
    }

    public String getDogBreed() {
        return DogBreed;
    }

    public void setDogBreed(String dogBreed) {
        DogBreed = dogBreed;
    }

    public String getDogGender() {
        return DogGender;
    }

    public void setDogGender(String dogGender) {
        DogGender = dogGender;
    }

    public String getDogName() {
        return DogName;
    }

    public void setDogName(String dogName) {
        DogName = dogName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public Long getLikes() {
        return Likes;
    }

    public void setLikes(Long likes) {
        Likes = likes;
    }

    public String getLookingFor() {
        return LookingFor;
    }

    public void setLookingFor(String lookingFor) {
        LookingFor = lookingFor;
    }

    public String getMatches() {
        return Matches;
    }

    public void setMatches(String matches) {
        Matches = matches;
    }


}
