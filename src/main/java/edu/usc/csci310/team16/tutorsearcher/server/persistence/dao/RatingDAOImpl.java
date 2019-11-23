package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Rating;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository("RatingDAO")
public class RatingDAOImpl extends AbstractDAO implements RatingDAO {

    @Override
    public Rating findRating(User tutee, User tutor) throws NoResultException {
        return em.createNamedQuery("findRatingByPeople", Rating.class)
                .setParameter("tutorId", tutor.getId())
                .setParameter("tuteeId", tutee.getId())
                .getSingleResult();
    }

    @Override
    public void updateRating(Rating rating, double value) {
        rating.setRating(value);
        em.merge(rating);
    }

    @Override
    public void addRating(User tutee, User tutor, double value) {
        em.persist(new Rating(tutor, tutee, value));
    }


}
