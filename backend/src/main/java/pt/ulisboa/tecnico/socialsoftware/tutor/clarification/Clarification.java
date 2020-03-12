package pt.ulisboa.tecnico.socialsoftware.tutor.clarification;

import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;

@Entity
@Table(name = "clarifications")
public class Clarification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User author;

    @OneToOne
    @JoinColumn(name = "doubt_id")
    private Doubt doubt;

    private String clarification;

    public Clarification() {}

    public Clarification(User author, Doubt doubt, ClarificationDto clarificationDto) {
        checkConsistentClarification(clarificationDto, author, doubt);
        this.author = author;
        this.doubt = doubt;
        this.clarification = clarificationDto.getDescription();
    }

    private void checkConsistentClarification(ClarificationDto clarificationDto, User author, Doubt doubt) {
        if(clarificationDto.getDescription().trim().length() == 0) {
            throw new TutorException(ErrorMessage.CLARIFICATION_EMPTY);
        }

        if(author.getRole().compareTo(User.Role.TEACHER) == 0) {
            throw  new TutorException(ErrorMessage.CLARIFICATION_INVALID_USER);
        }

        if(doubt.getStatus().compareTo(Doubt.Status.SOLVED) == 0) {
            throw  new TutorException(ErrorMessage.CLARIFICATION_NOT_ALLOWED);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Doubt getDoubt() {
        return doubt;
    }

    public void setDoubt(Doubt doubt) {
        this.doubt = doubt;
    }

    public String getClarification() {
        return clarification;
    }

    public void setClarification(String clarification) {
        this.clarification = clarification;
    }
}
