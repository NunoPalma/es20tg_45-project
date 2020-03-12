package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;


public class EvaluationDto {
    private boolean approvedEvaluation = false;

    private Integer id;

    private String justification;

    private QuestionDto submittedQuestion;

    EvaluationDto() {

    }

    public void setId(int questionId) {
        id = questionId;
    }

    public void setJustification(String message) {
        justification  = message;
    }

    public void approveEvaluationDto() {
        approvedEvaluation = true;
    }
}
