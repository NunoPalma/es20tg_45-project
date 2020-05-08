describe('Doubts walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Solved').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  /*it('login creates a doubt', () => {
    cy.contains('Component').click();
    cy.createDoubt1('Title Test', 'Input Test');
  });

  it('login creates two doubts in two different questions', () => {
    cy.contains('Component').click();
    cy.createDoubt1('Title Test', 'Input Test');
    cy.get('[data-cy="rightButton"]').click();
    cy.createDoubt1('Title Test2', 'Input Test2');
  });

  it('login creates an empty doubt', () => {
    cy.contains('Component').click();
    cy.get('[data-cy="newDoubtButton"]').click();
    cy.get('[data-cy="saveButton"]').click();
    cy.contains('Doubt must have Content').parent().find('button').click();
    cy.get('[data-cy="cancelButton"]').click();
  });*/

  it('login creates a doubt, a teacher responds to it and then creates a new doubt again', () => {
    cy.contains('Component').click();
    cy.createDoubt1('Title Test', 'Input Test');
    cy.get('[data-cy="logoutButton"').click();
    cy.demoTeacherLogin();
    cy.contains('Management').click();
    cy.contains('Discussions').click();
    cy.contains('SOAQualities')
      .get('[data-cy="createButton"')
      .click({ multiple: true, force: true });
    cy.get('[data-cy="ResponseInput"]').type('Response');
    cy.get('[data-cy="saveButton"]').click();
    cy.get('[data-cy="logoutButton"').click();
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.get('[data-cy="discussionsButton"]').click();
  });
});
