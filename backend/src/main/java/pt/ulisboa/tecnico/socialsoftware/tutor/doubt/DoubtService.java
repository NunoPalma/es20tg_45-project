package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
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
    private QuestionRepository questionRepository;

    @Autowired
    private DoubtRepositor doubtRepository;

    @PersistenceContext
    EntityManager entityManager;


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

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionId));

        Set<QuizAnswer> quizAnswers = student.getQuizAnswers();
        if(quizAnswers.isEmpty()){
            throw new TutorException(DOUBT_USER_HASNT_ANSWERED);
        }

        List<Quiz> quizList = quizAnswers.stream().map(QuizAnswer::getQuiz).collect(Collectors.toList());
        Set<QuizQuestion> quizQuestions = new HashSet<>();
        for(Quiz quiz: quizList){
            quizQuestions.addAll(quiz.getQuizQuestions());
        }
        if (!quizQuestions.stream().map(QuizQuestion::getQuestion).collect(Collectors.toSet()).contains(question)) {
            throw new TutorException(DOUBT_USER_HASNT_ANSWERED);
        }

        Doubt doubt = new Doubt(question, student, content);
        this.entityManager.persist(doubt);

        return new DoubtDto(doubt);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<DoubtDto> findUserDoubts(Integer userId){
        if (userId == null){
            throw new TutorException(DOUBT_USER_IS_EMPTY);
        }
        return doubtRepository.findUserDoubts(userId).stream().map(DoubtDto::new).collect(Collectors.toList());
    }

}
