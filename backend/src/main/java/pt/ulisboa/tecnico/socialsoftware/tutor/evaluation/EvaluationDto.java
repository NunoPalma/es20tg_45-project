package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;


public class EvaluationDto {
    private boolean approvedEvaluation = false;

    private Integer id;
    private Integer key;

    private String justification;

    private QuestionDto submittedQuestion;

    public EvaluationDto() {

    }

    public EvaluationDto(Evaluation evaluation, QuestionDto questionDto) {
        if (evaluation.getEvaluation()) {
            approvedEvaluation = true;
        }
        this.id = evaluation.getId();
        this.key = questionDto.getKey();
        this.justification = evaluation.getJustification();
        this.submittedQuestion = questionDto;
    }

    public void setId(int questionId) { id = questionId; }

    public Integer getId() { return id; }

    public void setKey(int questionKey) { key = questionKey; }

    public Integer getKey() { return key; }

    public QuestionDto getSubmittedQuestionDto() { return submittedQuestion; }

    public void setSubmittedQuestionDto(QuestionDto questionDto) { this.submittedQuestion = questionDto; }

    public void setJustification(String message) { justification  = message; }

    public void approveEvaluationDto() { approvedEvaluation = true; }
}
