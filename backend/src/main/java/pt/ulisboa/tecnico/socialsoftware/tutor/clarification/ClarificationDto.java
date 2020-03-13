package pt.ulisboa.tecnico.socialsoftware.tutor.clarification;

import java.io.Serializable;

public class ClarificationDto implements Serializable {

    private String description;

    private String author;

    public ClarificationDto() {}

    public ClarificationDto(Clarification clarification) {
        this.description = clarification.getClarification();
        this.author = clarification.getAuthor().getUsername();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }
}
