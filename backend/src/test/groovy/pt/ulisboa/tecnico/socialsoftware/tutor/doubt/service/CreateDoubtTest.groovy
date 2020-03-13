package pt.ulisboa.tecnico.socialsoftware.tutor.doubt.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtDto
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import spock.lang.Specification

@DataJpaTest
class CreateDoubtTest extends Specification {
    public static final String COURSE_NAME = "Software Architecture"
    public static final String COURSE2_NAME = "Distributed Systems"
    public static final String COURSE_EXEC_ACRNOYM = "ES"
    public static final String COURSE2_EXEC_ACRNOYM = "SDIS"
    public static final String COURSE_EXEC_TERM = "2019-2020"
    public static final String COURSE2_EXEC_TERM = "2019-2020"
    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION2_TITLE = 'question2 title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final String QUESTION2_CONTENT = 'question2 content'
    public static final Integer QUESTION_ID = 42
    public static final Integer QUESTION2_ID = 24
    public static final Integer QUESTION_KEY = 52
    public static final Integer QUESTION2_KEY = 54
    public static final String USER_NAME = "user"
    public static final String USERNAME_NAME = "username"
    public static final int USER_KEY = "90000"
    public static final String DOUBT_CONTENT = "doubt content"
    public static final String DOUBT2_CONTENT = "doubt2 content"




    @Autowired
    DoubtService doubtService

    @Autowired
    DoubtRepository doubtRepository

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    UserRepository userRepository

    def question
    def question2
    def course
    def course2
    def courseExecution
    def courseExecution2
    def courseSet
    def quiz

    def setup() {
        quiz =
        doubtService = new DoubtService()
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        course2 = new Course(COURSE2_NAME, Course.Type.TECNICO)
        courseExecution = new CourseExecution(course, COURSE_EXEC_ACRNOYM, COURSE_EXEC_TERM, Course.Type.TECNICO)
        courseExecution2 = new CourseExecution(course2, COURSE2_EXEC_ACRNOYM, COURSE2_EXEC_ACRNOYM, Course.Type.TECNICO)
        question = new Question()
        question.setId(QUESTION_ID)
        question.setKey(QUESTION_KEY)
        question.setContent(QUESTION_CONTENT);
        question.setStatus(Question.Status.AVAILABLE)
        question.setTitle(QUESTION_TITLE)
        question.setCourse(course)
        question2 = new Question()
        question2.setId(QUESTION2_ID)
        question2.setKey(QUESTION2_KEY)
        question2.setContent(QUESTION2_CONTENT)
        question2.setStatus(Question.Status.AVAILABLE)
        question2.setTitle(QUESTION2_TITLE)
        question2.setCourse(course2)
        course.addQuestion(question)
        course2.addQuestion(question2)
        questionRepository.save(question)
        questionRepository.save(question2)
        courseSet = new HashSet<CourseExecution>()
    }

    def "create a Doubt with a User, Content and a Question"() {
        given: "a Student"
        def student = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        courseSet.add(courseExecution)
        student.setCourseExecutions(courseSet)
        userRepository.save(student)
        and: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(DOUBT_CONTENT)

        when: "A doubt is created"
        doubtService.createDoubt(doubtdto, QUESTION_ID, student.getId())

        then:
        doubtRepository.count() == 1L
        def result = doubtRepository.findAll().get(0)

        result.getId() != null
        result.getContent() == DOUBT_CONTENT
        result.getAuthor().getName() == USER_NAME
        result.getAuthor().getId().getRole() == User.Role.STUDENT
        Question.getDoubts().get(0).equals(insertedClarification)
    }

    def "create a Doubt without a User"() {
        //Throws an exception
        given: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(DOUBT_CONTENT)

        when: "A doubt is created"
        doubtService.createDoubt(doubtdto ,QUESTION_ID, null)

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.DOUBT_USER_IS_EMPTY
    }

    def "create a Doubt without Content"() {
        //Throws an exception
        given: "a Student"
        def student = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        courseSet.add(courseExecution)
        student.setCourseExecutions(courseSet)
        userRepository.save(student)
        and: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(null)

        when: "A doubt is created"
        doubtService.createDoubt(doubtdto, QUESTION_ID, student.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.DOUBT_CONTENT_IS_EMPTY
    }

    def "create a Doubt without Question"(){
        //Throws an exception
        given: "a Student"
        def student = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        courseSet.add(courseExecution)
        student.setCourseExecutions(courseSet)
        userRepository.save(student)
        and: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(DOUBT_CONTENT)

        when: "A doubt is created"
        doubtService.createDoubt(doubtdto, null, student.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.DOUBT_QUESTION_IS_EMPTY
    }

    def "create a Doubt but the User is not a Student"() {
        given: "a Teacher"
        def teacher = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.TEACHER)
        courseSet.add(courseExecution)
        teacher.setCourseExecutions(courseSet)
        userRepository.save(teacher)
        and: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(DOUBT_CONTENT)

        when: "A doubt is created"
        doubtService.createDoubt(doubtdto, QUESTION_ID, teacher.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.DOUBT_USER_IS_NOT_A_STUDENT
    }

    def "create a Doubt where the User's Course Execution doesn't match the Question's"() {
        given: "a Student"
        def student = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        courseSet.add(courseExecution)
        student.setCourseExecutions(courseSet)
        and: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(DOUBT_CONTENT)

        when:"A doubt is created"
        doubtService.createDoubt(doubtdto, QUESTION2_ID, student.getId())

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.DOUBT_COURSE_EXECUTION_DOESNT_MATCH
    }

    def "create two Doubts with a User, Content, Question"() {
        given: "a Student"
        def student = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        courseSet.add(courseExecution)
        courseSet.add(courseExecution2)
        student.setCourseExecutions(courseSet)
        and: "a DoubtDto"
        def doubtdto = new DoubtDto()
        def doubtdto2 = new DoubtDto()
        doubtdto.setContent(DOUBT_CONTENT)
        doubtdto.setContent(DOUBT2_CONTENT)

        when:"The doubts are created"
        doubtService.createDoubt(doubtdto, QUESTION_ID, student.getId())
        doubtService.createDoubt(doubtdto, QUESTION2_ID, student.getId())

        then:
        doubtRepository.count() == 2L
        def result = doubtRepository.findAll().get(0)
        result.getId() != null
        result.getContent() == DOUBT_CONTENT
        result.getAuthor().getName() == USER_NAME
        result.getAuthor().getId().getRole() == User.Role.STUDENT
        question.getDoubts().get(0).equals(result)
        student.getDoubts().get(0).equals(result)
        def result2 = doubtRepository.findAll().get(1)
        result2.getId() != null
        result2.getContent() == DOUBT2_CONTENT
        result2.getAuthor().getName() == USER_NAME
        result2.getAuthor().getId().getRole() == User.Role.STUDENT
        question2.getDoubts().get(0).equals(result2)
        student.getDoubts().get(1).equals(result2)
        student.getDoubts().size() == 2

    }

    @TestConfiguration
    static class DoubtServiceImplTestContextConfiguration {

        @Bean
        DoubtService doubtService() {
            return new DoubtService()
        }
    }

}
