package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import java.util.List;

public interface CourseOfferedService {
    void updateCourseOffered(long id, List<String> courses);
}
