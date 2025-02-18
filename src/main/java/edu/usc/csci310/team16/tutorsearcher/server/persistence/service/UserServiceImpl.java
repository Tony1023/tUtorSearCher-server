package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter.UserProfile;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.CourseDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.RequestDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.UserDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.RequestOverlap;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private CourseOfferedService courseOfferedService;
    @Autowired
    private CourseTakenService courseTakenService;
    @Autowired
    private RequestDAO requestDAO;

    @Override
    public User findUserById(long id) {
        return userDAO.findById(id);
    }

    @Override
    public User findUserByCredentials(String email, String password) {
        User user = null;
        try {
            user = userDAO.findByCredentials(email, password);
        } catch (NoResultException ignored) { }
        return user;
    }

    @Override
    public void saveUser(UserProfile profile) {
        User user = findUserById(profile.getId());
        user.setName(profile.getName());
        user.setGrade(profile.getGrade());
        user.setBio(profile.getBio());
        List<Integer> availability = profile.getAvailability();
        userDAO.saveUser(user);
        for (Request request: user.getAcceptedRequestsAsTutor()) {
            for (RequestOverlap overlap: request.getOverlap()) {
                if (availability.contains(overlap.getSlot())) {
                    availability.remove(overlap.getSlot());
                }
            }
        }
        availabilityService.updateAvailability(profile.getId(), availability);
        courseOfferedService.updateCourseOffered(profile.getId(), profile.getTutorClasses());
        courseTakenService.updateCoursesTaken(profile.getId(), profile.getCoursesTaken());
    }

    @Override
    public long addUser(String email, String password) {
        User user = new User(email, password);
        return userDAO.addUser(user);
    }

    @Override
    public List<User> searchTutors(String courseNumber, List<Integer> slots, long searcherId) {
        Course course = courseDAO.findByCourseNumber(courseNumber);
        User searcher = userDAO.findById(searcherId);
        return userDAO.findTutors(course, slots, searcher);
    }

    @Override
    public List<User> getTutors(long id) {
        User tutee = userDAO.findById(id);
        return requestDAO.findByTutee(tutee)
                .stream()
                .map(Request::getTutor)
                .collect(Collectors.toList());
    }
}
