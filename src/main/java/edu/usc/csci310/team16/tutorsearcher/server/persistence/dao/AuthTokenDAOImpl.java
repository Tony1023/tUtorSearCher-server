package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.AuthToken;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository("AuthTokenDAO")
public class AuthTokenDAOImpl extends AbstractDAO implements AuthTokenDAO {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void addToken(long id, String token) {
        User user = userDAO.findById(id);
        AuthToken authToken = new AuthToken(user, token);
        em.persist(authToken);
    }

    @Override
    public AuthToken getToken(long id, String token) throws NoResultException {
        return em.createNamedQuery("findEntryByIdAndToken", AuthToken.class)
                .setParameter("id", id)
                .setParameter("token", token)
                .getSingleResult();
    }
}
