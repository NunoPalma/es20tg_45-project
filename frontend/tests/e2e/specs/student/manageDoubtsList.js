describe('Doubts List walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Solved').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates a doubt and see it on the doubt list', () => {
    cy.contains('Component').click();
    cy.createDoubt1('List Test');
    cy.get('[data-cy="QuizzesButton"]').click();
    cy.contains('Doubts').click();
    cy.contains('SOA').click({force:true});
    cy.contains('Back').click();
  });


});
