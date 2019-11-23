package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter.UserProfile;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import java.util.List;

public interface UserService {

    User findUserById(long id);
    User findUserByCredentials(String email, String password);
    void saveUser(UserProfile profile);
    long addUser(String email, String password);
    List<User> searchTutors(String courseNumber, List<Integer> slots);
    List<User> getTutors(long id);
}
