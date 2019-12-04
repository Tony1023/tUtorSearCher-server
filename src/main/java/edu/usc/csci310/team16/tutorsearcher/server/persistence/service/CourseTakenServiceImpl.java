package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.CourseOfferedDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.CourseTakenDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.CourseTaken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CourseTakenServiceImpl implements CourseTakenService {

    @Autowired
    private CourseTakenDAO courseTakenDAO;

    @Override
    public void updateCoursesTaken(long id, List<String> courses) {
        courseTakenDAO.removeCoursesForUser(id);
        courseTakenDAO.addCoursesForUser(id, courses);
    }
}
