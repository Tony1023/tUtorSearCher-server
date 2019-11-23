package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import java.util.List;

public interface RequestDAO {
    Request findById(long id);
    List<Request> findByPeopleAndCourse(User tutee, User tutor, Course course);
    List<Request> findByTuteeAndCourse(User tutee, Course course);
    List<Request> findByTuteeAndCourse(User tutee, Course course, int status);
    List<Request> findByTutee(User tutee);
    void updateRequestStatus(Request request, int status);
    Request addRequest(User tuteeId, User tutorId, Course course);
}
