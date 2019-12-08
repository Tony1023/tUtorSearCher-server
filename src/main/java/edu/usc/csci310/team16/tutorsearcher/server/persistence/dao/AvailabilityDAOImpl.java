package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Availability;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AvailabilityDAO")
public class AvailabilityDAOImpl extends AbstractDAO implements AvailabilityDAO {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void removeSlotsForUser(long id) {
        List<Availability> slots = em.createNamedQuery("getAvailabilitiesById", Availability.class)
                .setParameter("id", id)
                .getResultList();
        for (Availability slot: slots) {
            em.remove(slot);
        }
    }

    @Override
    public void removeSlots(User user, List<Integer> slots) {
        for (Integer slot: slots) {
            List<Availability> availability = em.createNamedQuery("getAvailabilityByUserAndSlot", Availability.class)
                    .setParameter("id", user.getId())
                    .setParameter("slot", slot)
                    .getResultList();
            if (!availability.isEmpty()) {
                em.remove(availability.get(0));
            }
        }
    }

    @Override
    public void addSlotsForUser(long id, List<Integer> slots) {
        User user = userDAO.findById(id);
        for (Integer slot: slots) {
            em.persist(new Availability(user, slot));
        }
    }
}
