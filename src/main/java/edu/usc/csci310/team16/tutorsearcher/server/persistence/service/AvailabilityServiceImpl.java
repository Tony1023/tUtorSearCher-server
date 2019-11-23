package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.AvailabilityDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private AvailabilityDAO availabilityDAO;

    @Override
    public void updateAvailability(long id, List<Integer> slots) {
        availabilityDAO.removeSlotsForUser(id);
        availabilityDAO.addSlotsForUser(id, slots);
    }
}
