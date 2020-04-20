describe('Administration walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  /*
  YOU MIGHT WANT THIS ONE SIR

  afterEach(() => {
    cy.contains('Logout').click();
  });
   */

  it('view tournaments', () => {
    cy.viewTournaments();
  });
});
