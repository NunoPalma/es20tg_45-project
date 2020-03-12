package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class SubmitEvaluationTest extends Specification {

    def "rejected evaluation must have justification"() {
        //checks if rejected evaluation has a justification
        expect: false
    }

    def "approved evaluation must have approved question"() {
        //checks if approved evaluation is associated to an approved question
        expect: false
    }
}