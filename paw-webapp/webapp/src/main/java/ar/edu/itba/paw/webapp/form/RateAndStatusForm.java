package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.model.PlayStatus;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class RateAndStatusForm {

    @Min(1)
    @Max(10)
    private Integer score;

    private PlayStatus playStatus;

    public Integer getScore() {
        return score;
    }

    public PlayStatus getPlayStatus() {
        return playStatus;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setPlayStatus(PlayStatus playStatus) {
        this.playStatus = playStatus;
    }
}




