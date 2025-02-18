package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Availability;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    long addUser(User user);
    User findById(long id);
    User findByCredentials(String email, String password) throws NoResultException;
    List<User> findTutors(Course course, List<Integer> slots, User searcher);
}
