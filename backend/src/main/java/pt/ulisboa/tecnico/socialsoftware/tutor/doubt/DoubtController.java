package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_NOT_FOUND;


@RestController
public class DoubtController {

    @Autowired
    DoubtService doubtService;

    @Autowired
    DoubtRepositor doubtRepositor;

    @PostMapping("/doubts/{doubtId}/visibility/{status}")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public DoubtDto changeVisibility(@PathVariable int doubtId, @PathVariable Doubt.Visibility status) {
        return doubtService.changeVisibility(doubtId, status);
    }

    @GetMapping("/doubts")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<DoubtDto> getStudentDoubts(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return doubtService.findUserDoubts(user.getId());
    }

    @PostMapping(value = "quizQuestion/{quizQuestionId}/doubts")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DoubtDto createDoubt(Principal principal, @RequestBody DoubtDto doubtDto, @PathVariable int quizQuestionId){
        Integer studentId = ((User) ((Authentication) principal).getPrincipal()).getId();
        return this.doubtService.createDoubt(doubtDto, quizQuestionId, studentId);
    }

    @GetMapping(value = "quizQuestion/{quizQuestionId}/doubts")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<DoubtDto> getDoubtsOfQuestions(Principal principal, @PathVariable int quizQuestionId) {
        return doubtService.findQuizQuestionDoubts(quizQuestionId);
    }

    @GetMapping("/doubts/all")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public List<DoubtDto> manageDoubts(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return doubtService.findCourseExecutionDoubts(new ArrayList<>(user.getCourseExecutions()));
    }

}
