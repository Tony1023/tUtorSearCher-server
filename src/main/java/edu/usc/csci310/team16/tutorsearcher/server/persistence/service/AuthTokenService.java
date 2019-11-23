package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

public interface AuthTokenService {
    String generateNewToken(long id);
    boolean validateUserToken(long id, String token);
}
