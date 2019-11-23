package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.CourseOfferedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CourseOfferedServiceImpl implements CourseOfferedService {

    @Autowired
    private CourseOfferedDAO courseOfferedDAO;

    @Override
    public void updateCourseOffered(long id, List<String> courses) {
        courseOfferedDAO.removeCoursesForUser(id);
        courseOfferedDAO.addCoursesForUser(id, courses);
    }
}
