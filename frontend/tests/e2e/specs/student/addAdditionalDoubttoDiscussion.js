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
    cy.get('[data-cy="createButton"]')
      .last()
      .click({ force: true });
    cy.get('[data-cy="ResponseInput"]').type(
      'A simple clarification for a doubt111'
    );
    cy.get('[data-cy="saveButton"]').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a additional doubt for a open and unsolved discussion and teacher solve it', () => {
    cy.demoStudentLogin();
    cy.get('[data-cy="studentDiscussions"]').click();
    cy.get('[data-cy="seeDoubtButton"]')
      .last()
      .click();
    cy.get('[data-cy="doubtinput"]').type('additional doubt');
    cy.get('[data-cy="saveButton"]').click();
    cy.contains('Logout').click();
    cy.demoTeacherLogin();
    cy.contains('Management').click();
    cy.contains('Discussions').click();
    cy.get('[data-cy="createButton"]')
        .last()
        .click({ force: true });
    cy.get('[data-cy="ResponseInput"]').type('A simple clarification for a doubt');
    cy.get('[data-cy="saveButton"]').click();
  });
});
