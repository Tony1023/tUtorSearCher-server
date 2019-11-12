package edu.usc.csci310.team16.tutorsearcher.server.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.UserProfile;

public interface UserService {

    UserProfile findUserById(long id);
}
