package pt.ulisboa.tecnico.socialsoftware.tutor.user;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.QuizAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.config.TutorPermissionEvaluator;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.UsersXmlExport;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.UsersXmlImport;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;

import java.sql.SQLException;
<<<<<<< HEAD
import java.util.*;
=======
import java.time.LocalDateTime;
import java.util.List;
>>>>>>> reference/master
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

<<<<<<< HEAD
    @Autowired
    private QuestionService questionService;

    @PersistenceContext
    EntityManager entityManager;

=======
>>>>>>> reference/master
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User findByKey(Integer key) {
        return this.userRepository.findByKey(key);
    }

    public Integer getMaxUserNumber() {
        Integer result = userRepository.getMaxUserNumber();
        return result != null ? result : 0;
    }

    public User createUser(String name, String username, User.Role role) {

        if (findByUsername(username) != null) {
            throw new TutorException(DUPLICATE_USER, username);
        }

        User user = new User(name, username, getMaxUserNumber() + 1, role);
        userRepository.save(user);
        return user;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String getEnrolledCoursesAcronyms(String username) {
        User user =  this.userRepository.findByUsername(username);

        return user.getEnrolledCoursesAcronyms();
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Set<QuestionDto> getAnsweredQuestions(String username){
        User user = this.userRepository.findByUsername(username);
        Set<QuizAnswer> quizAnswers = user.getQuizAnswers();
        if(quizAnswers == null || quizAnswers.isEmpty()){
            return new HashSet<>();
        }
        Set<QuestionAnswer> questionAnswerList = new HashSet<>();
        for(QuizAnswer quizAnswer: quizAnswers){
            questionAnswerList.addAll(quizAnswer.getQuestionAnswers());
        }
        return questionAnswerList.stream().map(QuestionAnswer::getQuizQuestion)
                .map(QuizQuestion::getQuestion).map(QuestionDto::new).collect(Collectors.toSet());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<CourseDto> getCourseExecutions(String username) {
        User user =  this.userRepository.findByUsername(username);

        return user.getCourseExecutions().stream().map(CourseDto::new).collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void addCourseExecution(String username, int executionId) {

        User user =  this.userRepository.findByUsername(username);

        CourseExecution courseExecution = courseExecutionRepository.findById(executionId).orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND, executionId));

        user.addCourse(courseExecution);
        courseExecution.addUser(user);
    }

    public String exportUsers() {
        UsersXmlExport xmlExporter = new UsersXmlExport();

       return xmlExporter.export(userRepository.findAll());
    }


    @Retryable(
      value = { SQLException.class },
      backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void importUsers(String usersXML) {
        UsersXmlImport xmlImporter = new UsersXmlImport();

        xmlImporter.importUsers(usersXML, this);
    }

<<<<<<< HEAD


=======
    @Transactional(isolation = Isolation.REPEATABLE_READ)
>>>>>>> reference/master
    public User getDemoTeacher() {
        User user = this.userRepository.findByUsername("Demo-Teacher");
        if (user == null)
            return createUser("Demo Teacher", "Demo-Teacher", User.Role.TEACHER);
        return user;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User getDemoStudent() {
        User user = this.userRepository.findByUsername("Demo-Student");
        if (user == null)
            return createUser("Demo Student", "Demo-Student", User.Role.STUDENT);
        return user;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User getDemoAdmin() {
        User user =  this.userRepository.findByUsername("Demo-Admin");
        if (user == null)
            return createUser("Demo Admin", "Demo-Admin", User.Role.DEMO_ADMIN);
        return user;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public User createDemoStudent() {
        String birthDate = LocalDateTime.now().toString();
        User newDemoUser = createUser("Demo-Student-" + birthDate, "Demo-Student-" + birthDate, User.Role.STUDENT);

        User demoUser = this.userRepository.findByUsername("Demo-Student");

        CourseExecution courseExecution = demoUser.getCourseExecutions().stream().findAny().orElse(null);

        if (courseExecution != null) {
            courseExecution.addUser(newDemoUser);
            newDemoUser.addCourse(courseExecution);
        }

        return newDemoUser;
    }
}
