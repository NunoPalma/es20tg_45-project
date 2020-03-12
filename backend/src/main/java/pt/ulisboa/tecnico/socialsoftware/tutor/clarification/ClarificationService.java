package pt.ulisboa.tecnico.socialsoftware.tutor.clarification;

import org.springframework.stereotype.Service;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

@Service
public class ClarificationService{

    public ClarificationService(){
    }

    public ClarificationDto createClarification(ClarificationDto clarification, Integer userId) {
        return clarification;
    }

}