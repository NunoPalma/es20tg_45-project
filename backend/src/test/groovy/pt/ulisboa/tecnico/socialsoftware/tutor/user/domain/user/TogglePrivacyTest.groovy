package pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserService
import spock.lang.Specification

@DataJpaTest
class TogglePrivacyTest extends Specification {

    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"

    public static final String STUDENT_NAME = "Student Name"
    public static final String STUDENT_USERNAME = "StudentUsername"
    public static final Integer STUDENT_KEY = 1

    @Autowired
    UserRepository userRepository

    @Autowired
    UserService userService

    @Autowired
    CourseRepository courseRepository

    def course
    def student

    def setup() {
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        student = new User(STUDENT_NAME, STUDENT_USERNAME, STUDENT_KEY, User.Role.STUDENT)
        userRepository.save(student)
    }

    def "student with private information toggles to public"() {
        given: "current privacy settings"
        def currentPrivacy = userService.getPrivacy(student.getUsername())

        when:
        userService.togglePrivacy(student.getUsername())

        then:
        def alteredPrivacy = userService.getPrivacy(student.getUsername())
        !alteredPrivacy.equals(currentPrivacy)
        alteredPrivacy == true
    }


    @TestConfiguration
    static class QuestionServiceImplTestContextConfiguration {
        @Bean
        UserService userService() {
            return new UserService()
        }
    }

}