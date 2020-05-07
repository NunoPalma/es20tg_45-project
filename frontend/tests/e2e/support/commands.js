// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This is will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add('demoTeacherLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="demoTeacherButton"]').click();
});


Cypress.Commands.add('demoStudentLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="studentButton"]').click();
  cy.contains('My Questions').click();
});

Cypress.Commands.add('demoTeacherLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="teacherButton"]').click();
  cy.contains('Management').click();
});


Cypress.Commands.add('createDoubt1', (content) => {

  cy.get('[data-cy="newDoubtButton"]').click();
  cy.get('[data-cy=Content').type(content);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('createClarification', (status, response) => {
  cy.get('[data-cy="createButton"]').click({ multiple: true });
  cy.get('[data-cy="Response"]').type(response);
  cy.get('[data-cy="saveButton"]').click();
});


Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click();
  cy.get('[data-cy="courseExecutionNameInput"]').type(name);
  cy.get('[data-cy="courseExecutionAcronymInput"]').type(acronym);
  cy.get('[data-cy="courseExecutionAcademicTermInput"]').type(academicTerm);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('closeErrorMessage', () => {
  cy.contains('Error')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add('closeClarificationErrorMessage', () => {
  cy.contains('Clarification must have a text')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add('createDoubt2', description => {
  cy.demoStudentLogin();
  cy.get('[data-cy="QuizzesButton"]').click();
  cy.contains('Solved').click();

  cy.contains('Component-and-connector viewtype')
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 4)
    .find('[data-cy="goButton"]')
    .click();

  cy.get('[data-cy="newDoubtButton"]').click();
  cy.get('[data-cy="Content"]').type(description);
  cy.get('[data-cy="saveButton"]').click();
  cy.contains('Logout').click();
});

Cypress.Commands.add('closeErrorMessage', () => {
  cy.contains('Error')
    .parent()
    .find('button')
    .click();
});

  Cypress.Commands.add(
    'createFromCourseExecution',
    (name, acronym, academicTerm) => {
      cy.contains(name)
        .parent()
        .should('have.length', 1)
        .children()
        .should('have.length', 7)
        .find('[data-cy="createFromCourse"]')
        .click();
      cy.get('[data-cy="Acronym"]').type(acronym);
      cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
      cy.get('[data-cy="saveButton"]').click();
    }
  );


//Commands for Submit Question Test

  Cypress.Commands.add('submitQuestion', (title, content, option) => {
    cy.get('[data-cy="submitButton"]').click({ force: true });
    cy.get('[data-cy="Title"]').type(title, { force: true });
    cy.get('[data-cy="Content"]').type(content);
    cy.get('[data-cy="Correct"]').eq(0).click({ force: true });
    cy.get('[data-cy="Option"]').eq(0).type(option);
    cy.get('[data-cy="Option"]').eq(1).type(option);
    cy.get('[data-cy="Option"]').eq(2).type(option);
    cy.get('[data-cy="Option"]').eq(3).type(option);
    cy.get('[data-cy="saveButton"]').click();
  });

  Cypress.Commands.add('submitQuestionNoCorrect', (title, content, option) => {
    cy.get('[data-cy="submitButton"]').click();
    cy.get('[data-cy="Title"]').type(title, { force: true });
    cy.get('[data-cy="Content"]').type(content);
    cy.get('[data-cy="Option"]').eq(0).type(option);
    cy.get('[data-cy="Option"]').eq(1).type(option);
    cy.get('[data-cy="Option"]').eq(2).type(option);
    cy.get('[data-cy="Option"]').eq(3).type(option);
    cy.get('[data-cy="saveButton"]').click();
  });


  Cypress.Commands.add('deleteQuestion', (title) => {
    cy.contains(title)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 7)
      .find('[data-cy="deleteQuestion"]')
      .click({ force: true });
  });

  Cypress.Commands.add('checkOrderTwoQuestions', (title1, title2) => {
    cy.contains(title1)
      .parent()
      .should('have.length', 1)
      .parent()
      .should('have.length', 1)
      .contains(title2);
  });


  Cypress.Commands.add( 'resubmitQuestion', (title, newContent) => {
    cy.contains(title)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 7)
      .find('[data-cy="resubmit"]')
      .click({ force: true });
    cy.get('[data-cy="Content"]').type(newContent);
    cy.get('[data-cy="saveButton"]').click({force:true});
  });


  Cypress.Commands.add('approveQuestion', (title, newContent) => {
    cy.get('[data-cy="Management"]').click();
    cy.get('[data-cy="Evaluate"]').click();
    cy.contains(title)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 7)
      .find('[data-cy="evaluateQuestion"]')
      .click({ force: true });
    cy.get('[data-cy="approve"]').click({ force: true });
    cy.get('[data-cy="saveQuestion"]').click({ force: true });
  });

  Cypress.Commands.add('checkIfAvailable', (title) => {
    cy.get('[data-cy="Management"]').click();
    cy.get('[data-cy="Questions"]').click();
    cy.contains(title)
      .parent()
      .should('have.length', 1);
  });

  Cypress.Commands.add('rejectQuestion', (title, justification) => {
    cy.contains(title)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 7)
      .find('[data-cy="evaluateQuestion"]')
      .click({ force: true });
    cy.get('[data-cy="reject"]').click({force:true});
    cy.get('[data-cy="justification"]').type(justification);
    cy.get('[data-cy="saveEvaluation"]').click({ force: true });
  });

  Cypress.Commands.add('viewTournaments', () => {
    cy.get('[data-cy="Quizzes"]').click({ force: true });
    cy.get('[data-cy="Tournaments"]').click({ force: true });
  });

  Cypress.Commands.add('createTournament', (name, startDate, endDate, topicName) => {
    cy.get('[data-cy="createButton"]').click();

    // insert in text fields
    cy.get('[data-cy="Name"]').click({ force: true }).type(name);
    cy.get('[data-cy="startDate"]').click({ force: true }).type(startDate);
    cy.get('[data-cy="endDate"]').click({ force: true }).type(endDate);

    // select a topic
    cy.contains(topicName)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 2)
      .find('[data-cy="checkTopic"]')
      .click({ force: true });

    // select the number of questions
    cy.get('[data-cy="Questions10"]').click({ force: true });

    // save the tournament
    cy.get('[data-cy="saveButton"]').click({ force: true });
  });

  Cypress.Commands.add('createTournamentNoTopics', (name, startDate, endDate) => {
    cy.get('[data-cy="createButton"]').click();

    // insert in text fields
    cy.get('[data-cy="Name"]').type(name);
    cy.get('[data-cy="startDate"]').click({ force: true }).type(startDate);
    cy.get('[data-cy="endDate"]').click({ force: true }).type(endDate);

    // select the number of questions
    cy.get('[data-cy="Questions10"]').click({ force: true });

    // save the tournament (should throw error)
    cy.get('[data-cy="saveButton"]').click({ force: true });
  });

  Cypress.Commands.add('enrollStudent', (tournamentName) => {
    let search = '[data-cy="' + tournamentName + '"]';
    cy.get(search).click({ force: true });
  });
