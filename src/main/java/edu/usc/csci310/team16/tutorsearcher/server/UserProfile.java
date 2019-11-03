package edu.usc.csci310.team16.tutorsearcher.server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private Integer id;
    private String picture_url;
    private String name;
    private String grade;
    private String email;
    private String bio;
    //possible availabilities, corresponding to "when is good" blocks
    private List<Integer> availability = new ArrayList<>();
    //just for tutors
    private List<String> coursesTaken = new ArrayList<>(); //tutor
    private List<String> tutorClasses = new ArrayList<>(); //tutor (which can they teach)
    private Double rating;

    private static UserProfile currentUser;

    public static void setCurrentUser(UserProfile profile) {
        currentUser = profile;
    }

    public static UserProfile getCurrentUser() {
        return currentUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getGrade() {
        return grade;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public List<Integer> getAvailability() {
        return availability;
    }

    public List<String> getCoursesTaken() {
        return coursesTaken;
    }

    public List<String> getTutorClasses() {
        return tutorClasses;
    }

    public Double getRating() {
        return rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAvailability(List<Integer> availability) {
        this.availability = availability;
    }

    public void setCoursesTaken(List<String> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }

    public void setTutorClasses(List<String> tutorClasses) {
        this.tutorClasses = tutorClasses;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
