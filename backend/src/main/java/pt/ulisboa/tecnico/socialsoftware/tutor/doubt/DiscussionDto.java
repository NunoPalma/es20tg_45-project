package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscussionDto implements Serializable {
    private Integer id;
    private Set<DoubtDto> postsDto = new HashSet<>();
    private String questionTitle;
    private Discussion.Visibility visibility;
    private String title;

    public DiscussionDto(){
    }


    public DiscussionDto(Discussion discussion){
        this.id = discussion.getId();
        this.visibility = discussion.getVisibility();
        this.postsDto = discussion.getPosts().stream().map(DoubtDto::new).collect(Collectors.toSet());
        this.questionTitle = discussion.getQuestion().getTitle();
        this.title = discussion.getTitle();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPostsDto(Set<DoubtDto> postsDto) {
        this.postsDto = postsDto;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setVisibility(Discussion.Visibility visibility) {
        this.visibility = visibility;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Discussion.Visibility getVisibility() {
        return visibility;
    }

    public Set<DoubtDto> getPostsDto() {
        return postsDto;
    }

    public String getTitle() {
        return title;
    }

}
