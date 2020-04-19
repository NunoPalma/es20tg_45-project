package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.Clarification;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationDto;

import java.io.Serializable;

public class DoubtDto implements Serializable {
    private Integer id;
    private String content;
    private String author;
    private Doubt.Status status;
    private ClarificationDto clarificationDto;

    public DoubtDto(){
    }

    public DoubtDto(Doubt doubt){
        this.id = doubt.getId();
        this.content = doubt.getContent();
        this.author = doubt.getAuthor().getName();
        this.status = doubt.getStatus();
        this.clarificationDto = new ClarificationDto(doubt.getClarification());
    }

    public Doubt.Status getStatus() {
        return status;
    }

    public void setStatus(Doubt.Status status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
