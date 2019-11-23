package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import java.util.List;

public interface AvailabilityService {
    void updateAvailability(long id, List<Integer> slots);
}
