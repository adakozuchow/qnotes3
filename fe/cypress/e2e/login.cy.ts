describe('Login', () => {
  beforeEach(() => {
    // Clear local storage and visit login page before each test
    cy.clearLocalStorage();
    cy.visit('/auth/login');
  });

  it('should successfully login with valid credentials', () => {
    // Use custom login command
    cy.login('a@a.pl', 'dupadupa');

    // Verify successful login
    cy.url().should('include', '/notes');
    cy.isLoggedIn();

    // Verify the notes page content is loaded
    cy.get('.login-container').should('not.exist');
    cy.get('app-notes-list').should('exist');
  });

  it('should show validation errors for empty fields', () => {
    // Click submit without entering any data
    cy.get('button[type="submit"]').should('be.disabled');

    // Type invalid email and check validation
    cy.get('input[type="email"]')
      .type('invalid')
      .blur();
    cy.get('.error-message')
      .contains('Please enter a valid email')
      .should('be.visible');

    // Clear fields and verify required validation
    cy.get('input[type="email"]').clear().blur();
    cy.get('.error-message')
      .contains('Email is required')
      .should('be.visible');
  });

  it('should show error dialog for invalid credentials', () => {
    // Type invalid credentials
    cy.login('wrong@email.com', 'wrongpassword');

    // Verify error dialog appears
    cy.get('mat-dialog-container')
      .contains('Something went wrong')
      .should('be.visible');
  });
});