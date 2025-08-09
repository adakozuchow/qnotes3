/// <reference types="cypress" />

// Custom command to login
Cypress.Commands.add('login', (email: string, password: string) => {
    cy.visit('/auth/login');
    cy.get('input[type="email"]').type(email);
    cy.get('input[type="password"]').type(password);
    cy.get('button[type="submit"]').click();
});

// Custom command to check if user is logged in
Cypress.Commands.add('isLoggedIn', () => {
    return cy.window().its('localStorage.token').should('exist');
});

// Declare the custom commands on the global Cypress namespace
declare global {
    namespace Cypress {
        interface Chainable {
            login(email: string, password: string): Chainable<void>
            isLoggedIn(): Chainable<boolean>
        }
    }
}
