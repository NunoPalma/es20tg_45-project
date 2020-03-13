package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
class CreateTournamentServiceSpockTest extends Specification {

	static final String TOURNAMENT_NAME = "TournamentOne"
	static final LocalDateTime START_DATE = LocalDateTime.now()
	static final LocalDateTime OVERLAP_END_DATE = START_DATE
	static final LocalDateTime EARLY_END_DATE = START_DATE.minusDays(1)
	static final LocalDateTime END_DATE = START_DATE.plusDays(20)
	static final Integer ONE_QUESTION = 1
	static final int USER_ID = 1
	static final int COURSE_EXECUTION_ID = 1

	@Autowired
	TournamentService tournamentService

	def tournament
	def topicsEmpty
	def topicsNotEmpty
	def userStudent
	def courseExecution
	def formatter

	def setup() {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

		topicsEmpty = new HashSet<>()

		topicsNotEmpty = new HashSet<>()
		topicsNotEmpty.add(new Topic())

		userStudent = new User()
		userStudent.setRole(User.Role.STUDENT)
		userStudent.setId(USER_ID)

		courseExecution = new CourseExecution()
		courseExecution.setId(COURSE_EXECUTION_ID)

		tournament = new TournamentDto()
		tournament.setStartDate(START_DATE.format(formatter))
		tournament.setEndDate(END_DATE.format(formatter))
		tournament.setTopics(topicsNotEmpty as Set<TopicDto>)
	}

	@Unroll
	def "invalid arguments: tournamentName=#tournamentName | startDate=#startDate | endDate=#endDate \
        | numQuestions=#numQuestions || errorMessage=#errorMessage"() {
		given: "a TournamentDto"
		def tournamentDto = new TournamentDto(userStudent, courseExecution)
		tournamentDto.setName(tournamentName)
		tournamentDto.setStartDate(startDate.format(formatter))
		tournamentDto.setEndDate(endDate.format(formatter))
		tournamentDto.setNumQuestions(numQuestions)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), tournamentDto)

		then:
		def error = thrown(TutorException)
		error.errorMessage == errorMessage

		where:
		tournamentName  | startDate  | endDate          | numQuestions || errorMessage
		null            | START_DATE | END_DATE         | ONE_QUESTION || TOURNAMENT_NAME_EMPTY
		"  "            | START_DATE | END_DATE         | ONE_QUESTION || TOURNAMENT_NAME_EMPTY
		TOURNAMENT_NAME | null       | END_DATE         | ONE_QUESTION || TOURNAMENT_START_DATE_EMPTY
		TOURNAMENT_NAME | START_DATE | null             | ONE_QUESTION || TOURNAMENT_END_DATE_EMPTY
		TOURNAMENT_NAME | START_DATE | EARLY_END_DATE   | ONE_QUESTION || TOURNAMENT_INVALID_END_DATE
		TOURNAMENT_NAME | START_DATE | OVERLAP_END_DATE | ONE_QUESTION || TOURNAMENT_DATES_OVERLAP
		TOURNAMENT_NAME | START_DATE | END_DATE         | 0            || TOURNAMENT_NOT_ENOUGH_QUESTIONS
	}

	def "create a tournament no name"() {
		given: "a tournament"
		tournament.setName(null)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), tournament)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == TOURNAMENT_NAME_EMPTY
	}

	def "tournament creator is a student"() {
		given: "a user that is not a student"
		userStudent.setRole(User.Role.TEACHER)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), tournament)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == TOURNAMENT_CREATOR_IS_NOT_STUDENT
	}

	def "create tournament without enough topics"() {
		given: "a tournament"
		tournament.setTopics(topicsEmpty)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), tournament)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == NOT_ENOUGH_TOPICS
	}

	def "all arguments are valid and create tournament"() {
		given: "a tournamentDto"
		def tournamentDto = new TournamentDto()
		tournamentDto.setName(TOURNAMENT_NAME)
		tournamentDto.setStartDate(START_DATE.format(formatter))
		tournamentDto.setEndDate(END_DATE.format(formatter))
		tournamentDto.setNumQuestions(ONE_QUESTION)
		tournamentDto.setTopics(topicsNotEmpty)

		when:
		def result = tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), tournamentDto)

		then: "the returned data are correct"
		result.name == TOURNAMENT_NAME
		result.startDate == START_DATE.format(formatter)
		result.endDate == END_DATE.format(formatter)
		result.numQuestions == ONE_QUESTION
		result.topics.get(0) == 1
	}

	@TestConfiguration
	static class QuizServiceImplTestContextConfiguration {

		@Bean
		TournamentService tournamentService() {
			return new TournamentService()
		}
	}
}
