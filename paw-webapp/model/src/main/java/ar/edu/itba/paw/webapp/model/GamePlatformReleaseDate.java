package ar.edu.itba.paw.webapp.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * Created by julrodriguez on 26/10/16.
 */
@Embeddable
public class GamePlatformReleaseDate {

    @Column(name = "release_date")
    private LocalDate releaseDate;

    /*package*/ GamePlatformReleaseDate() {
        //For Hibernate
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
}
