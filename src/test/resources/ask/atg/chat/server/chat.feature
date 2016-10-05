Feature: As a user I want to be able to chat with my contacts if they are online
  
  Scenario: Start a chat with an online contact
    Given a user exists
    And has a contact
    And contact is online
    And has a message
    And chat history don't exist
    When send message
    Then chat exists
    And has new message 
        
  Scenario: Send a chat message to an online contact
    Given a user exists
    And has a contact
    And contact is online
    And has a message
    And chat history exist
    When send message
    Then chat exists
    And has new message 
    
  Scenario: Get chat history in chronological order
    Given a user exists
    And has a contact
    And chat history exist
    When list chat history chronologically
    Then retrieve chat history