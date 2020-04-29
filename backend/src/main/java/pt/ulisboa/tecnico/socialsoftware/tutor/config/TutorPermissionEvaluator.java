package pt.ulisboa.tecnico.socialsoftware.tutor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.Clarification;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationController;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.administration.AdministrationService;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtController;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
=======
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseService;
>>>>>>> reference/master
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

    @Autowired
    private CourseService courseService;

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
<<<<<<< HEAD

        String username = ((User) authentication.getPrincipal()).getUsername();
=======
        int userId = ((User) authentication.getPrincipal()).getId();
>>>>>>> reference/master

        if (targetDomainObject instanceof CourseDto) {
            CourseDto courseDto = (CourseDto) targetDomainObject;
            String permissionValue = (String) permission;
            switch (permissionValue) {
                case "EXECUTION.CREATE":
                    return userService.getEnrolledCoursesAcronyms(userId).contains(courseDto.getAcronym() + courseDto.getAcademicTerm());
                case "DEMO.ACCESS":
                    return courseDto.getName().equals("Demo Course");
                default:
                    return false;
            }
        }

        if (targetDomainObject instanceof ClarificationDto) {
            ClarificationDto clarificationDto = (ClarificationDto) targetDomainObject;
            String permissionVal = (String) permission;
            switch (permissionVal) {
                case "CLARIFICATION.DEMO":
                    return clarificationDto.getDescription().equals("clarification description");
                default:
                    return false;
            }
        }


        if (targetDomainObject instanceof Integer) {
            int id = (int) targetDomainObject;
            String permissionValue = (String) permission;
            switch (permissionValue) {
                case "DEMO.ACCESS":
                    CourseDto courseDto = courseService.getCourseExecutionById(id);
                    return courseDto.getName().equals("Demo Course");
                case "COURSE.ACCESS":
                    return userHasAnExecutionOfTheCourse(userId, id);
                case "EXECUTION.ACCESS":
                    return userHasThisExecution(userId, id);
                case "QUESTION.ACCESS":
<<<<<<< HEAD
                    return userHasAnExecutionOfTheCourse(username, questionService.findQuestionCourse(id).getCourseId());
                case "QUESTION.ANSWERED":
                    return hasAnsweredQuestion(username, id);
=======
                    return userHasAnExecutionOfTheCourse(userId, questionService.findQuestionCourse(id).getCourseId());
>>>>>>> reference/master
                case "TOPIC.ACCESS":
                    return userHasAnExecutionOfTheCourse(userId, topicService.findTopicCourse(id).getCourseId());
                case "ASSESSMENT.ACCESS":
                    return userHasThisExecution(userId, assessmentService.findAssessmentCourseExecution(id).getCourseExecutionId());
                case "QUIZ.ACCESS":
<<<<<<< HEAD
                    return userHasThisExecution(username, quizService.findQuizCourseExecution(id).getCourseExecutionId());
                case "CLARIFICATION.CREATE":
                    return userHasAnExecutionOfTheCourse(username, questionService.findQuestionCourse(doubtService.getDoubtQuestion(id).getId()).getCourseId());
=======
                    return userHasThisExecution(userId, quizService.findQuizCourseExecution(id).getCourseExecutionId());
>>>>>>> reference/master
                default: return false;
            }
        }

        return false;
    }

<<<<<<< HEAD
    private boolean userHasAnExecutionOfTheCourse(String username, int id) {
        return userService.getCourseExecutions(username).stream().anyMatch(course -> course.getCourseId() == id);
    }


    boolean hasAnsweredQuestion(String username, int id){
        return userService.getAnsweredQuestions(username).stream().anyMatch(question -> question.getId() == id);
    }



    private boolean userHasThisExecution(String username, int id) {
        return userService.getCourseExecutions(username).stream()
                .anyMatch(course -> course.getCourseExecutionId() == id);
=======
    private boolean userHasAnExecutionOfTheCourse(int userId, int courseId) {
        return userService.getCourseExecutions(userId).stream()
                .anyMatch(course -> course.getCourseId() == courseId);
    }

    private boolean userHasThisExecution(int userId, int courseExecutionId) {
        return userService.getCourseExecutions(userId).stream()
                .anyMatch(course -> course.getCourseExecutionId() == courseExecutionId);
>>>>>>> reference/master
    }

     @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
