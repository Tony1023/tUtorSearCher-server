package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import java.util.List;

public interface CourseTakenService {
    void updateCoursesTaken(long id, List<String> courses);
}
