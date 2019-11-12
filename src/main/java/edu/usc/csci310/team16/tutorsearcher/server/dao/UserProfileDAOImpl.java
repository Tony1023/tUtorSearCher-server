package edu.usc.csci310.team16.tutorsearcher.server.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.UserProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;

@Repository("UserProfileDAO")
public class UserProfileDAOImpl extends AbstractDAO implements UserProfileDAO {

    @Override
    public void saveUser(UserProfile profile) {

    }

    @Override
    public UserProfile findById(long id) {
        UserProfile user = sessionFactory.openSession().createNamedQuery("getUserById", UserProfile.class)
                .setParameter("id", id)
                .getSingleResult();
        return user;
    }
}
