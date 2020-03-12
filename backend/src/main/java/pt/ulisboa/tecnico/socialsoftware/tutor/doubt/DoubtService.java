package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.SQLException;

import java.util.Set;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class DoubtService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    DoubtRepository doubtRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DoubtDto createDoubt(DoubtDto doubtdto, Integer questionId, Integer studentId) throws TutorException {
        if(studentId == null){
            throw new TutorException(ErrorMessage.DOUBT_USER_IS_EMPTY);
        }
        if(questionId == null){
            throw new TutorException(ErrorMessage.DOUBT_QUESTION_IS_EMPTY);
        }

        String content = doubtdto.getContent();
        if(content == null || content.trim().isEmpty()){
            throw new TutorException(DOUBT_CONTENT_IS_EMPTY);
        }

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionId));
        User student = userRepository.findById(studentId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, studentId));
        Set<QuizAnswer> quizAnswers = student.getQuizAnswers();
        if(quizAnswers.isEmpty()){
            throw new TutorException(DOUBT_USER_HASNT_ANSWERED);
        }
        Set<QuizQuestion> quizQuestions;
        Quiz quiz;
        for(QuizAnswer quizanswer : quizAnswers){
            quiz = quizanswer.getQuiz();
            quizQuestions = quiz.getQuizQuestions();
            for(QuizQuestion quizQuestion : quizQuestions){
                if(quizQuestion.getQuestion().equals(question)){
                    break;
                }
            }
            throw new TutorException(DOUBT_USER_HASNT_ANSWERED);
        }
        if(student.getRole() != User.Role.STUDENT){
            throw new TutorException(DOUBT_USER_IS_NOT_A_STUDENT);
        }

        Doubt doubt = new Doubt(question, student, content);
        this.entityManager.persist(doubt);
        question.addDoubt(doubt);
        student.addDoubt(doubt);

        return new DoubtDto(doubt);
    }
}
