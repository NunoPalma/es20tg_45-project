package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationDto
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationService
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.MUST_HAVE_JUSTIFICATION


@DataJpaTest
class SubmitEvaluationTest extends Specification {
    public static final String JUSTIFICATION = "QUESTION JUSTIFICATION"

    @Autowired
    EvaluationService evaluationService

    @Autowired
    EvaluationRepository evaluationRepository

    @Autowired
    QuestionRepository questionRepository


    def "rejected question must have justification"() {
        given: "an evaluationDto"
        def evaluationDto = new EvaluationDto()
        and: "a question"
        def pendingQuestion = new Question()
        pendingQuestion.setStatus(Question.Status.PENDING)
        pendingQuestion.setKey(1)
        questionRepository.save(pendingQuestion)
        and: "a questionDto"
        def pendingQuestionDto = new QuestionDto(pendingQuestion)

        evaluationService.createEvaluation(evaluationDto, pendingQuestionDto)

        when:
        evaluationService.submitEvaluation(pendingQuestionDto, false, JUSTIFICATION)

        then:
        evaluationRepository.count() == 1L
        def evaluation = evaluationRepository.findAll().get(0)
        def evalJustification = evaluation.getJustification()
        evalJustification != null
        evalJustification.length() != 0
        evalJustification == JUSTIFICATION
        evaluation.getSubmittedQuestion().getStatus() == Question.Status.REJECTED
    }

    def "approved evaluation must have approved question"() {
        given: "an evaluationDto"
        def evaluationDto = new EvaluationDto()
        and: "a question"
        def pendingQuestion = new Question()
        pendingQuestion.setStatus(Question.Status.PENDING)
        pendingQuestion.setKey(1)
        questionRepository.save(pendingQuestion)
        and: "a questionDto"
        def pendingQuestionDto = new QuestionDto(pendingQuestion)

        evaluationService.createEvaluation(evaluationDto, pendingQuestionDto)

        when:
        evaluationService.submitEvaluation(pendingQuestionDto, true, JUSTIFICATION)

        then:
        evaluationRepository.count() == 1L
        def evaluation = evaluationRepository.findAll().get(0)
        evaluation.getSubmittedQuestion().getStatus() == Question.Status.AVAILABLE
    }

    @Unroll("invalid arguments: #isApproved | #justification | errorMessage")
    def "invalid input values"(){
        given: "an evaluationDto"
        def evaluationDto = new EvaluationDto()
        and: "a question"
        def pendingQuestion = new Question()
        pendingQuestion.setStatus(Question.Status.PENDING)
        pendingQuestion.setKey(1)
        questionRepository.save(pendingQuestion)
        and: "a questionDto"
        def pendingQuestionDto = new QuestionDto(pendingQuestion)
        evaluationService.createEvaluation(evaluationDto, pendingQuestionDto)

        def approved = isApproved
        def eval_justification = justification

        when:
        evaluationService.submitEvaluation(pendingQuestionDto, approved, eval_justification)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
            isApproved      |  justification    || errorMessage
            false           |   null            || MUST_HAVE_JUSTIFICATION
            false           |   ""              || MUST_HAVE_JUSTIFICATION
    }


    @TestConfiguration
    static class EvaluationServiceImplTestContextConfiguration {

        @Bean
        EvaluationService evaluationService() {
            return new EvaluationService()
        }
    }
}

