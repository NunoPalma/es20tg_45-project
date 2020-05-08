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

  it('login creates a clarification for a open and unsolved discussion', () => {
    cy.get('[data-cy="createButton"]')
      .first()
      .click({ force: true });
    cy.get('[data-cy="ResponseInput"]').type('A simple clarification for a doubt');
    cy.get('[data-cy="saveButton"]').click();

  });


  it('login try to creates a clarification for a open discussion without text', () => {
    cy.get('[data-cy="createButton"]')
      .first()
      .click({ force: true });
    cy.get('[data-cy="saveButton"]').click();

    cy.log('close error message');
    cy.closeClarificationErrorMessage();

    cy.get('[data-cy="ResponseInput"]').type('A simple clarification for a doubt');
    cy.get('[data-cy="saveButton"]').click();
  });
});
