package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification

@DataJpaTest
class SubmitQuestionTest extends Specification {

    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"
    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final String OPTION_CONTENT = "optionId content"
    public static final String STUDENT_NAME = "Student Name"
    public static final String STUDENT_USERNAME = "StudentUsername"
    public static final Integer STUDENT_KEY = 1


    @Autowired
    QuestionService questionService

    @Autowired
    Course course

    @Autowired
    User student

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    CourseExecution courseExecution

    @Autowired
    CourseExecutionRepository courseExecutionRepository



    def setup() {
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)

        student = new User(STUDENT_NAME, STUDENT_USERNAME, STUDENT_KEY, User.Role.STUDENT)
        student.setId(STUDENT_KEY)

    }

    def "submitted question must be pending and created"() {
        given: "A question"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_CONTENT)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        questionDto.setOptions(options)

        when:
        questionService.submitQuestion(student.getKey(), course.getId(), questionDto)

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

        when:
        questionService.submitQuestion(student.getKey(), course.getId(), questionDto)

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

        setup:
        questionService.submitQuestion(student.getKey(), course.getId(), questionDto)
        def submittedQuestion = questionRepository.findAll().get(0)

        when:
        submittedQuestion == null

        then:
        InvalidObjectException exception = thrown()
        exception.message("Submitted question failed. Object is invalid")
    }


    @TestConfiguration
    static class QuestionServiceImplTestContextConfiguration {

        @Bean
        QuestionService questionService() {
            return new QuestionService()
        }
    }
}
