package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import java.util.List;

public interface CourseTakenDAO {
    void removeCoursesForUser(long id);
    void addCoursesForUser(long id, List<String> courses);
}
