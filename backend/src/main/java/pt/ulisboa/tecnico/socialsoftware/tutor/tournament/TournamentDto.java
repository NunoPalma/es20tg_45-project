package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.data.annotation.Transient;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TournamentDto implements Serializable {

	private Integer id;
	private Integer key;
	private User creator;
	private CourseExecution courseExecution;
	private String name;
	private String startDate;
	private String endDate;
	private Set<TopicDto> topics = new HashSet<>();
	private Integer numQuestions;
	private Quiz quiz;
	private Tournament.State state;

	@Transient
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public TournamentDto() {}

	public TournamentDto(User creator, CourseExecution courseExecution) {
		this.creator = creator;
		this.courseExecution = courseExecution;
	}

	public TournamentDto(Tournament tournament, boolean deepCopy) {
		this.id = tournament.getId();
		this.key = tournament.getKey();
		this.creator = tournament.getCreator();
		this.courseExecution = tournament.getCourseExecution();
		this.name = tournament.getName();
		this.state = tournament.getState();

		if (tournament.getStartDate() != null)
			this.startDate = tournament.getStartDate().format(formatter);
		if (tournament.getEndDate() != null)
			this.endDate = tournament.getEndDate().format(formatter);

		this.numQuestions = tournament.getNumQuestions();

		if (deepCopy) {
			this.topics = tournament.getTopics().stream()
					.map(TopicDto::new)
					.collect(Collectors.toSet());
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public CourseExecution getCourseExecution() {
		return courseExecution;
	}

	public User getCreator() {
		return creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Set<TopicDto> getTopics() {
		return topics;
	}

	public void setTopics(Set<TopicDto> topics) {
		this.topics = topics;
	}

	public Integer getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(Integer numQuestions) {
		this.numQuestions = numQuestions;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setCourseExecution(CourseExecution courseExecution) {
		this.courseExecution = courseExecution;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Tournament.State getState() {
		return state;
	}

	public void setState(Tournament.State state) {
		this.state = state;
	}

	public LocalDateTime getStartDateDate() {
		if (getStartDate() == null || getStartDate().isEmpty()) {
			return null;
		}
		return LocalDateTime.parse(getStartDate(), formatter);
	}

	public LocalDateTime getEndDateDate() {
		if (getEndDate() == null || getEndDate().isEmpty()) {
			return null;
		}
		return LocalDateTime.parse(getEndDate(), formatter);
	}
}
