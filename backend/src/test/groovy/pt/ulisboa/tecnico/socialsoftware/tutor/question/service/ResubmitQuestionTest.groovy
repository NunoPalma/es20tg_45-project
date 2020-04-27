package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Image
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.ImageRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.OptionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_MISSING_DATA
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_MULTIPLE_CORRECT_OPTIONS

@DataJpaTest
class ResubmitQuestionTest extends Specification {
    public static final String QUESTION_TITLE = 'question title'
    public static final String QUESTION_CONTENT = 'question content'
    public static final String OPTION_CONTENT = "optionId content"
    public static final String NEW_QUESTION_TITLE = 'new question title'
    public static final String NEW_QUESTION_CONTENT = 'new question content'
    public static final String NEW_OPTION_CONTENT = "new optionId content"
    public static final String URL = 'URL'

    @Autowired
    QuestionService questionService

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    OptionRepository optionRepository

    @Autowired
    ImageRepository imageRepository

    @Autowired
    QuizQuestionRepository quizQuestionRepository

    @Autowired
    QuestionAnswerRepository questionAnswerRepository

    def question
    def optionOK
    def optionKO

    def setup() {
        given: "create a question"
        question = new Question()
        question.setKey(1)
        question.setContent(QUESTION_TITLE)
        question.setContent(QUESTION_CONTENT)
        question.setStatus(Question.Status.REJECTED)
        and: 'an image'
        def image = new Image()
        image.setUrl(URL)
        image.setWidth(20)
        imageRepository.save(image)
        question.setImage(image)
        and: 'two options'
        optionOK = new Option()
        optionOK.setContent(OPTION_CONTENT)
        optionOK.setCorrect(true)
        optionOK.setQuestion(question)
        optionRepository.save(optionOK)
        optionKO = new Option()
        optionKO.setContent(OPTION_CONTENT)
        optionKO.setCorrect(false)
        optionKO.setQuestion(question)
        optionRepository.save(optionKO)
        question.addOption(optionKO)
        question.addOption(optionOK)
        questionRepository.save(question)
    }

    def "resubmit a question"(){
        given: "create a question"
        def questionDto = new QuestionDto()
        questionDto.setId(question.getId())
        questionDto.setTitle(NEW_QUESTION_TITLE)
        questionDto.setContent(NEW_QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.REJECTED.name())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setId(optionOK.getId())
        optionDto.setContent(NEW_OPTION_CONTENT)
        optionDto.setCorrect(false)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setId(optionKO.getId())
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(true)
        options.add(optionDto)
        questionDto.setOptions(options)

        when:
        questionService.resubmitQuestion(question.getId(), questionDto)

        then: "the question is changed"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() == question.getId()
        result.getTitle() == NEW_QUESTION_TITLE
        result.getContent() == NEW_QUESTION_CONTENT
        result.getStatus() == Question.Status.PENDING
        and: 'are not changed'
        result.getImage() != null
        and: 'an option is changed'
        result.getOptions().size() == 2
        def resOptionOne = result.getOptions().stream().filter({option -> option.getId() == optionOK.getId()}).findAny().orElse(null)
        resOptionOne.getContent() == NEW_OPTION_CONTENT
        !resOptionOne.getCorrect()
        def resOptionTwo = result.getOptions().stream().filter({option -> option.getId() == optionKO.getId()}).findAny().orElse(null)
        resOptionTwo.getContent() == OPTION_CONTENT
        resOptionTwo.getCorrect()
    }

    def "resubmitted question with no alterations"(){
        given: "create a question"
        def questionDto = new QuestionDto()
        questionDto.setId(question.getId())
        questionDto.setTitle(QUESTION_TITLE)
        questionDto.setContent(QUESTION_CONTENT)
        questionDto.setStatus(Question.Status.REJECTED.name())
        and: 'a optionId'
        def optionDto = new OptionDto()
        optionDto.setId(optionOK.getId())
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(false)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)
        optionDto = new OptionDto()
        optionDto.setId(optionKO.getId())
        optionDto.setContent(OPTION_CONTENT)
        optionDto.setCorrect(true)
        options.add(optionDto)
        questionDto.setOptions(options)

        when:
        questionService.resubmitQuestion(question.getId(), questionDto)

        then: "the question did not change, still rejected"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() == question.getId()
        result.getTitle() == QUESTION_TITLE
        result.getContent() == QUESTION_CONTENT
        result.getStatus() == Question.Status.REJECTED
        result.getImage() != null
        result.getOptions().size() == 2
        def resOptionOne = result.getOptions().stream().filter({option -> option.getId() == optionOK.getId()}).findAny().orElse(null)
        resOptionOne.getContent() == OPTION_CONTENT
        !resOptionOne.getCorrect()
        def resOptionTwo = result.getOptions().stream().filter({option -> option.getId() == optionKO.getId()}).findAny().orElse(null)
        resOptionTwo.getContent() == OPTION_CONTENT
        resOptionTwo.getCorrect()
    }

    @Unroll("invalid arguments: #newTitle | #newContent | #optionContent | #correctOption || errorMessage ")
    def "invalid input values"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(newTitle )
        questionDto.setContent(newContent)

        and: 'an option'
        def optionDto1 = new OptionDto()
        optionDto1.setContent(optionContent)
        optionDto1.setCorrect(correctOption)
        def options1 = new ArrayList<OptionDto>()
        options1.add(optionDto1)
        questionDto.setOptions(options1)

        when:
        questionService.resubmitQuestion(question.getId(), questionDto)

        then: "a TutorException is thrown"
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        newTitle            | newContent            | optionContent      | correctOption  || errorMessage
        null                | NEW_QUESTION_CONTENT  | NEW_OPTION_CONTENT |      true      || QUESTION_MISSING_DATA
        ""                  | NEW_QUESTION_CONTENT  | NEW_OPTION_CONTENT |      true      || QUESTION_MISSING_DATA
        NEW_QUESTION_TITLE  |       null            | NEW_OPTION_CONTENT |      true      || QUESTION_MISSING_DATA
        NEW_QUESTION_TITLE  |        ""             | NEW_OPTION_CONTENT |      true      || QUESTION_MISSING_DATA
        NEW_QUESTION_TITLE  | NEW_QUESTION_CONTENT  |       ""           |      true      || QUESTION_MISSING_DATA
        NEW_QUESTION_TITLE  | NEW_QUESTION_CONTENT  | NEW_OPTION_CONTENT |      false     || QUESTION_MULTIPLE_CORRECT_OPTIONS
    }

    @TestConfiguration
    static class QuestionServiceImplTestContextConfiguration {

        @Bean
        QuestionService questionService() {
            return new QuestionService()
        }
    }
}
