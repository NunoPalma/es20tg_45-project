package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import spock.lang.Specification
import spock.lang.Unroll

class CreateTournamentServiceSpockTest extends Specification {

    def tournamentService;

    def setup() {
        tournamentService = new TournamentService()
    }

    @Unroll
    def "invalid arguments: name=#name | startDate=#startDate | endDate=#endDate | numTopics=#numTopics || numQuestions=#numQuestions "() {
        // Exception is yeeted
        expect: false
    }

    def "end date is lesser than the start date"() {
        // Exception is thrown
        expect: false
    }

    def "the dates overlap"() {
        // Exception is thrown
        expect: false
    }

    def "number of topics is zero"() {
        // Exception is thrown
        expect: false
    }

    def "number of questions is zero"() {
        // Exception is thrown
        expect: false
    }

    def "user role is not student"() {
        // Exception is thrown
        expect: false
    }

    def "all arguments are valid and create tournament"() {
        // tournament is created
        expect: false
    }
}
