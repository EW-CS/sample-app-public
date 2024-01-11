Business Need: Test Pets Store

Feature: Validate Pets Service

  Scenario: Should return List of Pets
    Given I make a GET call on /pets
    Then I should receive 200 response status code
    And should receive a non-empty body


  Scenario: Should return list of Pets from an external API source
    Given Mock GET /petstore returns response with status 200 and body
      """
        [
            {
                "uuid": "31336285-935f-45d2-8e83-497950b77fc4",
                "name": "EX-MOCK-CAT"
            },
            {
                "uuid": "faf1f11c-2a01-407f-8430-3c21cdaf6e32",
                "name": "EX-MOCK-DOG"
            },
            {
                "uuid": "e1910388-7720-4420-b654-c8f594fbaea4",
                "name": "EX-MOCK-BIRDS"
            },
            {
                "uuid": "f5c69bde-afdf-4506-b2f9-1273bf3a0f74",
                "name": "EX-MOCK-RABBITS"
            }
        ]
      """
    When I make a GET call on /pets/external
    Then I should receive 200 response status code
    And verify the response with
      """
        [
            {
                "uuid": "31336285-935f-45d2-8e83-497950b77fc4",
                "name": "EX-MOCK-CAT"
            },
            {
                "uuid": "faf1f11c-2a01-407f-8430-3c21cdaf6e32",
                "name": "EX-MOCK-DOG"
            },
            {
                "uuid": "e1910388-7720-4420-b654-c8f594fbaea4",
                "name": "EX-MOCK-BIRDS"
            },
            {
                "uuid": "f5c69bde-afdf-4506-b2f9-1273bf3a0f74",
                "name": "EX-MOCK-RABBITS"
            }
        ]
      """