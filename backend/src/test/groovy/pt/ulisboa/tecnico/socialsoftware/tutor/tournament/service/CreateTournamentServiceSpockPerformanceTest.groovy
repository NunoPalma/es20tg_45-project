package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification

@DataJpaTest
class CreateTournamentServiceSpockPerformanceTest extends Specification {

	def courseExecution
	def formatter

	@Autowired
	TournamentService tournamentService

	@Autowired
	UserRepository userRepository

	@Autowired
	TournamentRepository tournamentRepository

	@Autowired
	CourseRepository courseRepository

	@Autowired
	CourseExecutionRepository courseExecutionRepository

	@Autowired
	TopicRepository topicRepository

	def "valid tournament creations"() {
		given: "a course execution"
		def course = new Course()
		course.setId(1)
		courseRepository.save(course)
		def courseExecution = new CourseExecution()
		courseExecution.setCourse(course)
		courseExecutionRepository.save(courseExecution)
		and: "a user"
		def user = new User()
		user.setKey(1)
		user.setRole(User.Role.STUDENT)
		userRepository.save(user)
		and: "a set of topics"
		def topicDto = new TopicDto()
		topicDto.setName("LeTopic")
		def topic = new Topic(course, topicDto)
		topicRepository.save(topic)
		Set<TopicDto> topics = new HashSet<>(Arrays.asList(topicDto));
		and: "a tournamentDto"
		def tournamentDto = new TournamentDto()
		tournamentDto.setName("LeTournament")
		tournamentDto.setStartDate("2020-10-10 11:11:11")
		tournamentDto.setEndDate("2020-10-12 22:22:22")
		tournamentDto.setTopics(topics)
		tournamentDto.setNumQuestions(20)

		when:
		1.upto(1000, {
			def ret = tournamentService.createTournament(1, 1, tournamentDto)
		})
		then:
		true
	}

	@TestConfiguration
	static class ServiceImplTestContextConfiguration {

		@Bean
		TournamentService tournamentService() {
			return new TournamentService()
		}
	}
}