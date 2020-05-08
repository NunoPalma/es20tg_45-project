//For this test, the student needs to have his statistics private (as is the default)
describe('Student Toggle Privacy action', () => {
  beforeEach(() => {
    cy.visit('/');
    cy.get('[data-cy="studentButton"]').click();
    cy.contains('Stats').click();
    cy.contains('Course Stats').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('Student toggles privacy to public and checks', () => {
    cy.get('[data-cy="makePublic"]').click({force: true});
    //Now we are going to go to a different page and get back to check that it updated
    cy.log('Now we are going to go to a different page and get back to check that it updated');
    cy.contains('Stats').click();
    cy.contains('My Stats').click();
    //And going back
    cy.log('And going back');
    cy.contains('Stats').click();
    cy.contains('Course Stats').click();
  });

  it('Student toggles privacy to private and checks', () => {
    cy.get('[data-cy="makePrivate"]').click({force: true});
    //Now we are going to go to a different page and get back to check that it updated
    cy.log('Now we are going to go to a different page and get back to check that it updated');
    cy.contains('Stats').click();
    cy.contains('My Stats').click();
    //And going back
    cy.log('And going back');
    cy.contains('Stats').click();
    cy.contains('Course Stats').click();
  });
});