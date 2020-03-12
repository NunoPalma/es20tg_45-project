package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import java.time.LocalDateTime;
import java.util.Set;

public class Tournament {

    private User creator;
    private CourseExecution courseExecution;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<Topic> topics;
    private Integer numQuestions;

    public Tournament(User creator, CourseExecution courseExecution) {
        this.creator = creator;
        this.courseExecution = courseExecution;
    }

    public Tournament(TournamentDto tournamentDto) {
        ;
    }

    public User getCreator() {
        return creator;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
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
