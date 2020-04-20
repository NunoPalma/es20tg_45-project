describe('Doubts walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Solved').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a doubt', () => {
    cy.contains('Component').click();
    cy.createDoubt('Input Test');
  });

  it('login creates two doubts in two different questions', () => {
    cy.contains('Component').click();
    cy.createDoubt('Input Test1');
    cy.get('[data-cy="rightButton"]').click();
    cy.createDoubt('Input Test2');
  });

  it('login creates an empty doubt', () => {
    cy.contains('Component').click();
    cy.get('[data-cy="createDoubt"]').click();
    cy.get('[data-cy="saveButton"]').click();
    cy.contains('Doubt must have Content').parent().find('button').click();
    cy.get('[data-cy="cancelButton"]').click();
  });
});
