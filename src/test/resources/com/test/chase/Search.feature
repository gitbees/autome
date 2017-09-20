Feature: Test Navigation

@go1
Scenario: Test Search 
Given I navigate to the Chase site
And I am on the Chase Home page
When I enter Chase Pay for the Search field
And I select the Click Search button
Then I select the Apple Pay link

@go2
Scenario: Amazon Search 
Given I navigate to the Amazon site
And I am on the Amazon Home page
When I enter Xbox for the Search field
Then I select the Go button