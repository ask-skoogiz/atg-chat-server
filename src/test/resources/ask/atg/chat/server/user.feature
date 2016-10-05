Feature: As an user I want to be able to create a unique user account so that I can login to the chat server

  Scenario: Register new user 
    Given I have a new user
    When I create new user
    Then a user is created

  Scenario: Register user doublet
    Given a user exists
    And I have a new user
    When I create new user
    Then a user is not created
    
  Scenario: Fetch user with valid credentials
    Given a user exists
    And I enter valid user credentials
    When I login
    Then the user is authenticated
    And user data is retrieved
    
  Scenario: Fetch user with invalid credentials
    Given a user exists
    And I enter invalid user credentials
    When I login
    Then user authentication fails
    And user data is not retrieved
