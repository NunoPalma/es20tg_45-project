package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class TournamentService {

	@Autowired
	private CourseExecutionRepository courseExecutionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private TournamentRepository tournamentRepository;

	@Autowired
	private TopicRepository topicRepository;

	@PersistenceContext
	EntityManager entityManager;

	public TournamentService() {}

	public Integer getMaxTournamentKey() {
		Integer maxTournamentKey = tournamentRepository.getMaxTournamentKey();
		return maxTournamentKey != null ? maxTournamentKey : 0;
	}

	@Retryable(
			value = { SQLException.class },
			backoff = @Backoff(delay = 5000))
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public TournamentDto createTournament(int userId, int courseExecutionId, List<String> topicNames, TournamentDto tournamentDto) {
		CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND, courseExecutionId));

		User user = userRepository.findById(userId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, userId));

		Set<Topic> topics = new HashSet<>();
		for (String topicName : topicNames) {
			Course c = courseExecution.getCourse();
			Integer id = c.getId();
			topics.add(topicRepository.findTopicByName(id, topicName));
		}

		if (user.getRole() != User.Role.STUDENT)
			throw new TutorException(TOURNAMENT_CREATOR_IS_NOT_STUDENT);

		if (tournamentDto.getKey() == null) {
			tournamentDto.setKey(getMaxTournamentKey() + 1);
		}

		Tournament tournament = new Tournament(tournamentDto);
		tournament.setCourseExecution(courseExecution);
		tournament.setCreator(user);
		tournament.setTopics(topics);

		return new TournamentDto(tournament, true);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void enrollStudent(Integer userId, Integer tournamentId) {
		if (userId == null)
			throw new TutorException(INVALID_USER_ID);
		else if (tournamentId == null)
			throw new TutorException(INVALID_TOURNAMENT_ID);

		User user = userRepository.findById(userId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, userId));

		if (user.getRole() != User.Role.STUDENT)
			throw new TutorException(INVALID_ENROLLMENT_ATTEMPT_NOT_STUDENT);

		Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new TutorException(TOURNAMENT_NOT_FOUND, tournamentId));

		tournament.enrollStudent(user);
	}
}
