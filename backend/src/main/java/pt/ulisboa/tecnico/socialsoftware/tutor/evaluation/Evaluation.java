package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")

public class Evaluation {

    private boolean approvedEvaluation = false;

    private String justification;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "evaluation")
    private Question submittedQuestion;

    public Evaluation(){
    }

    public Evaluation(Question question){
        submittedQuestion = question;
    }

    public boolean getEvaluation(){
        return approvedEvaluation;
    }

    public void approveEvaluation() { approvedEvaluation = true; }

    public String getJustification(){
        return justification;
    }

    public void setJustification(String justification) { this.justification = justification; }

    public Question getSubmittedQuestion(){
        return submittedQuestion;
    }
}