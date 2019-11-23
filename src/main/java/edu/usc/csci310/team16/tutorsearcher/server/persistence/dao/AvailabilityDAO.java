package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import java.util.List;

public interface AvailabilityDAO {
    void removeSlotsForUser(long id);
    void addSlotsForUser(long id, List<Integer> slots);
}
