package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.NotificationDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.dao.UserDAO;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Notification;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;

    @Override
    public List<Notification> getNotifications(long id) {
        return notificationDAO.getNotifications(id);
    }

    @Override
    public List<Notification> getNotifications(long id, boolean pushed) {
        return notificationDAO.getNotifications(id, pushed);
    }

    @Override
    public int getUnpushedNotificationCount(long id) {
        List<Notification> notifications = notificationDAO.getNotifications(id, false);
        for (Notification notification: notifications) {
            notificationDAO.changedPushed(notification, true);
        }
        return notifications.size();
    }
}
