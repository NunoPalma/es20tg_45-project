package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

class CancelTournamentServiceSpockTest {

	static final Integer DEMO_USER = 676
	static final Integer VALID_USER = 42
	static final Integer INVALID_USER = 100000
	static final Integer VALID_TOURNAMENT = 43
	static final Integer INVALID_TOURNAMENT = 100000
	static final String TOURNAMENT_NAME = "LeTournament"
	static final String COURSE_NAME = "LEIC-T"
	static final String COURSE_EXECUTION_ACRONYM = "CS101"
	static final String COURSE_EXECUTION_ACADEMIC_TERM = "1º Semestre"


	@Autowired
	TournamentService tournamentService

	@Autowired
	CourseExecutionRepository courseExecutionRepository

	@Autowired
	CourseRepository courseRepository

	@Autowired
	UserRepository userRepository

	@Autowired
	TopicRepository topicRepository

	@Autowired
	TournamentRepository tournamentRepository


	def userStudent
	def course
	def courseExecution
	def tournament


	def setup() {
		userStudent = new User()
		userStudent.setRole(User.Role.STUDENT)
		userStudent.setKey(VALID_USER)
		userRepository.save(userStudent)

		course = new Course()
		course.setName(COURSE_NAME)
		courseRepository.save(course)

		courseExecution = new CourseExecution()
		courseExecution.setAcronym(COURSE_EXECUTION_ACRONYM)
		courseExecution.setAcademicTerm(COURSE_EXECUTION_ACADEMIC_TERM)
		courseExecution.setCourse(course)
		courseExecutionRepository.save(courseExecution)

		tournament = new Tournament()
		tournament.setCourseExecution(courseExecution)
		tournament.setCreator(userStudent)
		tournament.setName(TOURNAMENT_NAME)
		tournamentRepository.save(tournament)
	}

	@Unroll
	def "invalid arguments: userId=#userId | tournamentId=#tournamentId \
			|| errorMessage=#errorMessage"() {
		when:
		tournamentService.cancelTournament(userId, tournamentId)

		then:
		def error = thrown(TutorException)
		error.errorMessage == errorMessage

		where:
		userId | tournamentId || errorMessage
		null   | 1            || INVALID_USER_ID
		1      | null         || INVALID_TOURNAMENT_ID
	}

	def "tournament doesn't exist"() {
		when:
		tournamentService.cancelTournament(DEMO_USER, INVALID_TOURNAMENT)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == INVALID_TOURNAMENT_ID
	}

	def "user doesn't exist"() {
		when:
		tournamentService.cancelTournament(INVALID_USER, VALID_TOURNAMENT)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == INVALID_USER_ID
	}

	def "user that attempts tournament cancellation isn't the creator"() {
		when:
		tournamentService.cancelTournament(DEMO_USER, tournament.getId())

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == USER_NOT_TOURNAMENT_CREATOR
	}

	def "successful tournament cancellation"() {
		when:
		tournamentService.cancelTournament()
		
		then: "the tournament state is cancelled"
		def tournament = tournamentRepository.findTournamentByName(courseExecution.getId(), TOURNAMENT_NAME).get()
		tournament != null
		and: "values are correct"
		tournament.name = TOURNAMENT_NAME
		tournament.state = Tournament.State.CANCELLED
	}


	@TestConfiguration
	static class TournamentServiceImplTestContextConfiguration {

		@Bean
		TournamentService tournamentService() {
			return new TournamentService()
		}
	}
}
