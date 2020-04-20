package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.validation.Valid;
import java.security.Principal;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.AUTHENTICATION_ERROR;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.CLARIFICATION_INVALID_USER;

@RestController
public class EvaluationController {

    private EvaluationService evaluationService;

    EvaluationController(EvaluationService evaluationService){ this.evaluationService = evaluationService;}

    @PutMapping("/evaluations/{questionId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#questionId, 'QUESTION.ACCESS')")
    public EvaluationDto submitEvaluation(Principal principal, @Valid @RequestBody EvaluationDto evaluationDto, @PathVariable Integer questionId){

        User user = (User) ((Authentication) principal).getPrincipal();

        if (user == null) {
            throw new TutorException(AUTHENTICATION_ERROR);
        } if (user.getRole() != User.Role.TEACHER) {
            throw new TutorException(CLARIFICATION_INVALID_USER);
        }

        return this.evaluationService.submitEvaluation(user.getUsername(), evaluationDto, questionId);
    }
}

