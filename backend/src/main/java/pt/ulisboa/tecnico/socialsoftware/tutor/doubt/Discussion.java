package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "discussions")
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    public enum Visibility {PUBLIC, PRIVATE}

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User author;

    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PRIVATE;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discussion", fetch= FetchType.EAGER, orphanRemoval=true)
    Set<Doubt> posts = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private QuestionAnswer questionAnswer;

    private String questionTitle;

    public Discussion(){

    }

    public Discussion(QuestionAnswer questionAnswer, String title, User author){
        this.questionAnswer = questionAnswer;
        this.title = title;
        this.questionTitle = questionAnswer.getQuizQuestion().getQuestion().getTitle();
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public Integer getId() {
        return id;
    }

    public Set<Doubt> getPosts() {
        return posts;
    }

    public void addPost(Doubt doubt){
        this.posts.add(doubt);
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Question getQuestion(){
        return questionAnswer.getQuizQuestion().getQuestion();
    }



    public String getTitle() {
        return title;
    }
}
