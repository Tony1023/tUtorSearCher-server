package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Course;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("RequestDAO")
public class RequestDAOImpl extends AbstractDAO implements RequestDAO {

    @Override
    public Request findById(long id) {
        return em.find(Request.class, id);
    }

    @Override
    public List<Request> findByPeopleAndCourse(User tutee, User tutor, Course course) {
        return em.createNamedQuery("findRequestByPeopleAndCourse", Request.class)
                .setParameter("tuteeId", tutee.getId())
                .setParameter("tutorId", tutor.getId())
                .setParameter("id", course.getId())
                .getResultList();
    }

    @Override
    public List<Request> findByTuteeAndCourse(User tutee, Course course) {
        return getQuery(tutee, course, null).getResultList();
    }

    @Override
    public List<Request> findByTuteeAndCourse(User tutee, Course course, int status) {
        return getQuery(tutee, course, status).getResultList();
    }

    @Override
    public List<Request> findByTutee(User tutee) {
        return em.createNamedQuery("findTutorsByTutee", Request.class)
                .setParameter("tuteeId", tutee.getId())
                .getResultList();
    }

    @Override
    public void updateRequestStatus(Request request, int status) {
        request.setStatus(status);
        em.merge(request);
    }

    @Override
    public Request addRequest(User tutee, User tutor, Course course) {
        Request request = new Request(tutor, tutee, course);
        em.persist(request);
        return request;
    }

    private TypedQuery<Request> getQuery(User tutee, Course course, Integer status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> query = cb.createQuery(Request.class);
        Root<Request> root = query.from(Request.class);
        if (status == null) {
            query.where(cb.and(cb.equal(root.get("tutee"), tutee),
                    cb.equal(root.get("course"), course)));
        } else {
            query.where(cb.and(cb.equal(root.get("tutee"), tutee),
                    cb.equal(root.get("course"), course),
                    cb.equal(root.get("status"), status)));
        }
        return em.createQuery(query);
    }


}
