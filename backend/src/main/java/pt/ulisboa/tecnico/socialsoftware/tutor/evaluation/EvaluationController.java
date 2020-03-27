package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EvaluationController {

    private EvaluationService evaluationService;

    EvaluationController(EvaluationService evaluationService){ this.evaluationService = evaluationService;}

    @PutMapping("/evaluations/{questionId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#questionId, 'QUESTION.ACCESS')")
    public EvaluationDto submitEvaluation(@Valid @RequestBody EvaluationDto evaluationDto, @PathVariable Integer questionId){
        return this.evaluationService.submitEvaluation(evaluationDto, questionId);
    }
}

