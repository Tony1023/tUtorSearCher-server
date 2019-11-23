package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.AuthToken;

public interface AuthTokenDAO {
    void addToken(long id, String token);
    AuthToken getToken(long id, String token);
}
