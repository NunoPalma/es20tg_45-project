describe('Student Resubmit Question action', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

//For this test a question with 'One Question' must be submitted previously and rejected

  it('student resubmits a question', () => {
    let title = 'One Question';

    cy.resubmitQuestion(title, 'NewContent');

  });

});