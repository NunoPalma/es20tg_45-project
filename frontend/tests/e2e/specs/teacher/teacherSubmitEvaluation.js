describe('Teacher submits evaluations action', () => {
  let title1 = 'I will be DISABLED'
  let title2 = 'I will be AVAILABLE'
  let title3 = 'I will be REJECTED'

  beforeEach(() => {
    cy.demoStudentLogin();
    cy.wait(2000);
    cy.submitQuestion(title1, 'Content1', 'Option');
    cy.wait(2000);
    cy.submitQuestion(title2, 'Content2', 'Option');
    cy.wait(2000);
    cy.submitQuestion(title3, 'Content3', 'Option');
    cy.wait(2000);
    cy.contains('Logout').click();

    cy.wait(2000);
    cy.demoTeacherLogin();
  });


  afterEach(() => {
    cy.contains('Logout').click();
    cy.wait(2000);

  });

  it('first question DISABLED, second APPROVED, third REJECTED', (title) => {
    cy.approveQuestion(title1);
    cy.wait(2000);

    cy.approveQuestion2(title2);
    cy.wait(2000);

    cy.checkIfAvailable(title2);
    cy.wait(2000);

    cy.get('[data-cy="Management"]').click();

    cy.get('[data-cy="Evaluate"]').click();

    cy.rejectQuestion(title3, 'finally done!');
    cy.wait(2000);

  });

});