describe('Student Resubmit Question action', () => {
  let title1 = 'Test_Title' + Date.now();

  beforeEach(() => {

    cy.demoStudentLogin();
    cy.wait(2000);
    cy.submitQuestion(title1, 'Content', 'Option');
    cy.wait(2000);
    cy.contains('Logout').click();

    cy.demoTeacherLogin();
    cy.wait(2000);
    cy.get('[data-cy="Management"]').click();
    cy.get('[data-cy="Evaluate"]').click();
    cy.rejectQuestion(title1, 'Not Good Enough');

    cy.contains('Logout').click();

    cy.visit('/');
    cy.get('[data-cy="studentButton"]').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });


  it('student resubmits a question', () => {

    cy.contains('My Questions').click();
    cy.resubmitQuestion(title1, 'NewContent');

  });

});