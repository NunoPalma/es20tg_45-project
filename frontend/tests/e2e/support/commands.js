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
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
/// <reference types="Cypress" />
Cypress.Commands.add('demoAdminLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="adminButton"]').click();
  cy.contains('Administration').click();
  cy.contains('Manage Courses').click();
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

// Commands for Course Execution test

Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click();
  cy.get('[data-cy="Name"]').type(name);
  cy.get('[data-cy="Acronym"]').type(acronym);
  cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('closeErrorMessage', () => {
  cy.contains('Error')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add('deleteCourseExecution', acronym => {
  cy.contains(acronym)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 7)
    .find('[data-cy="deleteCourse"]')
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
  cy.get('[data-cy="submitButton"]').click();
  cy.get('[data-cy="Title"]').type(title,{force: true});
  cy.get('[data-cy="Content"]').type(content);
  cy.get('[data-cy="Correct"]').eq(0).click({force: true});
  cy.get('[data-cy="Option"]').eq(0).type(option);
  cy.get('[data-cy="Option"]').eq(1).type(option);
  cy.get('[data-cy="Option"]').eq(2).type(option);
  cy.get('[data-cy="Option"]').eq(3).type(option);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('submitQuestionNoCorrect', (title, content, option) => {
  cy.get('[data-cy="submitButton"]').click();
  cy.get('[data-cy="Title"]').type(title,{force: true});
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
    .should('have.length',1)
    .children()
    .should('have.length',7)
    .find('[data-cy="deleteQuestion"]')
    .click({force: true});
});

Cypress.Commands.add('checkOrderTwoQuestions', (title1, title2) => {
  cy.contains(title1)
    .parent()
    .should('have.length', 1)
    .parent()
    .should('have.length', 1)
    .contains(title2);
});

//Commands for Evaluate Question Test
//trying to merge


Cypress.Commands.add('approveQuestion' , (title) => {
  cy.get('[data-cy="Management"]').click();
  cy.get('[data-cy="Evaluate"]').click();
  cy.contains(title)
    .parent()
    .should('have.length',1)
    .children()
    .should('have.length',7)
    .find('[data-cy="evaluateQuestion"]')
    .click({force: true});
  cy.get('[data-cy="approve"]').click({force: true});
  cy.get('[data-cy="saveEvaluation"]').click({force: true});
});

Cypress.Commands.add('rejectQuestion', (title,justification) => {
  cy.contains(title)
    .parent()
    .should('have.length',1)
    .children()
    .should('have.length',7)
    .find('[data-cy="evaluateQuestion"]')
    .click({force: true});
  cy.get('[data-cy="justification"]').type(justification);
  cy.get('[data-cy="saveEvaluation"]').click({force: true});
});

