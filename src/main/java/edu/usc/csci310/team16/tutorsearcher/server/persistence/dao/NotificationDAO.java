package edu.usc.csci310.team16.tutorsearcher.server.persistence.dao;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Notification;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Request;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.User;

import java.util.List;

public interface NotificationDAO {
    List<Notification> getNotifications(long id);
    List<Notification> getNotifications(long id, boolean pushed);
    void addNotification(Request request, User sender, User receiver, int type);
    void changedPushed(Notification notification, boolean pushed);
    void removeNotificationsByRequest(Request request);
}
