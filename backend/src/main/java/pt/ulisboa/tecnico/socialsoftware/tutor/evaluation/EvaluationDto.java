package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;


public class EvaluationDto {
    private boolean approvedEvaluation = false;

    private String justification;

    private QuestionDto submittedQuestion;

    EvaluationDto(){

    }
}
