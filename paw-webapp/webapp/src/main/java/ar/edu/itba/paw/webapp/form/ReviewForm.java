package ar.edu.itba.paw.webapp.form;

import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Web form for writing reviews. Contains all the fields that the users are responsible
 * for rilling out, with their constraints.
 */
public class ReviewForm {

    @Length(min = 50, max = 10000, message = "Please write between 50 and 10000 characters")
    private String review;

    @Min(value = 1, message = "Score must be between 1 and 10")
    @Max(value = 10, message = "Score must be between 1 and 10")
    private int storyScore;

    @Min(value = 1, message = "Score must be between 1 and 10")
    @Max(value = 10, message = "Score must be between 1 and 10")
    private int graphicsScore;

    @Min(value = 1, message = "Score must be between 1 and 10")
    @Max(value = 10, message = "Score must be between 1 and 10")
    private int audioScore;

    @Min(value = 1, message = "Score must be between 1 and 10")
    @Max(value = 10, message = "Score must be between 1 and 10")
    private int controlsScore;

    @Min(value = 1, message = "Score must be between 1 and 10")
    @Max(value = 10, message = "Score must be between 1 and 10")
    private int funScore;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStoryScore() {
        return storyScore;
    }

    public void setStoryScore(int storyScore) {
        this.storyScore = storyScore;
    }

    public int getGraphicsScore() {
        return graphicsScore;
    }

    public void setGraphicsScore(int graphicsScore) {
        this.graphicsScore = graphicsScore;
    }

    public int getAudioScore() {
        return audioScore;
    }

    public void setAudioScore(int audioScore) {
        this.audioScore = audioScore;
    }

    public int getControlsScore() {
        return controlsScore;
    }

    public void setControlsScore(int controlsScore) {
        this.controlsScore = controlsScore;
    }

    public int getFunScore() {
        return funScore;
    }

    public void setFunScore(int funScore) {
        this.funScore = funScore;
    }
}
