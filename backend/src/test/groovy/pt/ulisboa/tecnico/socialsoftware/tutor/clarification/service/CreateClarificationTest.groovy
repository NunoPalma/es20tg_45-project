package pt.ulisboa.tecnico.socialsoftware.tutor.clarification.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.*
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtRepositor
import spock.lang.Unroll
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
class CreateClarificationTest extends Specification {

    public static final String DOUBT_DESCRIPTION = "I don't understand why this option is not correct."

    public static final String CLARIFICATION_DESCRIPTION = "Your answer isn't correct because you need to watch the videos."
    public static final String CLARIFICATION_DESCRIPTION_EMPTY = " "

    public static final String USER_NAME = "name1"
    public static final String USER_NAME2 = "name2"
    public static final String USER_NAME3 = "name3"
    public static final String USER_USERNAME = "username1"
    public static final String USER_USERNAME2 = "username2"
    public static final String USER_USERNAME3 = "username3"
    public static final Integer USER_KEY = 1
    public static final Integer USER_KEY2 = 2
    public static final Integer USER_KEY3 = 3

    public static final String COURSE = "CourseOne"
    public static final String COURSE2 = "CourseTwo"
    public static final String ACRONYM = "C12"
    public static final String ACADEMIC_TERM = "1º Semestre"
    public static final String ACRONYM2 = "C13"
    public static final String ACADEMIC_TERM2 = "2º Semestre"

    public static final String QUESTION_TITLE = "question title"
    public static final String QUESTION_CONTENT = "question content"
    public static final Integer QUESTION_KEY = 1

    public static final String OPTION_CONTENT = "content"

    @Autowired
    ClarificationService clarificationService

    @Autowired
    ClarificationRepository clarificationRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    DoubtRepositor doubtRepository

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    @Autowired
    CourseRepository courseRepository

    def Teacher
    def TeacherTwo
    def Student
    def Question
    def Course
    def CourseTwo
    def CourseExecution
    def CourseExecutionTwo
    def SolvedDoubt
    def Doubt

    def setup() {

        Teacher = new User(USER_NAME, USER_USERNAME, USER_KEY, User.Role.TEACHER)
        userRepository.save(Teacher)

        Student = new User(USER_NAME2, USER_USERNAME2, USER_KEY2, User.Role.STUDENT)
        userRepository.save(Student)

        TeacherTwo = new User(USER_NAME3, USER_USERNAME3, USER_KEY3, User.Role.TEACHER)
        userRepository.save(TeacherTwo)

        Course = new Course(COURSE, Course.Type.TECNICO)
        courseRepository.save(Course)

        CourseTwo = new Course(COURSE2, Course.Type.TECNICO)
        courseRepository.save(CourseTwo)

        CourseExecution = new CourseExecution(Course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        CourseExecution.addUser(Teacher)
        Teacher.addCourse(CourseExecution)
        courseExecutionRepository.save(CourseExecution)

        CourseExecutionTwo = new CourseExecution(CourseTwo, ACRONYM2, ACADEMIC_TERM2, Course.Type.TECNICO)
        CourseExecutionTwo.addUser(TeacherTwo)
        TeacherTwo.addCourse(CourseExecutionTwo)
        courseExecutionRepository.save(CourseExecutionTwo)

        def questionDto = new QuestionDto()
        questionDto.setKey(QUESTION_KEY)
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())

        def optionDto = new OptionDto()
        optionDto.setContent(QUESTION_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.setOptions(options)

        Question = new Question(Course,questionDto)
        questionRepository.save(Question)

        Doubt = new Doubt(Question, Student, DOUBT_DESCRIPTION)
        doubtRepository.save(Doubt)

        SolvedDoubt = new Doubt(Question, Student, DOUBT_DESCRIPTION)
        SolvedDoubt.setStatus(Doubt.Status.SOLVED)
        doubtRepository.save(SolvedDoubt)

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
        insertedClarification.getAuthor().getRole() == User.Role.TEACHER
        insertedClarification.getAuthor().getClarifications().contains(insertedClarification)
        insertedClarification.getDoubt().getContent() == DOUBT_DESCRIPTION
        insertedClarification.getDoubt().getStatus() == Doubt.Status.SOLVED
        insertedClarification.getDoubt().getClarification() == insertedClarification

    }


    @Unroll
    def "invalid arguments: userid =#userid | questionid =#questionid || errormessage =#errormessage"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, userid, doubtid)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        userid      | doubtid     || errorMessage
        null        | 9           || CLARIFICATION_USER_IS_EMPTY
        13          | null        || CLARIFICATION_DOUBT_IS_EMPTY
        9000        | 5           || USER_NOT_FOUND
        13          | 9000        || DOUBT_NOT_FOUND

    }



    @Unroll
    def "clarification description is #description"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(description)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), Doubt.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == errorMessage

        where:
        description || errorMessage
        CLARIFICATION_DESCRIPTION_EMPTY|| CLARIFICATION_EMPTY
        null || CLARIFICATION_EMPTY
    }


    def "clarification owner is not a teacher"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Student.getId(), Doubt.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == CLARIFICATION_INVALID_USER
    }


    def "clarification for a solved clarification request"() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, Teacher.getId(), SolvedDoubt.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == CLARIFICATION_NOT_ALLOWED

    }


    def "clarification made by a teacher whose course execution isn't the same as question's course "() {

        given: "a clarificationDto"
        def clarificationDto = new ClarificationDto()
        clarificationDto.setDescription(CLARIFICATION_DESCRIPTION)

        when:
        clarificationService.createClarification(clarificationDto, TeacherTwo.getId(), Doubt.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == CLARIFICATION_INVALID_COURSE_TEACHER
    }

    @TestConfiguration
    static class ClarificationServiceImplTestContextConfiguration {

        @Bean
        ClarificationService clarificationService() {
            return new ClarificationService()
        }

    }

}


