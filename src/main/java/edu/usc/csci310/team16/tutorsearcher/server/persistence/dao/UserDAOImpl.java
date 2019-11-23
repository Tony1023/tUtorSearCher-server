package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("UserDAO")
public class UserDAOImpl extends AbstractDAO implements UserDAO {

    @Override
    public void saveUser(User user) {
        em.merge(user);
    }

    @Override
    public long addUser(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public User findByCredentials(String email, String password) {
        return em.createNamedQuery("findUserByCredentials", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    @Override
    public List<User> findTutors(Course course, List<Integer> slots) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<Course> course_ = query.from(Course.class);
        Root<CourseOffered> courseOffered_ = query.from(CourseOffered.class);
        Root<Availability> availability_ = query.from(Availability.class);
        CriteriaBuilder.In<Integer> in = cb.in(availability_.get("slot"));
        for (Integer slot: slots) {
            in.value(slot);
        }
        CriteriaQuery<User> cq = query.select(availability_.get("user")).where(cb.and(
                cb.equal(availability_.get("user"), courseOffered_.get("user")),
                cb.equal(course_.get("id"), courseOffered_.get("course")),
                in,
                cb.equal(course_.get("courseNumber"), course.getCourseNumber())
        ))
                .groupBy(availability_.get("user"))
                .orderBy(cb.desc(cb.count(availability_)));
        TypedQuery<User> tq = em.createQuery(cq);
        return tq.getResultList();
    }
}
