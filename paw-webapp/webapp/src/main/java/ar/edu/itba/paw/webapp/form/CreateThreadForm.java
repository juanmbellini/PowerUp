package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateThreadForm {

    @NotNull
    @NotEmpty(message = "Please enter a thread title")
    private String title;

    @NotNull
    private String initialComment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitialComment() {
        return initialComment;
    }

    public void setInitialComment(String initialComment) {
        this.initialComment = initialComment;
    }
}
