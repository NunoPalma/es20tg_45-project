describe('Student Submit Question action', () => {
  let title1 = 'Test_Title' + Date.now();
  let title2 = 'Test_Title' + Date.now();
  beforeEach(() => {
    cy.demoStudentLogin();

    cy.submitQuestion(title1, 'Content', 'Option');
    cy.submitQuestion(title2, 'Content', 'Option');

    cy.contains('Logout').click();

    cy.demoTeacherLogin();
  });

  //trying to merge

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('approves Question', (title) => {
    cy.approveQuestion(title1);
  });

  it('rejects Question', (title,justification) => {
    cy.rejectQuestion(title2, justification);
  });

});