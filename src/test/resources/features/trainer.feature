Feature: Start new game
  As a user,
  I want to be able to start a game,
  In order to start playing the lingo trainer game

  Scenario: A game is started
    When I request to start a new game
    Then A new game is started

  Scenario Outline: Start new round
    Given that I am playing a game
    And the round was won
    And the last word had "<previous length>" letters
    When I start a new round
    Then the word to guess has "<next length>" letters

    Examples:
    |previous length|next length|
    |5              |6          |
    |6              |7          |
    |7              |5          |

    # Failure path
    Given that I am playing a game
    And the round was lost
    Then I cannot start a new round