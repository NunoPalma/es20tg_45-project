escribe('Administration walkthrough', () => {
  beforeEach(() => {
    cy.demoStudentLogin();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('login creates tournament', () => {
    cy.createTournament("MyTournament - Christmas Special", "2020-05-20 12:40:00", "2020-05-24 17:00:00", "GNU Mailman");
  });

  it('login creates tournament with empty name', () => {
    cy.createTournament("", "2020-05-20 12:40:00", "2020-05-24 17:00:00", "GNU Mailman");

    cy.closeErrorMessage();
  });

  it('login creates tournament with empty start date', () => {
    cy.createTournament("MyTournament - Christmas Special", "", "2020-05-24 17:00:00", "GNU Mailman");

    cy.closeErrorMessage();
  });

  it('login creates tournament with empty end date', () => {
    cy.createTournament("MyTournament - Christmas Special", "2020-05-20 12:40:00", "", "GNU Mailman");

    cy.closeErrorMessage();
  });

  it('login creates tournament with no topics', () => {
    cy.createTournamentNoTopics("MyTournament 2 - Electric Boogaloo", "2020-05-20 12:40:00", "2020-05-24 17:00:00");

    cy.closeErrorMessage();
  });
});
