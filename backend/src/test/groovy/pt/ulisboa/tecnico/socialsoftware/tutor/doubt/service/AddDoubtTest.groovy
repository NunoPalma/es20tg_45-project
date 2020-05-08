package pt.ulisboa.tecnico.socialsoftware.tutor.doubt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuizAnswerRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.Clarification
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationDto
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationService
import pt.ulisboa.tecnico.socialsoftware.tutor.config.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Discussion
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DiscussionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DiscussionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtDto
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtRepositor
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DISCUSSION_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DISCUSSION_NOT_FOUND
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DISCUSSION_USER_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DISCUSSION_USER_IS_NOT_A_STUDENT
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_CONTENT_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_CONTENT_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_QUESTION_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_USER_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DOUBT_USER_IS_NOT_A_STUDENT
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_NOT_FOUND
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.USER_NOT_FOUND

@DataJpaTest
class AddDoubtTest extends Specification {
    public static final String COURSE_NAME = "Software Architecture"
    public static final String COURSE2_NAME = "Distributed Systems"
    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION2_TITLE = 'question2 title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final String QUESTION2_CONTENT = 'question2 content'
    public static final String QUIZ_TITLE = "quiz title"
    public static final Integer QUIZ_SERIES = 1
    public static final Integer QUIZ_KEY = 2
    public static final Integer QUESTION_KEY = 52
    public static final Integer QUESTION2_KEY = 54
    public static final String USER_NAME = "user"
    public static final String USERNAME_NAME = "username"
    public static final String TEACHER_NAME = "teacher"
    public static final String TEACHER_USERNAME = "teacher username"
    public static final Integer TEACHER_KEY = 3
    public static final Integer USER_KEY = 90000
    public static final String DOUBT_CONTENT = "doubt content"
    public static final String DOUBT_TITLE = "doubt title"
    public static final String DOUBT2_CONTENT = "doubt2 content"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"



    @Autowired
    ClarificationRepository clarificationRepository

    @Autowired
    DoubtService doubtService

    @Autowired
    DoubtRepositor doubtRepository

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    UserRepository userRepository


    @Autowired
    QuizRepository quizRepository

    @Autowired
    QuizQuestionRepository quizQuestionRepository

    @Autowired
    CourseRepository courseRepository

    @Autowired
    QuizAnswerRepository quizAnswerRepository

    @Autowired
    QuestionAnswerRepository questionAnswerRepository

    @Autowired
    ClarificationService clarificationService

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    @Autowired
    DiscussionRepository discussionRepository

    def question
    def question2
    def questiondto
    def questiondto2
    def optiondto
    def course
    def course2
    def quiz
    def quiz2
    def quizdto
    def teacher
    def student
    def options
    def quizquestion
    def quizquestion2
    def quizanswer
    def quizanswer2
    def questionanswer
    def questionanswer2
    def courseExecution
    def courseExecution2
    def doubt
    def clarification
    def discussion
    def clarificationdto


    def setup() {
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        course2 = new Course(COURSE2_NAME, Course.Type.TECNICO)
        courseRepository.save(course)
        courseRepository.save(course2)


        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)

        courseExecution2 = new CourseExecution(course2, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution2)

        quiz = new Quiz()
        quiz.setKey(QUIZ_KEY)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(courseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quiz2 = new Quiz()
        quiz2.setKey(QUIZ_KEY)
        quiz2.setType(Quiz.QuizType.GENERATED.toString())
        quiz2.setCourseExecution(courseExecution)
        quiz2.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)
        quizRepository.save(quiz2)

        student  = new User(USER_NAME, USERNAME_NAME, USER_KEY, User.Role.STUDENT)
        student.courseExecutions.add(courseExecution)
        teacher = new User(TEACHER_NAME, TEACHER_USERNAME, TEACHER_KEY, User.Role.TEACHER)
        teacher.courseExecutions.add(courseExecution)
        userRepository.save(student)
        userRepository.save(teacher)

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

        questiondto2 = new QuestionDto()
        questiondto2.setKey(QUESTION2_KEY)
        questiondto2.setTitle(QUESTION2_TITLE)
        questiondto2.setContent(QUESTION2_CONTENT)
        questiondto2.setStatus(Question.Status.AVAILABLE.name())

