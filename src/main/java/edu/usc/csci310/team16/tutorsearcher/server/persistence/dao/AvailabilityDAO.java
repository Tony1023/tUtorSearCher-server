package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import java.util.List;

public interface AvailabilityDAO {
    void removeSlotsForUser(long id);
    void removeSlots(User user, List<Integer> slots);
    void addSlotsForUser(long id, List<Integer> slots);
}
