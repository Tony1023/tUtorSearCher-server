package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Notification;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("NotificationDAO")
public class NotificationDAOImpl extends AbstractDAO implements NotificationDAO {

    @Override
    public List<Notification> getNotifications(long id) {
        return em.createNamedQuery("getNotificationsByReceiver", Notification.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Notification> getNotifications(long id, boolean pushed) {
        return em.createNamedQuery("getNotificationsByReceiverAndPushed", Notification.class)
                .setParameter("id", id)
                .setParameter("pushed", pushed)
                .getResultList();
    }

    @Override
    public void addNotification(Request request, User sender, User receiver, int type) {
        Notification notification = new Notification(request, sender, receiver, type);
        em.persist(notification);
    }

    @Override
    public void changedPushed(Notification notification, boolean pushed) {
        notification.setPushed(pushed);
        em.merge(notification);
    }

    @Override
    public void removeNotificationsByRequest(Request request) {
        for (Notification notification: em.createNamedQuery("findNotificationsByRequest", Notification.class)
                .setParameter("id", request.getId())
                .getResultList()) {
            em.remove(notification);
        }
    }

}