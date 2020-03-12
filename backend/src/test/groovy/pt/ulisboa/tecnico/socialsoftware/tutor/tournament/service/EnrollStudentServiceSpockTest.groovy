package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.USER_NOT_DEFINED
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NOT_DEFINED
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_ENROLLMENT_CLOSED_TOURNAMENT
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_ENROLLMENT_CREATED_TOURNAMENT
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.INVALID_ENROLLMENT_CANCELLED_TOURNAMENT

class EnrollStudentServiceSpockTest extends Specification {

    static final int USER_ID = 1
    def tournamentService
    def user
    def student
    def static tournamentDto
    def static closedTournamentState
    def static createdTournamentState
    def static cancelledTournamentState

    def setup() {
        tournamentService = new TournamentService()

        user = new User()

        student = new User()
        student.setRole(User.Role.STUDENT)

        tournamentDto = new TournamentDto()

        def tournament = new Tournament(tournamentDto)
        closedTournamentState = tournament.getClosedState()
        createdTournamentState = tournament.getCreatedState()
        cancelledTournamentState = tournament.getCancelledState()
    }

    def "a user that isn't a student tries to enroll"() {
        given: "a user that isn't a student"
        user.setRole(User.Role.TEACHER)

        when:
        tournamentService.enrollStudent(user, tournamentDto)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.INVALID_ENROLLMENT_ATTEMPT_NOT_STUDENT
    }

    def "the student is already enrolled in the tournament"() {
        given: "an user that is a student"
        user.setRole(User.Role.STUDENT)
        and: "a tournament"
        def tournament = new Tournament(tournamentDto)

        when:
        tournament.addParticipant(user)
        tournamentService.enrollStudent(user, tournamentDto)

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.STUDENT_ALREADY_ENROLLED
    }

    @Unroll
    def "invalid arguments where student is #studentExists and tournament is #tournamentExists and errorMessage is #errorMessage"() {
        when:
        tournamentService.enrollStudent(studentExists, tournamentExists)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        studentExists   |   tournamentExists    ||  errorMessage
        null            |   tournamentDto       ||  USER_NOT_DEFINED
        USER_ID         |   null                ||  TOURNAMENT_NOT_DEFINED
    }

    @Unroll
    def "invalid enrollment attempt where the student exists and the tournament's state is #state and errorMessage is #errorMessage"() {
        given: "a student"
        user.setRole(User.Role.STUDENT)
        and: "a tournament"
        def tournament = new Tournament(tournamentDto)

        when:
        tournament.setState(state)
        tournamentService.enrollStudent(tournamentDto)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        state                         ||  errorMessage
        closedTournamentState         ||  INVALID_ENROLLMENT_CLOSED_TOURNAMENT
        createdTournamentState        ||  INVALID_ENROLLMENT_CREATED_TOURNAMENT
        cancelledTournamentState      ||  INVALID_ENROLLMENT_CANCELLED_TOURNAMENT
    }

    def "valid enrollment where the student exists and the tournament is open"() {
        given: "a student"
        user.setRole(User.Role.STUDENT)
        user.setId(USER_ID)
        and: "a tournament"
        def tournament = new Tournament(tournamentDto)

        when:
        tournament.setState(tournament.getOpenState())
        tournamentService.enrollStudent(user.getId(), tournamentDto)

        then:
        tournament.getParticipants().contains(user)
    }
}