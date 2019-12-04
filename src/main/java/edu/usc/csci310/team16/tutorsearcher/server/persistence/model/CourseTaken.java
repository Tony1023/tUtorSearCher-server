package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UserCourses")
@NamedQueries(
        @NamedQuery(
                name = "findCourseTakenByUserId",
                query = "FROM CourseTaken WHERE user_id=:id"
        )
)
public class CourseTaken implements Serializable {

    public CourseTaken() { }

    public CourseTaken(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Course getCourse() {
        return course;
    }
}
