package pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.user

import jdk.nashorn.internal.runtime.Specialization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserService

import java.time.LocalDateTime

@DataJpaTest
class FilterStudentQuestionByDateTest extends Specialization{
    public static final String QUESTION_TITLE_1 = 'question title1'
    public static final String QUESTION_TITLE_2 = 'question title2'
    public static final String QUESTION_TITLE_3 = 'question title3'
    public static final String STUDENT_NAME = 'student name'
    public static final String STUDENT_USERNAME = 'student username'
    public static final Integer STUDENT_KEY = 1

    @Autowired
    QuestionService questionService

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    def "three questions"() {
        //given a user with 3 submitted questions the returned data must be correctly sorted
        given: "3 questions"
        def submittedQuestion1 = new Question()
        def submittedQuestion2 = new Question()
        def submittedQuestion3 = new Question()
        submittedQuestion1.setTitle(QUESTION_TITLE_1)
        submittedQuestion2.setTitle(QUESTION_TITLE_2)
        submittedQuestion3.setTitle(QUESTION_TITLE_3)
        submittedQuestion1.setCreationDate(LocalDateTime.now())
        submittedQuestion2.setCreationDate(LocalDateTime.now())
        submittedQuestion3.setCreationDate(LocalDateTime.now())
        and: "A user"
        def user = new User(STUDENT_NAME, STUDENT_USERNAME, STUDENT_KEY, User.Role.STUDENT)
        userRepository.save(user)

        when:
        userService.sortStudentSubmittedQuestions(STUDENT_USERNAME)

        then:
        def sortedSubmittedQuestions = user.getSubmittedQuestions()
        sortedSubmittedQuestions[0].getTitle() == QUESTION_TITLE_3
        sortedSubmittedQuestions[1].getTitle() == QUESTION_TITLE_2
        sortedSubmittedQuestions[2].getTitle() == QUESTION_TITLE_1
    }

    def "no questions"() {
        //check if returned data from user with no submitted questions is correct
        given: "A user"
        def user = new User(STUDENT_NAME, STUDENT_USERNAME, STUDENT_KEY, User.Role.STUDENT)

        when:
        userService.sortStudentSubmittedQuestions(STUDENT_USERNAME)

        then: "the returned data are correct"
        def sortedSubmittedQuestions = user.getSubmittedQuestions()
        sortedSubmittedQuestions.size() == 0
    }
}
