package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.model.PlayStatus;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RateAndStatusForm {

    @Min(1)
    @Max(10)
    private int score;

    private PlayStatus playStatus;

    public int getScore() {
        return score;
    }

    public PlayStatus getPlayStatus() {
        return playStatus;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayStatus(PlayStatus playStatus) {
        this.playStatus = playStatus;
    }
}




