describe('Discussions walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Solved').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a discussion', () => {
    cy.contains('Component').click();
    cy.createDiscussion('Title Test', 'Input Test');
  });

  it('login creates two doubts in two different questions', () => {
    cy.contains('Component').click();
    cy.createDiscussion('Title Test', 'Input Test');
    cy.get('[data-cy="rightButton"]').click();
    cy.createDiscussion('Title Test2', 'Input Test2');
  });

  it('login creates an empty discussion', () => {
    cy.contains('Component').click();
    cy.createDiscussion('Title Test', null);
  });

  it('login creates a discussion and a teacher responds to it', () => {
    cy.contains('Component').click();
    cy.createDiscussion('Title Test', 'Input Test');
    cy.get('[data-cy="logoutButton"').click();
    cy.demoTeacherLogin();
    cy.contains('Management').click();
    cy.contains('Discussions').click();
    cy.contains('ComponentAndConnectorOne')
      .get('[data-cy="createButton"]')
      .first()
      .click({ force: true });
    cy.get('[data-cy="ResponseInput"]').type('Response');
    cy.get('[data-cy="saveButton"]').click();
    cy.get('[data-cy="logoutButton"').click();
    cy.demoStudentLogin();
    cy.get('[data-cy="studentDiscussions"]').click();
  });
});
