package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class EvaluationService {

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createEvaluation(EvaluationDto evaluationDto, int questionKey) {

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void submitEvaluation(Integer questionId, boolean approvedEvaluation, String justification) {

    }
}
