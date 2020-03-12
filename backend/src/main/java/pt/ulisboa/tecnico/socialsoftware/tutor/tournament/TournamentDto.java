package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TournamentDto {

	private User creator;
	private CourseExecution courseExecution;
	private String name;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Set<Topic> topics;
	private Integer numQuestions;

	private Set<User> participants;

	public TournamentDto() {
		participants = new HashSet<>();
	}

	public TournamentDto(User creator, CourseExecution courseExecution) {
		participants = new HashSet<>();
		this.creator = creator;
		this.courseExecution = courseExecution;
	}

	public TournamentDto(Tournament tournament) {
		;
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

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	public Integer getNumQuestions() {
		return numQuestions;
	}

	public void setNumQuestions(Integer numQuestions) {
		this.numQuestions = numQuestions;
	}
}
