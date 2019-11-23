package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Rating;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

public interface RatingDAO {
    Rating findRating(User tutee, User tutor);
    void updateRating(Rating rating, double value);
    void addRating(User tutee, User tutor, double value);
}
