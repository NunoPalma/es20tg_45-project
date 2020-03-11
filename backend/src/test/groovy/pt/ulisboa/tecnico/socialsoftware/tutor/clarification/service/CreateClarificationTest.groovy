package pt.ulisboa.tecnico.socialsoftware.tutor.clarification.service

import spock.lang.Specification
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.*

class CreateClarificationTest extends Specification{

    def clarificationService

    def setup(){
        clarificationService = new ClarificationService()
    }

    def "create a clarification"(){
        //creates a clarification with consistent data
        expect: false
    }

    def "clarification description is empty"(){
        //Throws Exception
        expect: false
    }

    def "clarification owner is not a teacher"(){
        //Throws Exception
        expect: false
    }

    def "clarification owner is a teacher"(){
        //creates a clarification with an invalid teacher
        expect: false
    }

    def "clarification for a solved clarification request"(){
        //creates a clarification for a clarification request already solved
        expect: false
    }

    def "clarification for an invalid clarification request"(){
        //Throws Exception
        expect: false
    }


}


