package pt.ulisboa.tecnico.socialsoftware.tutor.clarification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.Doubt;
import pt.ulisboa.tecnico.socialsoftware.tutor.doubt.DoubtRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

@Service
public class ClarificationService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoubtRepository doubtRepository;

    @PersistenceContext
    EntityManager entityManager;

    public ClarificationService(){
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ClarificationDto createClarification(ClarificationDto clarificationDto, Integer userId, Integer doubtId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new TutorException(ErrorMessage.USER_NOT_FOUND,userId));
        Doubt doubt = doubtRepository.findById(doubtId).orElseThrow(() -> new TutorException(ErrorMessage.DOUBT_NOT_FOUND, doubtId));

        Clarification clarification = new Clarification(user, doubt, clarificationDto);
        this.entityManager.persist(clarification);

        return new ClarificationDto(clarification);

    }

}