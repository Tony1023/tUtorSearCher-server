package edu.usc.csci310.team16.tutorsearcher.server.persistence.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "AuthTokens")
@NamedQueries(
        @NamedQuery(
                name = "findEntryByIdAndToken",
                query = "FROM AuthToken WHERE user_id=:id AND auth_token=:token AND expires > CURDATE()"
        )
)
public class AuthToken {

    public AuthToken() { }

    public AuthToken(User user, String token) {
        this.user = user;
        this.token = token;
        expires = new Timestamp(new Date().getTime() + 3 * 24 * 60 * 60 * 1000);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "auth_token")
    private String token;

    @Column(name = "date_added")
    @CreationTimestamp
    private Timestamp dateAdded;

    @Column(name = "date_active")
    private Timestamp expires;
}
