package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
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
	static final String TOPIC_NAME = "InterestingTopic"
	static final int USER_ID = 1
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

	def tournamentDto
	def topic
	def topicNameList
	def topicsEmpty
	def topicsNotEmpty
	def userStudent
	def course
	def courseExecution
	def formatter

	def setup() {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

		topic = new Topic()
		topic.setName(TOPIC_NAME)
		topicRepository.save(topic)

		topicNameList = new ArrayList<>()
		topicNameList.add(TOPIC_NAME)

		topicsEmpty = new HashSet<>()

		topicsNotEmpty = new HashSet<>()
		topicsNotEmpty.add(topic)

		userStudent = new User()
		userStudent.setRole(User.Role.STUDENT)
		userStudent.setKey(USER_ID)
		userRepository.save(userStudent)

		course = new Course()
		course.setName(COURSE_NAME)
		course.addTopic(topic)
		courseRepository.save(course)

		topic.setCourse(course)

		courseExecution = new CourseExecution()
		courseExecution.setAcronym(COURSE_EXECUTION_ACRONYM)
		courseExecution.setAcademicTerm(COURSE_EXECUTION_ACADEMIC_TERM)
		courseExecution.setCourse(course)
		courseExecutionRepository.save(courseExecution)

		tournamentDto = new TournamentDto()
		tournamentDto.setName(TOURNAMENT_NAME)
		tournamentDto.setStartDate(START_DATE.format(formatter))
		tournamentDto.setEndDate(END_DATE.format(formatter))
		tournamentDto.setTopics(topicsNotEmpty as Set<TopicDto>)
		tournamentDto.setNumQuestions(ONE_QUESTION)
	}

	@Unroll
	def "invalid arguments: tournamentName=#tournamentName | startDate=#startDate | endDate=#endDate \
        | numQuestions=#numQuestions || errorMessage=#errorMessage"() {
		given: "a TournamentDto"
		def tournamentDto = new TournamentDto(userStudent, courseExecution)
		tournamentDto.setName(tournamentName)
		tournamentDto.setStartDate(START_DATE.format(formatter))
		tournamentDto.setEndDate(END_DATE.format(formatter))
		tournamentDto.setNumQuestions(numQuestions)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), topicNameList, tournamentDto)

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
		tournamentDto.setName(null)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), topicNameList, tournamentDto)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == TOURNAMENT_NAME_EMPTY
	}

	def "tournament creator is a student"() {
		given: "a user that is not a student"
		tournamentDto.setName("HelloTournament")
		userStudent.setRole(User.Role.TEACHER)

		when:
		tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), topicNameList, tournamentDto)

		then: "an exception is thrown"
		def exception = thrown(TutorException)
		exception.getErrorMessage() == TOURNAMENT_CREATOR_IS_NOT_STUDENT
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
		def result = tournamentService.createTournament(userStudent.getId(), courseExecution.getId(), topicNameList, tournamentDto)

		then: "the returned data are correct"
		result.name == TOURNAMENT_NAME
		result.startDate == START_DATE.format(formatter)
		result.endDate == END_DATE.format(formatter)
		result.numQuestions == ONE_QUESTION
		result.topics.size() == 1
	}

	@TestConfiguration
	static class QuizServiceImplTestContextConfiguration {

		@Bean
		TournamentService tournamentService() {
			return new TournamentService()
		}
	}
}
