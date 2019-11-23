package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "Courses")
@NamedQueries(
        @NamedQuery(
                name = "findCourseByCourseNumber",
                query = "FROM Course WHERE course_number=:courseNumber"
        )
)
public class Course {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "course_number")
    @NonNull
    private String courseNumber;

    public String getCourseNumber() {
        return courseNumber;
    }

    public long getId() {
        return id;
    }
}
