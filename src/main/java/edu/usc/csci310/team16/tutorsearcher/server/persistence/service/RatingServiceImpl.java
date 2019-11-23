package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.RatingDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.UserDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Rating;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Service
@Transactional(rollbackFor = Exception.class, noRollbackFor = NoResultException.class)
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingDAO ratingDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public Double getRating(long tuteeId, long tutorId) {
        User tutee = userDAO.findById(tuteeId);
        User tutor = userDAO.findById(tutorId);
        try {
            Rating rating = ratingDAO.findRating(tutee, tutor);
            return rating.getRating();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void updateRating(long tuteeId, long tutorId, double value) {
        User tutee = userDAO.findById(tuteeId);
        User tutor = userDAO.findById(tutorId);
        try {
            Rating rating = ratingDAO.findRating(tutee, tutor);
            ratingDAO.updateRating(rating, value);
        } catch (NoResultException ignore) {
            ratingDAO.addRating(tutee, tutor, value);
        }
    }
}
