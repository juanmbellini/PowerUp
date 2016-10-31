package ar.edu.itba.paw.webapp.model;

import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by julrodriguez on 26/10/16.
 */
@Embeddable
public class GamePlatformRelationData {

    @Column(name = "release_DATE")
    LocalDate createdAt;

    /*package*/ GamePlatformRelationData() {
        //For Hibernate
    }
}
