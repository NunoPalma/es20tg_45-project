package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
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
class EnrollStudentServiceSpockPerformanceTest extends Specification {

    def USER_ID = 1000

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

    def "valid enrollment where the student exists and the tournament is open"() {
        given: "a user that creates a tournament"
        def creator = new User()
        creator.setRole(User.Role.STUDENT)
        creator.setKey(667)
        userRepository.save(creator)

        and: "a course execution"
        def course = new Course()
        course.setId(1)
        courseRepository.save(course)
        def courseExecution = new CourseExecution()
        courseExecution.setCourse(course)
        courseExecutionRepository.save(courseExecution)

        and: "a tournament"
        def tournament = new Tournament()
        tournament.setState(Tournament.State.OPEN)
        tournament.setCreator(creator)
        tournament.setCourseExecution(courseExecution)
        tournament.setNumQuestions(1)
        tournamentRepository.save(tournament)

        when: "1000 users try to enroll in a tournament"
        1.upto(1000, {
            def student = new User()
            student.setRole(User.Role.STUDENT)
            student.setKey(USER_ID)
            print(USER_ID)
            USER_ID += 1
            userRepository.save(student)
            tournamentService.enrollStudent(student.getId(), tournament.getId())

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