package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Availability")
@NamedQueries(
        @NamedQuery(
                name = "getAvailabilitiesById",
                query = "FROM Availability WHERE user_id=:id"
        )
)
public class Availability implements Serializable {

    public Availability() { }

    public Availability(User user, int slot) {
        this.user = user;
        this.slot = slot;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @Column(name = "slot_num")
    private int slot;

    public int getSlot() {
        return slot;
    }
}
