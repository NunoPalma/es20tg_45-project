package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.Clarification;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;

@Entity
@Table(name = "Doubts")
public class Doubt{
    public enum Status {SOLVED, UNSOLVED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNSOLVED;

    private String content;

    @OneToOne
    private Clarification clarification = null;

    @ManyToOne
    private User author;

    @ManyToOne
    private Question question;

    public Doubt(){
    }

    public Doubt(Question question, User user, String content){
        this.author = user;
        this.content = content;
        this.question = question;
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public Integer getId() {
        return id;
    }

    public Clarification getClarification() {
        return clarification;
    }

    public void setClarification(Clarification clarification) {
        this.clarification = clarification;
    }

    public void solve() {
        this.status = Status.SOLVED;
    }

    public void unsolve() {
        this.status = Status.UNSOLVED;
    }

    public Status getStatus() {
        return status;
    }
}

