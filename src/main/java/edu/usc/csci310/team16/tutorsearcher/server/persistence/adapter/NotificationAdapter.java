package edu.usc.csci310.team16.tutorsearcher.server.persistence.adapter;

import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.Notification;
import edu.usc.csci310.team16.tutorsearcher.server.persistence.model.RequestOverlap;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter {

    public NotificationAdapter(Notification notification) {
        id = notification.getId();
        receiver_id = notification.getReceiver().getId();
        sender_id = notification.getSender().getId();
        req_id = notification.getRequest().getId();
        sender_name = notification.getSender().getName();
        type = notification.getReceiverType();
        req_status = notification.getRequest().getStatus();

        List<String> overlapStr = new ArrayList<>();
        for (int i = 0; i < 28 * 7; ++i) {
            overlapStr.add("0");
        }
        for (RequestOverlap slot: notification.getRequest().getOverlap()) {
            overlapStr.set(slot.getSlot(), "1");
        }
        overlap = String.join(",", overlapStr);

        if (req_status == 1) {
            msg = notification.getSender().getEmail();
        } else {
            msg = "";
        }
    }

    private long id;
    private long receiver_id;
    private long sender_id;
    private long req_id;
    private String sender_name;
    private int type;
    private int req_status;
    private String msg;
    private String overlap;

    public long getId() {
        return id;
    }

    public long getReceiver_id() {
        return receiver_id;
    }

    public long getSender_id() {
        return sender_id;
    }

    public long getReq_id() {
        return req_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public int getType() {
        return type;
    }

    public int getReq_status() {
        return req_status;
    }

    public String getMsg() {
        return msg;
    }

    public String getOverlap() {
        return overlap;
    }
}
