package edu.usc.csci310.team16.tutorsearcher.server.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.UserProfile;

public interface UserProfileDAO {
    void saveUser(UserProfile profile);
    UserProfile findById(long id);
}
