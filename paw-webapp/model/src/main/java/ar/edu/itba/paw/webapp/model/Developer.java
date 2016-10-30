package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by julrodriguez on 26/10/16.
 */
@Entity
@Table(name = "companies")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="companies_seq", sequenceName="companies_companiesid_seq",allocationSize=1)
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "developers")
    private Collection<Game> games;

    public Developer(long id, String name) {
        this.id = id;
        this.name = name;
    }
    /*package*/  Developer() {
        //for hibernate
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGames(Collection<Game> games) {
        this.games = games;
    }

    public Collection<Game> getGames() {
        return games;
    }
}
