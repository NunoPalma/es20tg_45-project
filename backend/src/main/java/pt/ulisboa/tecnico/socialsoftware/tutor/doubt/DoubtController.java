package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationDto;
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

    @PostMapping("/discussions/{discussionId}/visibility/{status}")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public DiscussionDto changeVisibility(@PathVariable int discussionId, @PathVariable Discussion.Visibility status) {
        return doubtService.changeVisibility(discussionId, status);
    }

    @GetMapping("/discussions")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<DiscussionDto> getStudentDoubts(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return doubtService.findUserDiscussions(user.getId());
    }

    @PostMapping(value = "quizQuestion/{quizQuestionId}/discussions")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DiscussionDto createDiscussion(Principal principal, @RequestBody DiscussionDto discussionDto, @PathVariable int quizQuestionId){
        Integer studentId = ((User) ((Authentication) principal).getPrincipal()).getId();
        return doubtService.createDiscussion(discussionDto, quizQuestionId, studentId);
    }

    @GetMapping(value = "quizQuestion/{quizQuestionId}/discussions")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<DiscussionDto> getDiscussionsOfQuestions(Principal principal, @PathVariable int quizQuestionId) {
        return doubtService.findQuizQuestionDiscussions(quizQuestionId);
    }

    @GetMapping("/discussions/all")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public List<DiscussionDto> manageDiscussions(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return doubtService.findCourseExecutionDiscussions(new ArrayList<>(user.getCourseExecutions()));
    }

}
