package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.CANNOT_SUBMIT
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_CONTENT_FOR_OPTION
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_CONTENT_FOR_QUESTION
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_CONTENT_FOR_QUESTION
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_TITLE_FOR_QUESTION
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_TITLE_FOR_QUESTION
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.ONE_CORRECT_OPTION_NEEDED
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_MISSING_DATA
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_MULTIPLE_CORRECT_OPTIONS

@DataJpaTest
class SubmitQuestionTest extends Specification {

    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"

    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final String OPTION_CONTENT = "optionId content1"

    public static final String STUDENT_NAME = "Student Name"
    public static final String STUDENT_USERNAME = "StudentUsername"
    public static final Integer STUDENT_KEY = 1

    @Autowired
    QuestionService questionService

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    def course
    def student

    def setup() {
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        student = new User(STUDENT_NAME, STUDENT_USERNAME, STUDENT_KEY, User.Role.STUDENT)
        userRepository.save(student)
    }

    def "submitted question must be pending and created"() {
        given: "A question"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.PENDING.name())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.setOptions(options)

        when:
        questionService.submitQuestion(student.getId(), course.getId(), questionDto)

        then:
        questionRepository.count() == 1L
        def submittedQuestion = questionRepository.findAll().get(0)
        submittedQuestion.getId() != null
        submittedQuestion.getKey() == 1
        submittedQuestion.getStatus() == Question.Status.PENDING
        submittedQuestion.getTitle() == QUESTION_TITLE
        submittedQuestion.getContent() == QUESTION_CONTENT
        submittedQuestion.getImage() == null
        submittedQuestion.getOptions().size() == 1
        submittedQuestion.getCourse().getName() == COURSE_NAME
        course.getQuestions().contains(submittedQuestion)
    }

    def "user contains submitted question"() {
        given: "A question"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.PENDING.name())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.setOptions(options)

        when:
        questionService.submitQuestion(student.getId(), course.getId(), questionDto)

        then:
        questionRepository.count() == 1L
        def submittedQuestion = questionRepository.findAll().get(0)
        def submittedQuestions = student.getSubmittedQuestions()
        submittedQuestions.contains(submittedQuestion)
    }

    def "submitted question must not be a null object"() {
        given: "A question"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.PENDING.name())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.setOptions(options)

        questionService.submitQuestion(student.getId(), course.getId(), questionDto)
        def submittedQuestion = questionRepository.findAll().get(0)

        when:
        submittedQuestion != null

        then:
        true
    }

    def "cannot submit if 3 rejected, pending also counts"(){
        given: "A question1"
        def questionDto1 = new QuestionDto()
        questionDto1.setKey(1)
        questionDto1.setTitle(QUESTION_TITLE)
        questionDto1.setContent(QUESTION_CONTENT)
        questionDto1.setStatus(Question.Status.PENDING.name())
        def questionDto2 = new QuestionDto()
        questionDto2.setKey(2)
        questionDto2.setTitle(QUESTION_TITLE)
        questionDto2.setContent(QUESTION_CONTENT)
        questionDto2.setStatus(Question.Status.PENDING.name())
        def questionDto3 = new QuestionDto()
        questionDto3.setKey(3)
        questionDto3.setTitle(QUESTION_TITLE)
        questionDto3.setContent(QUESTION_CONTENT)
        questionDto3.setStatus(Question.Status.PENDING.name())
        def questionDto4 = new QuestionDto()
        questionDto4.setKey(4)
        questionDto4.setTitle(QUESTION_TITLE)
        questionDto4.setContent(QUESTION_CONTENT)
        questionDto4.setStatus(Question.Status.PENDING.name())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(true)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto1.setOptions(options)
        questionDto2.setOptions(options)
        questionDto3.setOptions(options)
        questionDto4.setOptions(options)
        questionService.submitQuestion(student.getId(), course.getId(), questionDto1)
        questionService.submitQuestion(student.getId(), course.getId(), questionDto2)
        questionService.submitQuestion(student.getId(), course.getId(), questionDto3)

        when:
        questionService.submitQuestion(student.getId(), course.getId(), questionDto4)

        then:"a TutorException is thrown"
        def error = thrown(TutorException)
        error.errorMessage == CANNOT_SUBMIT
    }

    @Unroll("invalid arguments: #questionTitle | #questionContent | #optionContent1 | #correctOption1 || errorMessage ")
    def "invalid input values"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(questionTitle)
        questionDto.setContent(questionContent)

        and: 'an option'
        def optionDto1 = new OptionDto()
        optionDto1.setContent(optionContent1)
        optionDto1.setCorrect(correctOption1)
        def options1 = new ArrayList<OptionDto>()
        options1.add(optionDto1)
        questionDto.setOptions(options1)

        when:
        questionService.submitQuestion(student.getId(), course.getId(), questionDto)

        then: "a TutorException is thrown"
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        questionTitle   | questionContent   | optionContent1 | correctOption1 || errorMessage
        null            | QUESTION_CONTENT  | OPTION_CONTENT |      true      || INVALID_TITLE_FOR_QUESTION
        ""              | QUESTION_CONTENT  | OPTION_CONTENT |      true      || INVALID_TITLE_FOR_QUESTION
        QUESTION_TITLE  |       null        | OPTION_CONTENT |      true      || INVALID_CONTENT_FOR_QUESTION
        QUESTION_TITLE  |        ""         | OPTION_CONTENT |      true      || INVALID_CONTENT_FOR_QUESTION
        QUESTION_TITLE  | QUESTION_CONTENT  |       ""       |      true      || INVALID_CONTENT_FOR_OPTION
        QUESTION_TITLE  | QUESTION_CONTENT  | OPTION_CONTENT |      false     || ONE_CORRECT_OPTION_NEEDED
    }

    @TestConfiguration
    static class QuestionServiceImplTestContextConfiguration {

        @Bean
        QuestionService questionService() {
            return new QuestionService()
        }
    }
}
