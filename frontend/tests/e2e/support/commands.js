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
});

Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click();
  cy.get('[data-cy="Name"]').type(name);
  cy.get('[data-cy="Acronym"]').type(acronym);
  cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('closeErrorMessage', (name, acronym, academicTerm) => {
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
    cy.get(search).click({force: true});
});
