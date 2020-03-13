package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import java.io.Serializable;

public class DoubtDto implements Serializable {
    private Integer id;
    private String content;
    private String author;

    public DoubtDto(){
    }

    public DoubtDto(Doubt doubt){
        this.content = doubt.getContent();
        this.author = doubt.getAuthor().getName();
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