        questiondto2.setOptions(options)

        question = new Question(course, questiondto)
        question2 = new Question(course2, questiondto2)

        questionRepository.save(question)
        questionRepository.save(question2)

        quizanswer = new QuizAnswer(student, quiz)
        quizanswer2 = new QuizAnswer(student, quiz2)
        student.addQuizAnswer(quizanswer)

        quizquestion = new QuizQuestion(quiz, question, 1)
        questionanswer = new QuestionAnswer(quizanswer, quizquestion, 30);
        quizanswer.addQuestionAnswer(questionanswer);
        quizquestion.addQuestionAnswer(questionanswer);

        quizquestion2 = new QuizQuestion(quiz, question2, 2)
        questionanswer2 = new QuestionAnswer(quizanswer2, quizquestion2, 60)
        quizanswer2.addQuestionAnswer(questionanswer2)
        quizquestion2.addQuestionAnswer(questionanswer2)

        quizQuestionRepository.save(quizquestion)
        quizQuestionRepository.save(quizquestion2)
        questionAnswerRepository.save(questionanswer)
        questionAnswerRepository.save(questionanswer2)
        quizAnswerRepository.save(quizanswer)
        quizAnswerRepository.save(quizanswer2)

        discussion = new Discussion(questionanswer, DOUBT_TITLE, student)
        doubt = new Doubt(student,DateHandler.now().toString(),"OLA",true,discussion)
        discussion.addPost(doubt)
        discussionRepository.save(discussion)
        doubtRepository.save(doubt)
        clarificationdto = new ClarificationDto()
        clarificationdto.setDescription(DOUBT_CONTENT)
        clarificationdto.setAuthor(teacher.getName())
        clarification = new Clarification(teacher,doubt,clarificationdto)
        clarificationRepository.save(clarification)

    }

    def "add a Doubt to a discussion"() {
        given: "a DoubtDto and DiscussionDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent("Gil")
        doubtdto.setAuthor(USER_NAME)

        when: "A discussion is created"
        doubtService.addDoubt(discussion.getId(),student.getId(),doubtdto)

        then:
        discussionRepository.count() == 1L
        def result = discussionRepository.findAll().get(0)

        result.getId() != null
        def getPostsList = new ArrayList<Doubt>(result.getPosts())
        getPostsList.size() == 2
    }


    @Unroll
    def "invalid arguments: userid =#userid | discussionid =#discussionid || errormessage =#errormessage"(){
        given: "a DoubtDto and DiscussionDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent("Gil")
        doubtdto.setAuthor(USER_NAME)

        when: "A doubt is added"
        doubtService.addDoubt(discussionid, userid, doubtdto)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        userid | discussionid || errorMessage
        null   |   3          || DISCUSSION_USER_IS_EMPTY
        5      | null         || DISCUSSION_IS_EMPTY

    }



    @Unroll
    def "invalid data in database where content is #content"(){
        given: "a DoubtDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent(content)
        def discussionDto = new DiscussionDto()
        discussionDto.addPostDto(doubtdto)
        discussionDto.setQuestionTitle(QUESTION_TITLE)
        discussionDto.setStatus(Discussion.Status.OPEN)
        discussionDto.setTitle(DOUBT_TITLE)

        when: "A doubt is created"
        doubtService.createDiscussion(discussionDto, questionanswer.getId(), student.getId())

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        content || errorMessage
        "     " || DOUBT_CONTENT_IS_EMPTY
        null    || DOUBT_CONTENT_IS_EMPTY

    }


    def "add a Doubt but the User is not a Student"() {
        given: "a DoubtDto and DiscussionDto"
        def doubtdto = new DoubtDto()
        doubtdto.setContent("Gil")
        doubtdto.setAuthor(TEACHER_NAME)


        when: "A doubt is created"
        doubtService.addDoubt(discussion.getId(), teacher.getId(), doubtdto)

        then:
        def exception = thrown(TutorException)
        exception.errorMessage == DISCUSSION_USER_IS_NOT_A_STUDENT
    }



    @TestConfiguration
    static class DoubtServiceImplTestContextConfiguration {

        @Bean
        DoubtService doubtService() {
            return new DoubtService()
        }

        @Bean
        ClarificationService clarificationService() {
            return new ClarificationService()
        }
    }

}
