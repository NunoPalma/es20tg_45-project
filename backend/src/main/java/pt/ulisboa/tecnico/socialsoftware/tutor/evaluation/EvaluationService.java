package pt.ulisboa.tecnico.socialsoftware.tutor.evaluation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.QuizService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class EvaluationService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @PersistenceContext
    EntityManager entityManager;

    EvaluationService() {
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EvaluationDto createEvaluation(EvaluationDto evaluationDto, QuestionDto questionDto) {
        Question question = questionRepository.findByKey(questionDto.getKey()).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionDto.getKey()));
        if (question.getStatus() != Question.Status.PENDING){
            throw new TutorException(QUESTION_NOT_PENDING, question.getKey());
        }

        Evaluation evaluation = new Evaluation(question);
        this.entityManager.persist(evaluation);
        return new EvaluationDto(evaluation, questionDto);
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EvaluationDto submitEvaluation(QuestionDto questionDto, boolean approvedEvaluation, String justification) {
        Evaluation evaluation = evaluationRepository.findByKey(questionDto.getKey()).orElseThrow(() -> new TutorException(EVALUATION_NOT_AVAILABLE, questionDto.getKey()));
        Question question = questionRepository.findByKey(questionDto.getKey()).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionDto.getKey()));

        if (approvedEvaluation) {
            question.setStatus(Question.Status.AVAILABLE);
            questionDto.setStatus(Question.Status.AVAILABLE.name());
            evaluation.approveEvaluation();
            evaluation.setJustification(justification);
        }

        else {
            question.setStatus(Question.Status.REJECTED);
            questionDto.setStatus(Question.Status.REJECTED.name());
            //LANÇAR EXCEÇÃO SE JUSTIFICAÇÃO TIVER VAZIA
            evaluation.setJustification(justification);
        }

        return new EvaluationDto(evaluation, questionDto);
    }
}
