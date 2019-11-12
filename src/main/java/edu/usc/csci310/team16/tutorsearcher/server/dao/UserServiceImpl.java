package edu.usc.csci310.team16.tutorsearcher.server.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserProfileDAO userProfileDAO;

    @Autowired
    UserServiceImpl(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }

    @Override
    public UserProfile findUserById(long id) {
        return userProfileDAO.findById(id);
    }
}
