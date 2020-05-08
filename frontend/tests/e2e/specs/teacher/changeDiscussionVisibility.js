describe('teacher discussion management walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Solved').click();
    cy.contains('Component').click();
    cy.createDiscussion('Title Test', 'Input Test');
    cy.contains('Logout').click();
    cy.demoTeacherLogin();
    cy.contains('Management').click();
    cy.contains('Discussions').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login makes a discussion public', () => {
    cy.get('[data-cy="visibilityButton"]')
      .first()
      .click({ force: true });
  });
});
