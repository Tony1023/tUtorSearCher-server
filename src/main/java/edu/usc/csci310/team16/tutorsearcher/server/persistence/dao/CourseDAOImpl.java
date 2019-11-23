package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;
import org.springframework.stereotype.Repository;

@Repository("CourseDAO")
public class CourseDAOImpl extends AbstractDAO implements CourseDAO {

    @Override
    public Course findByCourseNumber(String courseNumber) {
        return em.createNamedQuery("findCourseByCourseNumber", Course.class)
                .setParameter("courseNumber", courseNumber)
                .getSingleResult();
    }
}
