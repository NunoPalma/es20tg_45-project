describe('Create Tournament walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click({ force: true });
  });

  it('login creates and cancels tournament', () => {
    cy.viewTournaments();
    cy.createTournament(
      'MyTournament 6 - Return of the Tournament',
      '2020-05-20 12:40:00',
      '2020-05-24 17:00:00',
      'GNU Mailman',
        5
    );

    cy.cancelTournament('MyTournament 6 - Return of the Tournament');
  });
});