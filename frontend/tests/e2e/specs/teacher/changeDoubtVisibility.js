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

  it('login makes a doubt public', () => {
    cy.get('[data-cy="visButton"]')
      .first()
      .click({ force: true });
  });
});
