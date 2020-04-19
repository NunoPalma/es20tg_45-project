package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.*;


import java.util.Set;


import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class DoubtService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private DoubtRepositor doubtRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DoubtDto createDoubt(DoubtDto doubtdto, Integer questionId, Integer studentId){

        if(studentId == null){
            throw new TutorException(ErrorMessage.DOUBT_USER_IS_EMPTY);
        }

        if(questionId == null){
            throw new TutorException(ErrorMessage.DOUBT_QUESTION_IS_EMPTY);
        }

        String content = doubtdto.getContent();

        User student = userRepository.findById(studentId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, studentId));

        if(student.getRole() != User.Role.STUDENT){
            throw new TutorException(DOUBT_USER_IS_NOT_A_STUDENT);
        }

        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionId));

        QuestionAnswer questionAnswer = quizQuestion.getQuestionAnswerofUser(studentId);

        Doubt doubt = new Doubt(questionAnswer, student, content);
        this.entityManager.persist(doubt);

        return new DoubtDto(doubt);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<DoubtDto> findQuizQuestionDoubts(Integer questionQuestionId){
        if (questionQuestionId == null){
            throw new TutorException(DOUBT_USER_IS_EMPTY);
        }
        List<DoubtDto> doubts = new ArrayList<>();
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionQuestionId).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionQuestionId));
        Set<QuestionAnswer> questionAnswerList = quizQuestion.getQuestionAnswers();
        for(QuestionAnswer questionAnswer : questionAnswerList){
            doubts.addAll(doubtRepository.findQuestionAnswerDoubts(questionAnswer.getId()).stream().map(DoubtDto::new).collect(Collectors.toList()));
        }
        return doubts;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<DoubtDto> findUserDoubts(Integer userId){
        if (userId == null){
            throw new TutorException(DOUBT_USER_IS_EMPTY);
        }
        return doubtRepository.findUserDoubts(userId).stream().map(DoubtDto::new).collect(Collectors.toList());
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Question getDoubtQuestion(Integer doubtId) {
        Doubt doubt = doubtRepository.findById(doubtId).orElseThrow(()-> new TutorException(DOUBT_NOT_FOUND));
        return doubt.getQuestionAnswer().getQuizQuestion().getQuestion();
    }
}
