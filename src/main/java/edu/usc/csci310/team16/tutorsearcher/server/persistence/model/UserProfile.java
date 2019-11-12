package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@NamedQueries({
        @NamedQuery(
                name = "getUserById",
                query = "from UserProfile where id=:id"
        )
})
public class UserProfile {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "grade")
    private String grade;

    @Column(name = "email")
    private String email;

    @Column(name = "bio")
    private String bio;
    //possible availabilities, corresponding to "when is good" blocks
//    private List<Integer> availability = new ArrayList<>();
//    //just for tutors
//    private List<String> coursesTaken = new ArrayList<>(); //tutor
//    private List<String> tutorClasses = new ArrayList<>(); //tutor (which can they teach)
//    private Double rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
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
//
//    public List<Integer> getAvailability() {
//        return availability;
//    }
//
//    public List<String> getCoursesTaken() {
//        return coursesTaken;
//    }
//
//    public List<String> getTutorClasses() {
//        return tutorClasses;
//    }
//
//    public Double getRating() {
//        return rating;
//    }
//
    public void setId(long id) {
        this.id = id;
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
//
//    public void setAvailability(List<Integer> availability) {
//        this.availability = availability;
//    }
//
//    public void setCoursesTaken(List<String> coursesTaken) {
//        this.coursesTaken = coursesTaken;
//    }
//
//    public void setTutorClasses(List<String> tutorClasses) {
//        this.tutorClasses = tutorClasses;
//    }
//
//    public void setRating(Double rating) {
//        this.rating = rating;
//    }
}
