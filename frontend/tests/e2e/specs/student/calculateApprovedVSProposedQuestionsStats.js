//For this test, we suggest the student to have, at least, 1 Proposed Question
// and 1 Approved Question
describe('Student Sees Proposed/Approved Questions Stats action', () => {
  let title1 = 'Test_Title' + Date.now();
  let title2 = 'Test_Title' + Date.now();
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.wait(2000);
    cy.submitQuestion(title1, 'Content', 'Option');
    cy.wait(2000);
    cy.submitQuestion(title2, 'Content', 'Option');
    cy.wait(2500);
    cy.contains('Logout').click();

    cy.demoTeacherLogin();
    cy.approveQuestion(title1);
    cy.wait(2000);
    cy.rejectQuestion(title2, 'Not Good Enough Test');

    cy.contains('Logout').click();

    cy.visit('/');
    cy.get('[data-cy="studentButton"]').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('Student checks Proposed/Approved Stats in My Questions section', () => {
    cy.contains('My Questions').click();
    cy.log('We know wait a little bit (5 seconds) to check the stats');
    cy.wait(5000);
  });
});