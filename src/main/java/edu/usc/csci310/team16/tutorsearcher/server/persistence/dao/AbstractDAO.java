package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractDAO {

    @PersistenceContext
    protected EntityManager em;
}
