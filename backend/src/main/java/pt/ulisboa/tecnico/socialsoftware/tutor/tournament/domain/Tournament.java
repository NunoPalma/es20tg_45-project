package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Entity
@Table(
        name = "tournaments",
        indexes = {
                @Index(name = "tournaments_idx_0", columnList = "key")
        }
)
public class Tournament {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable = false)
    private Integer key;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "course_execution_id")
    private CourseExecution courseExecution;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToMany
    @JoinTable(
            name = "topics_of_tournament",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics;

    @Column(name = "num_questions")
    private Integer numQuestions;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public Tournament() {}

    public Tournament(User creator, CourseExecution courseExecution) {
        this.creator = creator;
        this.courseExecution = courseExecution;
    }

    public Tournament(TournamentDto tournamentDto) {
        this.key = tournamentDto.getKey();
        this.creator = tournamentDto.getCreator();
        this.courseExecution = tournamentDto.getCourseExecution();
        setName(tournamentDto.getName());
        setStartDate(tournamentDto.getStartDateDate());
        setEndDate(tournamentDto.getEndDateDate());
        setNumQuestions(tournamentDto.getNumQuestions());
        this.topics = new HashSet<>();
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        checkStartDate(startDate);
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        checkEndDate(endDate);
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
        checkNumQuestions(numQuestions);
        this.numQuestions = numQuestions;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    private void checkName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new TutorException(TOURNAMENT_NAME_EMPTY);
        }
    }

    private void checkStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            throw new TutorException(TOURNAMENT_START_DATE_EMPTY);
        }
    }

    private void checkEndDate(LocalDateTime endDate) {
        if (this.startDate == null) {
            throw new TutorException(TOURNAMENT_START_DATE_EMPTY);
        }
        if (endDate == null) {
            throw new TutorException(TOURNAMENT_END_DATE_EMPTY);
        }
        if (endDate.isBefore(this.startDate)) {
            throw new TutorException(TOURNAMENT_INVALID_END_DATE);
        }
        if (endDate.isEqual(this.startDate)) {
            throw new TutorException(TOURNAMENT_DATES_OVERLAP);
        }
    }

    private void checkNumQuestions(int numQuestions) {
        if (numQuestions < 1) {
            throw new TutorException(TOURNAMENT_NOT_ENOUGH_QUESTIONS);
        }
    }
}
