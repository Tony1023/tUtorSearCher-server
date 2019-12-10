package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Notifications")
@NamedQueries({
        @NamedQuery(
                name = "getNotificationsByReceiver",
                query = "FROM Notification WHERE receiver_id=:id ORDER BY id DESC"
        ),
        @NamedQuery(
                name = "getNotificationsByReceiverAndPushed",
                query = "FROM Notification WHERE receiver_id=:id AND PUSHED=:pushed ORDER BY id DESC"
        ),
        @NamedQuery(
                name = "findNotificationsByRequest",
                query = "FROM Notification WHERE req_id=:id"
        )
})
public class Notification {

    public Notification() { }

    public Notification(Request request, User sender, User receiver, int receiverType) {
        this.request = request;
        this.sender = sender;
        this.receiver = receiver;
        this.receiverType = receiverType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "req_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "receiver_type")
    private int receiverType;

    @Column(name = "pushed")
    private boolean pushed;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    public long getId() {
        return id;
    }
}
