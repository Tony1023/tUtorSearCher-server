package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.*;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private RequestDAO requestDAO;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private NotificationDAO notificationDAO;
    @Autowired
    private RequestOverlapDAO requestOverlapDAO;
    @Autowired
    private AvailabilityDAO availabilityDAO;

    @Override
    public int addRequest(long tuteeId, long tutorId, String courseNumber, List<Integer> overlap) {
        Course course = courseDAO.findByCourseNumber(courseNumber);
        User tutor = userDAO.findById(tutorId);
        User tutee = userDAO.findById(tuteeId);
        List<Request> requests = requestDAO.findByPeopleAndCourse(tutee, tutor, course);
        if (!requests.isEmpty()) { // Already requested to the same tutor
            return 1;
        }
        requests = requestDAO.findByTuteeAndCourse(tutee, course, 1);
        if (!requests.isEmpty()) {
            return -1;
        }
        Request req = requestDAO.addRequest(tutee, tutor, course);
        requestOverlapDAO.addOverlaps(req, overlap);
        notificationDAO.addNotification(req, tutee, tutor, 0);
        return 0;
    }

    @Override
    public Request findById(long id) {
        return requestDAO.findById(id);
    }

    @Override
    public void acceptRequest(Request request, List<Integer> overlap) {
        requestDAO.updateRequestStatus(request, 1);
        List<Request> requests = requestDAO.findByTuteeAndCourse(request.getTutee(), request.getCourse(), 0);
        for (Request toInvalidate: requests) {
            requestDAO.updateRequestStatus(toInvalidate, 3);
        }
        notificationDAO.removeNotificationsByRequest(request);
        notificationDAO.addNotification(request, request.getTutor(), request.getTutee(), 1);
        notificationDAO.addNotification(request, request.getTutee(), request.getTutor(), 0);
        requestOverlapDAO.removeOverlaps(request);
        requestOverlapDAO.addOverlaps(request, overlap);
        availabilityDAO.removeSlots(request.getTutor(), overlap);
        em.flush();
        em.refresh(request);
    }

    @Override
    public void rejectRequest(Request request) {
        requestDAO.updateRequestStatus(request, 2);
        notificationDAO.addNotification(request, request.getTutor(), request.getTutee(), 1);
    }
}
