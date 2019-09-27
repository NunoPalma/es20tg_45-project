package pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.dto.QuizDto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ExceptionError.QUIZ_HAS_ANSWERS;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ExceptionError.QUIZ_NOT_CONSISTENT;

@Entity
@Table(
        name = "quizzes",
        indexes = {
                @Index(name = "quizzes_indx_0", columnList = "number")
        })
public class Quiz implements Serializable {
    public enum QuizType {
        EXAM, TEST, STUDENT, TEACHER
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "available_date")
    private LocalDateTime availableDate;

    @Column(name = "conclusion_date")
    private LocalDateTime conclusionDate;

    @Column(columnDefinition = "boolean default false")
    private boolean scramble = false;

    @Column(nullable = false)
    private String title = "Title";

    private Integer number;
    private Integer year;
    private String type;
    private Integer series;
    private String version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz", fetch = FetchType.LAZY)
    private Set<QuizQuestion> quizQuestions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz", fetch = FetchType.LAZY)
    private Set<QuizAnswer> quizAnswers = new HashSet<>();

    public Quiz() {}

    public Quiz(QuizDto quiz) {
        checkQuestions(quiz.getQuestions());

        this.number = quiz.getNumber();
        setTitle(quiz.getTitle());
        this.type = quiz.getType();
        this.scramble = quiz.getScramble();
        this.creationDate = quiz.getCreationDate();
        setAvailableDate(quiz.getAvailableDate());
        setConclusionDate(quiz.getConclusionDate());
        this.year = quiz.getYear();
        this.series = quiz.getSeries();
        this.version = quiz.getVersion();
    }

    private void checkTitle(String title) {
        if (title == null || title.trim().length() == 0) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Title");
        }
    }

    private void checkAvailableDate(LocalDateTime availableDate) {
        if (this.type.equals(QuizType.TEACHER.name()) && availableDate == null) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Available date");
        }
        if (this.type.equals(QuizType.TEACHER.name()) && this.conclusionDate != null && conclusionDate.isBefore(availableDate)) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Available date");
        }
    }

    private void checkConclusionDate(LocalDateTime conclusionDate) {
        if (this.type.equals(QuizType.TEACHER.name()) &&
            conclusionDate != null &&
            availableDate != null &&
            conclusionDate.isBefore(availableDate)) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Conclusion date " + conclusionDate + availableDate);
        }
    }

    private void checkQuestions(List<QuestionDto> questions) {
        if (questions != null) {
            for (QuestionDto questionDto : questions) {
                if (questionDto.getSequence() != questions.indexOf(questionDto) + 1) {
                    throw new TutorException(QUIZ_NOT_CONSISTENT, "sequence of questions not correct");
                }
            }
        }
    }

    public void remove() {
        getQuizQuestions().forEach(QuizQuestion::remove);
        quizQuestions.clear();
    }

    public void checkCanRemove() {
        if (!quizAnswers.isEmpty()) {
            throw new TutorException(QUIZ_HAS_ANSWERS);
        }
        getQuizQuestions().forEach(QuizQuestion::checkCanRemove);
    }

    public void generate(int quizSize, List<Question> activeQuestions) {
        int numberOfActiveQuestions = activeQuestions.size();
        Set <Integer> usedQuestions = new HashSet<>();

        int numberOfQuestions = 0;
        while (numberOfQuestions < quizSize) {
            int next = new Random().nextInt(numberOfActiveQuestions);
            if(!usedQuestions.contains(next)) {
                usedQuestions.add(next);
                new QuizQuestion(this, activeQuestions.get(next), numberOfQuestions++);
            }
        }

        this.setCreationDate(LocalDateTime.now());
        this.setType(QuizType.STUDENT.name());

        // TODO change based on fenix info
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        if (calendar.get(Calendar.MONTH) < Calendar.AUGUST) {
            year -= 1;
        }

        this.setYear(year);
        this.title = "Generated Quiz";
    }

    public Integer getId() {
    return id;
    }

    public void setId(Integer id) {
    this.id = id;
    }

    public Integer getNumber() {
    return number;
    }

    public void setNumber(Integer number) {
    this.number = number;
    }

    public boolean getScramble() {
    return scramble;
    }

    public void setScramble(boolean scramble) {
    this.scramble = scramble;
    }

    public String getTitle() {
    return title;
    }

    public void setTitle(String title) {
        checkTitle(title);
        this.title = title;
    }

    public LocalDateTime getCreationDate() {
    return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
    }

    public LocalDateTime getAvailableDate() {
    return availableDate;
    }

    public void setAvailableDate(LocalDateTime availableDate) {
        checkAvailableDate(availableDate);
        this.availableDate = availableDate;
    }

    public LocalDateTime getConclusionDate() {
    return conclusionDate;
    }

    public void setConclusionDate(LocalDateTime conclusionDate) {
        checkConclusionDate(conclusionDate);
        this.conclusionDate = conclusionDate;
    }

    public Integer getYear() {
    return year;
    }

    public void setYear(Integer year) {
    this.year = year;
    }

    public String getType() {
    return type;
    }

    public void setType(String type) {
    this.type = type;
    }

    public Integer getSeries() {
    return series;
    }

    public void setSeries(Integer series) {
    this.series = series;
    }

    public String getVersion() {
    return version;
    }

    public void setVersion(String version) {
    this.version = version;
    }

    public Set<QuizQuestion> getQuizQuestions() {
    return quizQuestions;
    }

    public void setQuizQuestions(Set<QuizQuestion> quizQuestions) {
    this.quizQuestions = quizQuestions;
    }

    public Set<QuizAnswer> getQuizAnswers() {
    return quizAnswers;
    }

    public void setQuizAnswers(Set<QuizAnswer> quizAnswers) {
    this.quizAnswers = quizAnswers;
    }

    public void addQuizQuestion(QuizQuestion quizQuestion) {
        if (quizQuestions == null) {
            quizQuestions = new HashSet<>();
        }
        this.quizQuestions.add(quizQuestion);
    }

    public void addQuizAnswer(QuizAnswer quizAnswer) {
        if (quizAnswers == null) {
            quizAnswers = new HashSet<>();
        }
        this.quizAnswers.add(quizAnswer);
    }
}