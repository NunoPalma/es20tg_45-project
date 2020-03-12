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
class CreateEvaluationTest extends Specification {

    @Autowired
    Question pendingQuestion

    @Autowired
    EvaluationService evaluationService

    @Autowired
    EvaluationRepository evaluationRepository

    def setup(){
        pendingQuestion = new Question()
        pendingQuestion.setKey(1)
    }

    def "created evaluation can't be approved nor have justification"() {
        given: "An evaluationDto"
        def evaluationDto = new EvaluationDto()

        when:
        evaluationService.createEvaluation(evaluationDto, pendingQuestion.getKey())

        then: "the correct evaluation is inside the repository"
        evaluationRepository.count() == 1L
        def evaluation = evaluationRepository.findAll().get(0)
        evaluation.getEvaluation() == false
        evaluation.getJustification() == null
    }

    def "evaluation has pending question"() {
        given: "An evaluationDto"
        def evaluationDto = new EvaluationDto()

        when:
        evaluationService.createEvaluation(evaluationDto, pendingQuestion.getKey())

        then: "associated question must be pending"
        def evaluation = evaluationRepository.findAll().get(0)
        evaluation.getSubmittedQuestion().getStatus() == Question.Status.PENDING
    }

    def "evaluation not null"() {
        given: "An evaluationDto"
        def evaluationDto = new EvaluationDto()

        evaluationService.createEvaluation(evaluationDto, pendingQuestion.getKey())
        def evaluation = evaluationRepository.findAll().get(0)

        when:
        evaluation == null

        then:
        InvalidObjectException exception = thrown()
        exception.message("Evaluation creation failed. Object is invalid")

    }

    @TestConfiguration
    static class EvaluationServiceImplTestContextConfiguration {

        @Bean
        EvaluationService evaluationService() {
            return new EvaluationService()
        }
    }
}
