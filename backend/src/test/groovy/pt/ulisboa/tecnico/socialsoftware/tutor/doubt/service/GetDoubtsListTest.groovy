package pt.ulisboa.tecnico.socialsoftware.tutor.doubt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtRepositor

import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
// files should have the same name as the class
class GetDoubtsListTest extends Specification {
    public static final String COURSE_NAME = "Software Architecture"
    public static final String USER_NAME = "user"
    public static final String USERNAME_NAME = "username"
    public static final Integer USER_KEY = 90000
    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final Integer QUESTION_KEY = 3
    public static final String DOUBT_CONTENT = 'doubt content'


    @Autowired
    DoubtService doubtService

    @Autowired
    UserRepository userRepository

    @Autowired
    DoubtRepositor doubtRepositor

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    CourseRepository courseRepository

    def student
    def question
    def questiondto
    def optiondto
    def options
    def course

    def setup(){
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        student  = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        userRepository.save(student)

        questiondto = new QuestionDto()
        questiondto.setKey(QUESTION_KEY)
        questiondto.setTitle(QUESTION_TITLE)
        questiondto.setContent(QUESTION_CONTENT)
        questiondto.setStatus(Question.Status.AVAILABLE.name())

        optiondto = new OptionDto()
        optiondto.setContent(QUESTION_CONTENT)
        optiondto.setCorrect(true)

        options = new ArrayList<OptionDto>()
        options.add(optiondto)

        questiondto.setOptions(options)
        question = new Question(course, questiondto)
        questionRepository.save(question)
    }

    def "Get the doubt list of a user that has no doubts"(){

        when:
        def result = doubtService.findUserDoubts(student.getId())

        then:
        result.size() == 0
    }

    def "Get the doubt list of a null user"(){

        when:
        def result = doubtService.findUserDoubts(null)

        then:
        def error = thrown(TutorException)
        error.errorMessage == DOUBT_USER_IS_EMPTY // this error could be named better? why not invalid user?

    }

    def "Get the doubt list of a user"(){

        given: "A doubt"
        def doubt = new Doubt(question, student, DOUBT_CONTENT)
        doubtRepositor.save(doubt)

        when:
        def result = doubtService.findUserDoubts(student.getId())

        then:
        result.size() == 1
        def newDoubt = result.get(0);
        newDoubt.getAuthor() == student.getName()
        newDoubt.getContent() == DOUBT_CONTENT
    }



    @TestConfiguration
    static class DoubtServiceImplTestContextConfiguration {

        @Bean
        DoubtService doubtService() {
            return new DoubtService()
        }
    }


}
