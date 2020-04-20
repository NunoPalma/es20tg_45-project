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
  cy.get('[data-cy="demoStudentButton"]').click();
});


Cypress.Commands.add('createDoubt', (content) => {

  cy.get('[data-cy="createDoubt"]').click();
  cy.get('[data-cy=Content').type(content);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('createClarification', (status, response) => {
  cy.get('[data-cy="createButton"]').click({ multiple: true });
  cy.get('[data-cy="Response"]').type(response);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('closeClarificationErrorMessage', () => {
  cy.contains('Clarification must have a text')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add('createDoubt', description => {
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
  cy.contains('error')
    .parent()
    .find('button')
    .click();
});
