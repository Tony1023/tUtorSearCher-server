package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.AuthTokenDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.AuthToken;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthTokenServiceImpl implements AuthTokenService {

    @Autowired
    private AuthTokenDAO authTokenDAO;

    @Override
    public String generateNewToken(long id) {
        String token = UUID.randomUUID().toString();
        authTokenDAO.addToken(id, token);
        return token;
    }

    @Override
    public boolean validateUserToken(long id, String token) {
        try {
            authTokenDAO.getToken(id, token);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
