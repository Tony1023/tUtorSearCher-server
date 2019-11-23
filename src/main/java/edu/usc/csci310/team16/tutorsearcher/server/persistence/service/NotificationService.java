package edu.usc.csci310.team16.tutorsearcher.server.persistence.service;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(long id);
    List<Notification> getNotifications(long id, boolean pushed);
    int getUnpushedNotificationCount(long id); // also change them to pushed
}
