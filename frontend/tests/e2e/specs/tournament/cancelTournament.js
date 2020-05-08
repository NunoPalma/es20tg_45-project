describe('Cancel Tournament walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click({ force: true });
  });

  it('login creates and cancels tournament', () => {
    let name = 'New Tournament - 1 minute speedrun, a short movie';
    cy.viewTournaments();
    cy.createTournament(
        name,
        '2020-05-20 12:40:00',
        '2020-05-20 12:41:00',
        'GNU Mailman',
        5
    );
    cy.contains('Logout').click({ force: true });

    cy.demoStudentLogin();
    cy.viewTournaments();

    cy.cancelTournament(name);
  });
});