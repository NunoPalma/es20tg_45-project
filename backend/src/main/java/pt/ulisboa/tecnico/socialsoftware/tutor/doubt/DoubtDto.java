package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.Clarification;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt;

import java.io.Serializable;

public class DoubtDto implements Serializable {
    private Integer id;
    private String content;
    private String author;
    private Doubt.Status status;
    private Doubt.Visibility visibility;
    private ClarificationDto clarificationDto;
    private String questionTitle;
    private String title;
    private String creationDate;
    private boolean isNew;

    private Doubt.DoubtType doubtType;
    private int mainDoubtId;


    public DoubtDto(){
    }

    public DoubtDto(Doubt doubt){
        this.title = doubt.getTitle();
        this.id = doubt.getId();
        this.content = doubt.getContent();
        this.author = doubt.getAuthor().getName();
        this.creationDate = doubt.getCreationDate();
        this.status = doubt.getStatus();
        this.visibility = doubt.getVisibility();
        if(doubt.getClarification() != null) {
            this.clarificationDto = new ClarificationDto(doubt.getClarification());
        }
        this.questionTitle = doubt.getQuestionAnswer().getQuizQuestion().getQuestion().getTitle();
        this.isNew = doubt.isNew();
        this.doubtType = doubt.getDoubtType();
        this.mainDoubtId = doubt.getMainDoubtId();
    }

    public Doubt.DoubtType getDoubtType() {
        return doubtType;
    }

    public void setDoubtType(Doubt.DoubtType doubtType) {
        this.doubtType = doubtType;
    }

    public int getMainDoubtId() {
        return mainDoubtId;
    }

    public void setMainDoubtId(int mainDoubtId) {
        this.mainDoubtId = mainDoubtId;
    }

    public Doubt.Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Doubt.Visibility visibility) {
        this.visibility = visibility;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public boolean isNew(){
        return isNew;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public ClarificationDto getClarificationDto() {
        return clarificationDto;
    }

    public void setClarificationDto(ClarificationDto clarificationDto) {
        this.clarificationDto = clarificationDto;
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
