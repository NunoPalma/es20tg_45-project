package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class EvaluationService {

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    EntityManager entityManager;


    EvaluationService() {
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EvaluationDto createEvaluation(EvaluationDto evaluationDto, int questionKey) {
        Question question = questionRepository.findByKey(questionKey).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionKey));
        if (question.getStatus() != Question.Status.PENDING){
            throw new TutorException(QUESTION_NOT_PENDING, questionKey);
        }

        evaluationDto.setId(questionKey);

        Evaluation evaluation = new Evaluation(question);
        this.entityManager.persist(evaluation);
        return new EvaluationDto();
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void submitEvaluation(Integer questionKey, boolean approvedEvaluation, String justification) {

    }
}
