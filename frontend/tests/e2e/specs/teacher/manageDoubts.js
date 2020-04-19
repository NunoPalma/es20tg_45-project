describe('teacher doubt management walkthrough', () => {
  beforeEach(() => {
    cy.demoTeacherLogin();
    cy.contains('Management').click();
    cy.contains('Doubts').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a clarification for a unsolved doubt', () => {
    cy.createClarification('UNSOLVED', 'A default clarification for a doubt');
  });

  it('login creates a clarification for a unsolved doubt without text', () => {
    cy.createClarification('UNSOLVED', '');

    cy.log('close error message');
    cy.closeErrorMessage();

    cy.log('close dialog');
    cy.get('[data-cy="cancelButton"]').click();
  });
});
