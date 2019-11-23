package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;

public interface CourseDAO {
    Course findByCourseNumber(String courseNumber);
}
