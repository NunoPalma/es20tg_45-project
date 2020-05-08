describe('Discussion List walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Solved').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a discussion and see it on the discussion list', () => {
    cy.contains('Component').click();
    cy.createDiscussion('List Title Test', 'List Test');
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('My Discussions').click();
  });
});
