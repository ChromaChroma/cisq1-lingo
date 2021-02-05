Feature: Start new game
  As a user,
  I want to be able to start a game,
  In order to start playing the lingo trainer game

  Scenario: A game is started
    When I request to start a new game
    Then A new game is started