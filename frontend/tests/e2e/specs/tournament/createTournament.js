describe('Create Tournament walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click({ force: true });
  });

  it('login creates tournament', () => {
    cy.viewTournaments();
    cy.createTournament(
      'MyTournament - Christmas Special',
      '2020-05-20 12:40:00',
      '2020-05-24 17:00:00',
      'GNU Mailman',
        5
    );
  });

  it('login creates tournament with empty name', () => {
    cy.viewTournaments();
    cy.createTournament(
      ' ',
      '2020-05-20 12:40:00',
      '2020-05-24 17:00:00',
      'GNU Mailman',
        5
    );

    cy.closeErrorMessage();
  });

  it('login creates tournament with empty start date', () => {
    cy.viewTournaments();
    cy.createTournament(
      'MyTournament - Christmas Special',
      ' ',
      '2020-05-24 17:00:00',
      'GNU Mailman',
        5
    );

    cy.closeErrorMessage();
  });

  it('login creates tournament with empty end date', () => {
    cy.viewTournaments();
    cy.createTournament(
      'MyTournament - Christmas Special',
      '2020-05-20 12:40:00',
      ' ',
      'GNU Mailman',
        5
    );

    cy.closeErrorMessage();
  });

  it('login creates tournament with no topics', () => {
    cy.viewTournaments();
    cy.createTournamentNoTopics(
      'MyTournament 2 - Electric Boogaloo',
      '2020-05-20 12:40:00',
      '2020-05-24 17:00:00'
    );

    cy.closeErrorMessage();
  });
});
