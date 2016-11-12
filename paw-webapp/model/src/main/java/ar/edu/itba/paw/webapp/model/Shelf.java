package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Shelves are custom-named game lists created by users to organize their gameIds. Users might create a Shelf for the
 * weekends, another one with their favorite gameIds, and yet another one for the gameIds they want to buy, etc.
 */
@Entity
@Table(name = "shelves")
public class Shelf {

    @Id
    @SequenceGenerator(name = "shelves_seq", sequenceName = "shelves_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shelves_seq")
    private long id;

    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(
            name = "shelf_games",
            joinColumns=@JoinColumn(name = "shelf_id")
    )
    private Set<Long> gameIds = new LinkedHashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar updatedAt;


    /*package*/  Shelf() {
        //for Hibernate
    }

    public Shelf(String name, User creator, Set<Long> initialGameIds) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Shelf name cannot be null or empty");
        }
        if(creator == null) {
            throw new IllegalArgumentException("Shelf creator cannot be null, must belong to someone");
        }
        this.name = name;
        this.user = creator;
        if(initialGameIds != null) {
            this.gameIds.addAll(initialGameIds);
        }
    }

    public Shelf(String name, User creator) {
        this(name, creator, Collections.emptySet());
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Set<Long> getGameIds() {
        return Collections.unmodifiableSet(gameIds);
    }

    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(createdAt.toInstant(), createdAt.getTimeZone().toZoneId());
    }

    public LocalDateTime getUpdatedAt() {
        return LocalDateTime.ofInstant(updatedAt.toInstant(), updatedAt.getTimeZone().toZoneId());
    }

    public void addGame(long gameId) {
        if(gameIds.contains(gameId)) {
            throw new IllegalArgumentException("Game #" + gameId + " is already in Shelf #" + id);
        }
        gameIds.add(gameId);
    }

    public void removeGame(long gameId) {
        if(!gameIds.contains(gameId)) {
            throw new IllegalArgumentException("Game #" + gameId + " is not in Shelf #" + id);
        }
        gameIds.remove(gameId);
    }

    public void clear() {
        gameIds.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        return id == shelf.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
