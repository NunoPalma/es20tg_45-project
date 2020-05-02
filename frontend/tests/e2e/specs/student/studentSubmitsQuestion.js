

describe('Student Submit Question action', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  //trying to merge +


  it('login creates and submits a question', () => {
    let title = 'Test_Title' + Date.now();

    cy.submitQuestion(title, 'Content', 'Option');

    cy.deleteQuestion(title);

  });

  it('login creates one normal question and one faulty question', () => {

    let title1 = 'Test_Title' + Date.now();
    let title2 = 'Test_Title' + Date.now();

    cy.submitQuestion(title1, 'Content', 'Option');

    cy.log('try to create with no correct option');
    cy.submitQuestionNoCorrect(title2, 'Content2', 'Option');

    cy.closeErrorMessage();

    cy.log('close dialog');
    cy.get('[data-cy="cancelButton"]').click();

    cy.deleteQuestion(title1);
  });

  it('submits two questions and checks if ordered', () => {
    let title1 = 'Test_Title' + Date.now();
    let title2 = 'Test_Title' + Date.now();

    cy.submitQuestion(title1, 'Content', 'Option');
    cy.submitQuestion(title2, 'Content', 'Option');

    cy.checkOrderTwoQuestions(title1, title2);

    cy.deleteQuestion(title2);
    cy.deleteQuestion(title1);
  });

});