package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationDto
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import spock.lang.Specification

@DataJpaTest
class SubmitEvaluationTest extends Specification {
    public static final String JUSTIFICATION = "QUESTION JUSTIFICATION"

    @Autowired
    Question pendingQuestion

    @Autowired
    EvaluationService evaluationService

    @Autowired
    EvaluationRepository evaluationRepository

    def "rejected question must have justification"() {
        given: "an evaluationDto"
        def evaluationDto = new EvaluationDto()
        and: "a question"
        def pendingQuestion = new Question()
        pendingQuestion.setKey(1)
        evaluationService.createEvaluation(evaluationDto, pendingQuestion.getKey())
        def evaluation = evaluationRepository.findAll().get(0)

        when:
        evaluationService.submitEvaluation(pendingQuestion.getKey(), false, JUSTIFICATION)

        then:
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
        pendingQuestion.setKey(1)
        evaluationService.createEvaluation(evaluationDto, pendingQuestion.getKey())
        def evaluation = evaluationRepository.findAll().get(0)

        when:
        evaluationService.submitEvaluation(pendingQuestion.getKey(), true, JUSTIFICATION)

        then:
        evaluation.getSubmittedQuestion().getStatus() == Question.Status.AVAILABLE
    }

    @TestConfiguration
    static class EvaluationServiceImplTestContextConfiguration {

        @Bean
        EvaluationService evaluationService() {
            return new EvaluationService()
        }
    }
}

