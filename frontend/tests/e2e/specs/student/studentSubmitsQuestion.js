

describe('Student Submit Question action', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates and submits a question', () => {
    cy.submitQuestion('Title', 'Content', 'Option');

    cy.deleteQuestion('Title');
  });
});