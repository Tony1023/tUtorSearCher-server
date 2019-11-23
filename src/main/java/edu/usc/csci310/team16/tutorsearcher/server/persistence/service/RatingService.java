package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

public interface RatingService {

    Double getRating(long tuteeId, long tutorId);
    void updateRating(long tuteeId, long tutorId, double rating);
}
