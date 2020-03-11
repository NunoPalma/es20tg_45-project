package pt.ulisboa.tecnico.socialsoftware.tutor.doubt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import spock.lang.Specification

@DataJpaTest
class CreateDoubtTest extends Specification {

    @Autowired
    DoubtService doubtService

    @Autowired
    QuestionRepository questionRepository

    def doubt

    def setup() {
        doubtService = new DoubtService()
    }

    def "create a Doubt with a User, Content and a Question"() {
        expect: false
    }

    def "create a Doubt without a User"() {
        //Throws an exception
        expect: false
    }

    def "create a Doubt without Content"() {
        //Throws an exception
        expect:false
    }

    def "create a Doubt without Question"(){
        //Throws an exception
        except:false
    }

    def "create a Doubt but the User is not a Student"() {
        //Throws an exception
        expect: false
    }

    def "create two Doubts with a User and Content"() {
        expect: false
    }

}
