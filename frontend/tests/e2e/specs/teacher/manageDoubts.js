describe('teacher doubt management walkthrough', () => {
  beforeEach(() => {
    cy.createDoubt2('A simple default doubt');
    cy.demoTeacherLogin();
    cy.contains('Management').click();
    cy.contains('Doubts').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a clarification for a unsolved doubt', () => {
    cy.get('[data-cy="createButton"]')
      .first()
      .click({ force: true });
    cy.get('[data-cy="Response"]').type('A simple clarification for a doubt');
    cy.get('[data-cy="saveButton"]').click();
  });

  it('login try to creates a clarification for a unsolved doubt without text', () => {
    cy.get('[data-cy="createButton"]')
      .first()
      .click({ force: true });
    cy.get('[data-cy="saveButton"]').click();

    cy.log('close error message');
    cy.closeClarificationErrorMessage();

    cy.get('[data-cy="Response"]').type('A simple clarification for a doubt');
    cy.get('[data-cy="saveButton"]').click();
  });
});
