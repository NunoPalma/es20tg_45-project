package pt.ulisboa.tecnico.socialsoftware.tutor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtController;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.AssessmentService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.TopicService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.QuizService;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserService;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_USER_HASNT_ANSWERED;

@Component
public class TutorPermissionEvaluator implements PermissionEvaluator {
    private static Logger logger = LoggerFactory.getLogger(TutorPermissionEvaluator.class);

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private DoubtService doubtService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        String username = ((User) authentication.getPrincipal()).getUsername();

        if (targetDomainObject instanceof CourseDto) {
            CourseDto courseDto = (CourseDto) targetDomainObject;
            String permissionValue = (String) permission;
            switch (permissionValue) {
                case "EXECUTION.CREATE":
                    return userService.getEnrolledCoursesAcronyms(username).contains(courseDto.getAcronym() + courseDto.getAcademicTerm());
                case "DEMO.ACCESS":
                    return courseDto.getName().equals("Demo Course");
                default:
                    return false;
            }
        }

        if (targetDomainObject instanceof Integer) {
            int id = (int) targetDomainObject;
            String permissionValue = (String) permission;
            switch (permissionValue) {
                case "COURSE.ACCESS":
                    return userHasAnExecutionOfTheCourse(username, id);
                case "EXECUTION.ACCESS":
                    return userHasThisExecution(username, id);
                case "QUESTION.ACCESS":
                    return userHasAnExecutionOfTheCourse(username, questionService.findQuestionCourse(id).getCourseId());
                case "QUESTION.ANSWERED":
                    logger.info("Entra aqui");
                    return hasAnsweredQuestion(username, id);
                case "TOPIC.ACCESS":
                    return userHasAnExecutionOfTheCourse(username, topicService.findTopicCourse(id).getCourseId());
                case "ASSESSMENT.ACCESS":
                    return userHasThisExecution(username, assessmentService.findAssessmentCourseExecution(id).getCourseExecutionId());
                case "QUIZ.ACCESS":
                    return userHasThisExecution(username, quizService.findQuizCourseExecution(id).getCourseExecutionId());
                case "CLARIFICATION.CREATE":
                    return userHasThisExecution(username, questionService.findQuestionCourse(doubtService.getDoubtQuestion(id).getId()).getCourseExecutionId());
                default: return false;
            }
        }

        return false;
    }

    private boolean userHasAnExecutionOfTheCourse(String username, int id) {
        return userService.getCourseExecutions(username).stream().anyMatch(course -> course.getCourseId() == id);
    }


    boolean hasAnsweredQuestion(String username, int id){
        return userService.getAnsweredQuestions(username).stream().anyMatch(question -> question.getId() == id)
                && userHasAnExecutionOfTheCourse(username, questionService.findQuestionCourse(id).getCourseId());
    }



    private boolean userHasThisExecution(String username, int id) {
        return userService.getCourseExecutions(username).stream()
                .anyMatch(course -> course.getCourseExecutionId() == id);
    }

     @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }

}
