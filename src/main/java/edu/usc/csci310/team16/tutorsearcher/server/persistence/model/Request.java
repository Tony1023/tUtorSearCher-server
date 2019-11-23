package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Requests")
@NamedQueries({
        @NamedQuery(
                name = "findRequestByPeopleAndCourse",
                query = "FROM Request WHERE tutee_id=:tuteeId AND tutor_id=:tutorId AND course_id=:id"
        ),
        @NamedQuery(
                name = "findTutorsByTutee",
                query = "FROM Request WHERE tutee_id=:tuteeId AND req_status=1"
        )
})
public class Request {

    public Request() { }

    public Request(User tutor, User tutee, Course course) {
        this.tutor = tutor;
        this.tutee = tutee;
        this.course = course;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @ManyToOne
    @JoinColumn(name = "tutee_id")
    private User tutee;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "min_hours")
    private int minHours;

    @Column(name = "req_status")
    private int status;

    @OneToMany
    @JoinColumn(name = "req_id")
    private List<RequestOverlap> overlap;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getTutor() {
        return tutor;
    }

    public User getTutee() {
        return tutee;
    }

    public Course getCourse() {
        return course;
    }

    public List<RequestOverlap> getOverlap() {
        return overlap;
    }

    public long getId() {
        return id;
    }
}
