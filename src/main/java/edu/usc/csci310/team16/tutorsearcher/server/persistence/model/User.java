package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
@NamedQueries(
        @NamedQuery(
                name = "findUserByCredentials",
                query = "FROM User WHERE email=:email AND pass_hash=:password"
        )
)
public class User {

    public User() { }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "grade")
    private String grade;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "pass_hash")
    private String password;

    @Column(name = "bio")
    private String bio;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Availability> availability;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<CourseTaken> coursesTaken; //tutor

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<CourseOffered> tutorClasses; //tutor (which can they teach)

    @Formula("(SELECT avg(ifnull(r.rating, -1)) FROM Ratings r WHERE r.tutor_id = id)")
    private Double rating;

    @OneToMany
    @JoinColumn(name = "tutor_id")
    @Where(clause = "req_status = 1")
    private List<Request> acceptedRequests;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public List<Availability> getAvailability() {
        return availability;
    }

    public List<CourseTaken> getCoursesTaken() {
        return coursesTaken;
    }

    public List<CourseOffered> getTutorClasses() {
        return tutorClasses;
    }

    public Double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Request> getAcceptedRequestsAsTutor() {
        return acceptedRequests;
    }
}
