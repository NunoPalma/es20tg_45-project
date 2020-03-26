package pt.ulisboa.tecnico.socialsoftware.tutor.exceptions;

public enum ErrorMessage {
    QUIZ_NOT_FOUND("Quiz not found with id %d"),
    QUIZ_QUESTION_NOT_FOUND("Quiz question not found with id %d"),
    QUIZ_ANSWER_NOT_FOUND("Quiz answer not found with id %d"),
    QUESTION_ANSWER_NOT_FOUND("Question answer not found with id %d"),
    OPTION_NOT_FOUND("Option not found with id %d"),
    QUESTION_NOT_FOUND("Question not found with id %d"),
    USER_NOT_FOUND("User not found with id %d"),
    TOPIC_NOT_FOUND("Topic not found with id %d"),
    ASSESSMENT_NOT_FOUND("Assessment not found with id %d"),
    TOPIC_CONJUNCTION_NOT_FOUND("Topic Conjunction not found with id %d"),
    COURSE_EXECUTION_NOT_FOUND("Course execution not found with name %d"),

    COURSE_NOT_FOUND("Course not found with id %s"),
    COURSE_NAME_IS_EMPTY("The course name is empty"),
    COURSE_TYPE_NOT_DEFINED("The course type is not defined"),
    COURSE_EXECUTION_ACRONYM_IS_EMPTY("The course execution acronym is empty"),
    COURSE_EXECUTION_ACADEMIC_TERM_IS_EMPTY("The course execution academic term is empty"),
    USERNAME_NOT_FOUND("Username %s not found"),

    QUIZ_USER_MISMATCH("Quiz %s is not assigned to student %s"),
    QUIZ_MISMATCH("Quiz Answer Quiz %d does not match Quiz Question Quiz %d"),
    QUESTION_OPTION_MISMATCH("Question %d does not have option %d"),
    COURSE_EXECUTION_MISMATCH("Course Execution %d does not have quiz %d"),

    DUPLICATE_TOPIC("Duplicate topic: %s"),
    DUPLICATE_USER("Duplicate user: %s"),
    DUPLICATE_COURSE_EXECUTION("Duplicate course execution: %s"),

    USERS_IMPORT_ERROR("Error importing users: %s"),
    QUESTIONS_IMPORT_ERROR("Error importing questions: %s"),
    TOPICS_IMPORT_ERROR("Error importing topics: %s"),
    ANSWERS_IMPORT_ERROR("Error importing answers: %s"),
    QUIZZES_IMPORT_ERROR("Error importing quizzes: %s"),

    QUESTION_IS_USED_IN_QUIZ("Question is used in quiz %s"),
    QUIZ_NOT_CONSISTENT("Field %s of quiz is not consistent"),
    USER_NOT_ENROLLED("%s - Not enrolled in any available course"),
    QUIZ_NO_LONGER_AVAILABLE("This quiz is no longer available"),
    QUIZ_NOT_YET_AVAILABLE("This quiz is not yet available"),

    NOT_ENOUGH_QUESTIONS("Not enough questions to create a quiz"),
    QUESTION_MISSING_DATA("Missing information for quiz"), // TODO check me
    QUESTION_MULTIPLE_CORRECT_OPTIONS("Questions can only have 1 correct option"),
    QUESTION_CHANGE_CORRECT_OPTION_HAS_ANSWERS("Can not change correct option of answered question"),
    QUIZ_HAS_ANSWERS("Quiz already has answers"),
    QUIZ_ALREADY_COMPLETED("Quiz already completed"),
    QUIZ_QUESTION_HAS_ANSWERS("Quiz question has answers"),
    FENIX_ERROR("Fenix Error"),
    AUTHENTICATION_ERROR("Authentication Error"),
    FENIX_CONFIGURATION_ERROR("Incorrect server configuration files for fenix"),

    TOURNAMENT_NAME_EMPTY("The tournament name is empty"),
    TOURNAMENT_START_DATE_EMPTY("The tournament start date is empty"),
    TOURNAMENT_END_DATE_EMPTY("The tournament end date is empty"),
    TOURNAMENT_INVALID_END_DATE("The tournament end date is before the start date"),
    TOURNAMENT_DATES_OVERLAP("The tournament's start and end dates overlap"),
    TOURNAMENT_NOT_ENOUGH_QUESTIONS("Not enough questions to create a tournament"),
    NOT_ENOUGH_TOPICS("Not enough topics to create a tournament"),
    TOURNAMENT_CREATOR_IS_NOT_STUDENT("The tournament creator is not a student"),
    INVALID_ENROLLMENT_CLOSED_TOURNAMENT("The tournament is closed"),
    INVALID_ENROLLMENT_CREATED_TOURNAMENT("The tournament hasn't been open yet"),
    INVALID_ENROLLMENT_CANCELLED_TOURNAMENT("The tournament has been cancelled"),
    TOURNAMENT_NOT_FOUND("Tournament with id %d not found"),
    TOPIC_WITH_NAME_NOT_FOUND("Topic with name %s not found"),

    INVALID_USER_ID("Invalid user ID"),
    INVALID_TOURNAMENT_ID("Invalid tournament ID"),
    INVALID_ENROLLMENT_ATTEMPT_NOT_STUDENT("Invalid user role"),
    STUDENT_ALREADY_ENROLLED("The student is already enrolled in the tournament"),

    ACCESS_DENIED("You do not have permission to view this resource");

    public final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}