package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable = false)
    private Integer key;

    private boolean approvedEvaluation = false;

    private String justification;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "evaluation")
    private Question submittedQuestion;

    // Who reviewed the question? Is that user a professor? A professor for the course the question is part of?

    public Evaluation(){
    }

    public Evaluation(Question question) {
        key = question.getKey();
        submittedQuestion = question;
    }

    public boolean getEvaluation() { return approvedEvaluation; }

    public void approveEvaluation() { approvedEvaluation = true; }

    public String getJustification() { return justification; }

    public void setJustification(String justification) { this.justification = justification; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getKey() { return key; }

    public void setKey(Integer key) { this.key = key; }

    public Question getSubmittedQuestion() { return submittedQuestion; }
}
