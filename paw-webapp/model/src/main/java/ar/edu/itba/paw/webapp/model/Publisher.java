package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by julrodriguez on 26/10/16.
 */
@Entity
@Table(name = "companies")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="companies_seq", sequenceName="companies_companiesid_seq",allocationSize=1)
    private long id;

    @Column(length = 100)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "publishers")
    private Collection<Game> games;

    public Publisher(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /*package*/  Publisher() {
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

    public Collection<Game> getGames() {
        return games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Publisher publisher = (Publisher) o;

        if (id != publisher.id) return false;
        return name != null ? name.equals(publisher.name) : publisher.name == null;

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
