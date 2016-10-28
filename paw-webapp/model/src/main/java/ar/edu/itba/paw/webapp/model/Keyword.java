package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by julrodriguez on 26/10/16.
 */
@Entity
@Table(name = "keywords")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keywords_keywordid_seq")
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "games")
    private Collection<Game> games;

    public Keyword(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /*package*/  Keyword() {
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
}
