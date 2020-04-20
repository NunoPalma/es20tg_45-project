describe('View Tournaments walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click({ force: true });
  });

  it('view tournaments', () => {
    cy.viewTournaments();
  });
});
