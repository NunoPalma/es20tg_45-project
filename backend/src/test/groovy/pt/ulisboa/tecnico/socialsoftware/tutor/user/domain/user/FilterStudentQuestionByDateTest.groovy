package pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.user

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class FilterStudentQuestionByDateTest {
    def "three questions"() {
        //given a user with 3 submitted questions the returned data must be correctly sorted
        expect: false
    }

    def "no questions"() {
        //check if returned data from user with no submitted questions is correct
        expect: false
    }
}
