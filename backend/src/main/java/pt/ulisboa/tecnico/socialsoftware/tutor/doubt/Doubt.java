package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.hibernate.annotations.JoinColumnOrFormula;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.Clarification;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NAME_IS_EMPTY;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_CONTENT_IS_EMPTY;

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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "doubt")
    private Clarification clarification = null;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User author;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private QuestionAnswer questionAnswer;

    public Doubt(){
    }

    public Doubt(QuestionAnswer questionAnswer, User user, String content){
        this.author = user;
        this.author.addDoubt(this);
        if (content == null || content.trim().isEmpty()) {
            throw new TutorException(DOUBT_CONTENT_IS_EMPTY);
        }
        this.content = content;
        this.questionAnswer = questionAnswer;
        this.questionAnswer.addDoubt(this);
    }




    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;
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

    public void setStatus(Status status){
        this.status = status;
    }

    public Question getQuestion(){
        return questionAnswer.getQuizQuestion().getQuestion();
    }
}

