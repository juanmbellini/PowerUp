package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateThreadForm {

    @NotNull
    @NotEmpty(message = "Please enter a thread title")
    @Length(min = 1, max = 50, message = "Please write between 1 and 50 characters")
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
