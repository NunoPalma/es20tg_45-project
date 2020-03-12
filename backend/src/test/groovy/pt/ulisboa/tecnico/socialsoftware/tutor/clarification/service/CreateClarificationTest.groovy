package pt.ulisboa.tecnico.socialsoftware.tutor.clarification.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserService
import spock.lang.Specification
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.*

@DataJpaTest
class CreateClarificationTest extends Specification {

    public static final String DOUBT_DESCRIPTION = "I don't understand why this option is not correct."
    public static final String CLARIFICATION_DESCRIPTION = "Your answer isn't correct because you need to watch the videos."
    public static final String CLARIFICATION_DESCRIPTION_EMPTY = " "

    public static final String USER_NAME = "Eduardo"
    public static final String USER_USERNAME = "Eduardo50"
    public static final Integer USER_KEY = 10

    public static final String COURSE_ONE = "CourseOne"
    public static final String COURSE_TWO = "CourseTwo"
    public static final String ACRONYM_ONE = "C12"
    public static final String ACADEMIC_TERM_ONE = "1º Semestre"

    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION_CONTENT = 'question content'

    @Autowired
    ClarificationService clarificationService

    @Autowired
    ClarificationRepository clarificationRepository

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    @Autowired
    DoubtService doubtService

    @Autowired
    DoubtRepository doubtRepository

    @Autowired
    QuestionService questionService

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    @Autowired
    CourseRepository courseRepository

    def Teacher
    def Student
    def Doubt
    def SolvedDoubt

    def setup() {

        Teacher = new User(USER_NAME, USER_USERNAME, USER_KEY, User.Role.TEACHER)
        userRepository.save(Teacher)
        Student = new User(USER_NAME, USER_USERNAME, USER_KEY, User.Role.STUDENT)
        userRepository.save(Student)
        Doubt = new Doubt(new Question(), Student, DOUBT_DESCRIPTION)
        SolvedDoubt = new Doubt(new Question(), Student, DOUBT_DESCRIPTION)

    }


    def "create a clarification"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), Doubt.getId())

        then: "the correct clarification is successfully added to the repository"
        clarificationRepository.count() == 1L
        def insertedClarification = clarificationRepository.findAll().get(0)

        insertedClarification.getId() != null
        insertedClarification.getClarification() == CLARIFICATION_DESCRIPTION
        insertedClarification.getAuthor().getName() == USER_NAME
        insertedClarification.getDoubt().getDescr() == DOUBT_DESCRIPTION
        insertedClarification.getDoubt().getStatus() == Doubt.Status.SOLVED
        insertedClarification.getAuthor().getRole() == User.Role.TEACHER
        Doubt.getClarification().equals(insertedClarification)

    }


    def "clarification description is empty"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION_EMPTY)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), Doubt.getId())

        then:
        thrown(TutorException)
    }


    def "clarification owner is not a teacher"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Student.getId(), Doubt.getId())

        then:
        thrown(TutorException)
    }

    def "clarification owner is a teacher"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), Doubt.getId())

        then: "the owner is a teacher"
        def insertedClarification = clarificationRepository.findAll().get(0)
        insertedClarification.getAuthor().getRole() == User.Role.TEACHER

    }

    def "clarification for a solved clarification request"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), SolvedDoubt.getId())

        then:
        thrown(TutorException)

    }


    def "clarification made by a teacher whose course execution isn't the same as question's course "() {

        given: "two courses"
        def courseOne = new Course(COURSE_ONE, Course.Type.TECNICO)
        courseRepository.save(courseOne)
        def courseTwo = new Course(COURSE_TWO, Course.Type.TECNICO)
        courseRepository.save(courseTwo)
        and: "a execution course"
        def CourseExecution = new CourseExecution(courseOne, ACRONYM_ONE, ACADEMIC_TERM_ONE, Course.Type.TECNICO)
        CourseExecution.addUser(Teacher)
        courseExecutionRepository.save(CourseExecution)
        and: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        def question = questionService.createQuestion(courseTwo.getId(), questionDto)
        and: "a doubt"
        def doubt = new Doubt(question, Student, DOUBT_DESCRIPTION)
        doubtRepository.save(doubt)
        and: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), doubt.getId())

        then:
        thrown(TutorException)
    }

}


