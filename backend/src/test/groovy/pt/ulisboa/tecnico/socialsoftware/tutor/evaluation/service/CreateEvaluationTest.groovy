package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class CreateEvaluationTest extends Specification {

    def "created evaluation can't be approved nor have justification"() {
        //checks if created evaluation attributes are correctly instantiated
        expect: false
    }

    def "evaluation has pending question"() {
        //checks if pending evaluation is correctly assigned to a pending question
        expect: false
    }

    def "evaluation not null"() {
        //checks if object created is not null
        expect: false
    }
}
