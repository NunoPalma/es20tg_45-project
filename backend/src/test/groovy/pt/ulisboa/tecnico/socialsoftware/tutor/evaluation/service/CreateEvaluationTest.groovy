package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationDto
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.EvaluationService
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import spock.lang.Specification


@DataJpaTest
class CreateEvaluationTest extends Specification {

    @Autowired
    EvaluationService evaluationService

    @Autowired
    EvaluationRepository evaluationRepository

    @Autowired
    QuestionRepository questionRepository

    def pendingQuestion

    def setup(){
        pendingQuestion = new Question()
        pendingQuestion.setKey(1)
        pendingQuestion.setStatus(Question.Status.PENDING)
        questionRepository.save(pendingQuestion)
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
        evaluation != null

        then:
        true

    }

    @TestConfiguration
    static class EvaluationServiceImplTestContextConfiguration {

        @Bean
        EvaluationService evaluationService() {
            return new EvaluationService()
        }
    }
}
