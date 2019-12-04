package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.CourseOffered;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.CourseTaken;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CourseTakenDAO")
public class CourseTakenDAOImpl extends AbstractDAO implements CourseTakenDAO {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CourseDAO courseDAO;

    @Override
    public void removeCoursesForUser(long id) {
        List<CourseTaken> courses = em.createNamedQuery("findCourseTakenByUserId", CourseTaken.class)
                .setParameter("id", id)
                .getResultList();
        for (CourseTaken course : courses) {
            em.remove(course);
        }
    }

    @Override
    public void addCoursesForUser(long id, List<String> courses) {
        User user = userDAO.findById(id);
        for (String courseNumber : courses) {
            Course course = courseDAO.findByCourseNumber(courseNumber);
            em.persist(new CourseTaken(user, course));
        }
    }
}
