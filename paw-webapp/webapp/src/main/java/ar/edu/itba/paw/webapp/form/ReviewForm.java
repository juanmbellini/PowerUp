package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Web form for writing reviews. Contains all the fields that the users are responsible
 * for rilling out, with their constraints.
 */
public class ReviewForm {

    @Length(min = 5, max = 10000, message = "Please write between 5 and 10000 characters")
    private String review;

    @Min(value = 1, message = "Select a score")
    @Max(value = 10, message = "Select a score")
    private int storyScore;

    @Min(value = 1, message = "Select a score")
    @Max(value = 10, message = "Select a score")
    private int graphicsScore;

    @Min(value = 1, message = "Select a score")
    @Max(value = 10, message = "Select a score")
    private int audioScore;

    @Min(value = 1, message = "Select a score")
    @Max(value = 10, message = "Select a score")
    private int controlsScore;

    @Min(value = 1, message = "Select a score")
    @Max(value = 10, message = "Select a score")
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
