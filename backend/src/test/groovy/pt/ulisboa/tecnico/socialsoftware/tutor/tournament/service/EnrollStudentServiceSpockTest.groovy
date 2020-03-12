package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import spock.lang.Specification
import spock.lang.Unroll

@DataJpaTest
class EnrollStudentServiceSpockTest extends Specification {

    def tournamentService

    def setup() {
        tournamentService = new TournamentService()
    }

    def "a user that isn't a student tries to enroll"() {
        //an exception is thrown
        expect: false
    }

    def "the student is already enrolled in the tournament"() {
        //an exception is thrown
        expect: false
    }

    @Unroll
    def "invalid arguments where student is #studentExists and tournament is #tournamentExists and exception is #expectedException"() {
        //an exception is thrown
        expect: false
    }

    @Unroll
    def "invalid enrollment attempt where the student exists and the tournament's state is #state"() {
        //an exception is thrown
        expect: false
    }

    def "valid enrollment where the student exists and the tournament is open"() {
        //the student is enrolled in the tournament
        expect: false
    }
}