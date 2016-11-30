package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Droche on 26/11/16.
 */
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @SequenceGenerator(name = "companies_seq", sequenceName = "companies_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companies_seq")
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "developers")
    private Collection<Game> gamesDeveloped;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "publishers")
    private Collection<Game> gamesPublished;

    public Company(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /*package*/ Company() {
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

    public void setGamesDeveloped(Collection<Game> games) {
        this.gamesDeveloped = games;
    }

    public void setGamesPublished(Collection<Game> games) {
        this.gamesPublished = games;
    }

    public Collection<Game> getGamesDeveloped() {
        return gamesDeveloped;
    }

    public Collection<Game> getGamesPublished() {
        return gamesPublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (id != company.id) return false;
        return name != null ? name.equals(company.name) : company.name == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + " (id = " + id + ")";
    }
}
