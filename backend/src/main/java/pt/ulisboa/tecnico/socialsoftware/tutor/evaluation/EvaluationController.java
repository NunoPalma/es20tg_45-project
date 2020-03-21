package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;

import javax.validation.Valid;

@RestController
public class EvaluationController {

    private EvaluationService evaluationService;

    EvaluationController(EvaluationService evaluationService){ this.evaluationService = evaluationService;}

    @PutMapping("/evaluations/{courseId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#questionId, 'QUESTION.ACCESS')")
    public EvaluationDto submitEvaluation(@Valid @RequestBody QuestionDto question, @RequestParam boolean approved, @RequestParam String justification){
        return this.evaluationService.submitEvaluation(question, approved, justification);
    }
}
