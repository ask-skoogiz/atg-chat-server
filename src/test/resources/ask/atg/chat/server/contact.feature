Feature: As a user I want to be able to add other users as contacts
  
  Scenario: User requests to add another user as contact
    Given a user exists
    And another user exists
    When user adds another user as contact
    Then a contact request is available
    And contacted user is notified

  Scenario: Contacted user accepts contact request
    Given a contact request exists
    And contacted user is notified
    When user accepts contact request
    Then the contact is mutual
    
  Scenario: Contacted user rejects contact request
    Given a contact request exists
    And contacted user is notified
    When user rejects contact request
    Then the request is removed

  Scenario: Get all contacts connected to a user
    Given a user exists
    And has contacts
    When list contacts
    Then return all contacts

  Scenario: Get online contacts connected to a user
    Given a user exists
    And has contacts
    And a contact is online
    When list contacts
    And filter online contacts
    Then return all contacts that are online
    
    