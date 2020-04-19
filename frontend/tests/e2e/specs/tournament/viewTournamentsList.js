describe('Administration walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  it('view tournaments', () => {
    cy.viewTournaments();
  });
});