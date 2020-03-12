package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    private boolean approvedEvaluation = false;

    private String justification;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "evaluation")
    private Question submittedQuestion;

    Evaluation() {
    }

    Evaluation(Question question, EvaluationDto evaluationDto) {
        submittedQuestion = question;
    }

    public boolean getEvaluation() {
        return approvedEvaluation;
    }

    public String getJustification() {
        return justification;
    }

    public Question getSubmittedQuestion() {
        return submittedQuestion;
    }
}
