package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RequestOverlap")
public class RequestOverlap implements Serializable {

    public RequestOverlap() { }

    public RequestOverlap(Request request, int slot) {
        this.request = request;
        this.slot = slot;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "req_id")
    private Request request;

    @Id
    @Column(name = "slot_num")
    private int slot;


    public int getSlot() {
        return slot;
    }
}
