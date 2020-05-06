describe('Student Resubmit Question action', () => {
  beforeEach(() => {
    cy.demoTeacherLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

//For this test a question with 'Testing Question' title must be previously submitted

  it('teacher approves with Save and Approve option, question becomes available', () => {
    let title = 'Testing Question';

    cy.approveQuestion(title, 'moreContent');

    cy.checkifAvailable(title);

  });

});