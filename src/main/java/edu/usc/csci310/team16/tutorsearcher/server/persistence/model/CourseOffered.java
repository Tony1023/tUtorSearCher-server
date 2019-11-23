package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CourseOffered")
@NamedQueries(
        @NamedQuery(
                name = "findCourseOfferedByUserId",
                query = "FROM CourseOffered WHERE user_id=:id"
        )
)
public class CourseOffered implements Serializable {

    public CourseOffered() { }

    public CourseOffered(User user, Course course) {
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
