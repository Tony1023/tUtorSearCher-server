package edu.usc.csci310.team16.tutorsearcher.server.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDAO {

    @Autowired
    protected SessionFactory sessionFactory;
}
