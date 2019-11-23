package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Ratings")
@NamedQueries(
        @NamedQuery(
                name = "findRatingByPeople",
                query = "FROM Rating WHERE tutor_id=:tutorId AND tutee_id=:tuteeId"
        )
)
public class Rating implements Serializable {

    public Rating() { }

    public Rating(User tutor, User tutee, double rating) {
        this.tutor = tutor;
        this.tutee = tutee;
        this.rating = rating;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @Id
    @ManyToOne
    @JoinColumn(name = "tutee_id")
    private User tutee;

    @Column(name = "rating")
    private double rating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
