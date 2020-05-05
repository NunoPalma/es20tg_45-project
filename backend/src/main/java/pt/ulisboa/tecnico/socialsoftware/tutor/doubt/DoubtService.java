package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.JwtTokenProvider;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.clarification.ClarificationService;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;


import java.util.Set;


import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class DoubtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private DoubtRepositor doubtRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private ClarificationService clarificationService;

    @PersistenceContext
    EntityManager entityManager;

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Doubt createDoubt(DoubtDto doubtdto, Integer studentId, Discussion discussion){

        boolean isNew = doubtdto.isNew();

        String creationDate = doubtdto.getCreationDate();

        String content = doubtdto.getContent();

        User student = userRepository.findById(studentId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, studentId));

        if(student.getRole() != User.Role.STUDENT){
            throw new TutorException(DOUBT_USER_IS_NOT_A_STUDENT);
        }
        return new Doubt(student, creationDate, content, isNew, discussion);

    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DiscussionDto createDiscussion(DiscussionDto discussionDto, Integer questionId, Integer studentId){
        if(studentId == null){
            throw new TutorException(ErrorMessage.DOUBT_USER_IS_EMPTY);
        }

        if(questionId == null){
            throw new TutorException(ErrorMessage.DOUBT_QUESTION_IS_EMPTY);
        }
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionId));

        QuestionAnswer questionAnswer = quizQuestion.getQuestionAnswerofUser(studentId);

        Discussion discussion = new Discussion(questionAnswer, questionAnswer.getQuizQuestion().getQuestion().getTitle());
        for(DoubtDto doubtDto : discussionDto.getPostsDto()){
            discussion.addPost(createDoubt(doubtDto, studentId, discussion));
        }
        entityManager.persist(discussion);
        return new DiscussionDto(discussion);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<DiscussionDto> findQuizQuestionDiscussions(Integer questionQuestionId){
        if (questionQuestionId == null){
            throw new TutorException(DOUBT_USER_IS_EMPTY);
        }
        List<DiscussionDto> discussions = new ArrayList<>();
        QuizQuestion quizQuestion = quizQuestionRepository.findById(questionQuestionId).orElseThrow(() -> new TutorException(QUESTION_NOT_FOUND, questionQuestionId));

        Set<QuestionAnswer> questionAnswerList = quizQuestion.getQuestionAnswers();
        for(QuestionAnswer questionAnswer : questionAnswerList){
            discussions.addAll(discussionRepository.findQuestionAnswerDiscussions(questionAnswer.getId()).stream().map(DiscussionDto::new).collect(Collectors.toList()));
        }
        return discussions.stream().filter(discussionDto -> discussionDto.getVisibility().equals(Discussion.Visibility.PUBLIC)).collect(Collectors.toList());
    }

    public List<DiscussionDto> findUserDiscussions(Integer userId){
        if(userId == null){
            throw new TutorException(DOUBT_USER_IS_EMPTY);
        }
        List<DiscussionDto> userDiscussion = discussionRepository.findUserDiscussions(userId).stream().map(DiscussionDto::new).collect(Collectors.toList());
        return userDiscussion;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DiscussionDto changeVisibility(Integer discussionId, Discussion.Visibility status) {
        Discussion discussion = discussionRepository.findById(discussionId).orElseThrow(()-> new TutorException(DOUBT_NOT_FOUND));
        discussion.setVisibility(status);
        return new DiscussionDto(discussion);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<DiscussionDto> findCourseExecutionDiscussions(List<CourseExecution> courseExec){
        //return doubtRepository.getDoubts().stream().filter( doubt -> courseExec.stream().anyMatch(doubt.getQuestion().getCourse().getCourseExecutions()::contains)).map(DoubtDto::new).collect(Collectors.toList());
        if(!courseExec.isEmpty()) {
            List<DiscussionDto> discussions = discussionRepository.getDiscussions().stream()
                    .filter(discussion -> !courseExec.contains(discussion.getQuestionAnswer().getQuizQuestion().getQuiz().getCourseExecution()))
                    .map(DiscussionDto::new)
                    .collect(Collectors.toList());

            for (DiscussionDto discussion: discussions) {
                for (DoubtDto d : discussion.getPostsDto()) {
                    d.setClarificationDto(clarificationService.findDoubtClarification(d.getId()));
                }
            }

            return discussions;

        }
            else {
            return new ArrayList<DiscussionDto>();
        }
    }


}
