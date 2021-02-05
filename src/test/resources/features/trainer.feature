Feature: Lingo trainer
  As a user,
  I want to be able to start a game,
  In order to start playing the lingo trainer game

  Scenario: Start new game
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

    # Failure path: Start round when round over
    Given that I am playing a game
    And the round was lost
    Then I cannot start a new round

  Scenario Outline: Guessing a word
    Given that I am playing a game
    And the round is not over
    And I have guessed less than 5 times
    When I guess the word "<guess>"
    And the word exists
    And the word is the correct amount of length
    Then I the "<guess>" is compared to "<word>"
    And I recieve the feedback "<feedback>" of what letters are right, absent in the word, or present but misplaced

    Examples:
    |word   |guess  |feedback                                                 |
    |cookie |cookie |correct - correct - correct - correct - correct - correct|
    |fridge |facade |correct - absent - absent - absent - present - correct   |
    |files  |karat  |absent - absent - absent - absent - absent               |

  Scenario: Successfull guess
    Given that I am playing a game
    And the round is not over
    And I have guessed less than five times
    When I guess the correct word
    Then the system will calculate the score
    And add the calculated score to the total score

  Scenario: Game over after five wrong guesses
    Given that I am playing a game
    And the round is not over
    And I have guessed four times this round
    When I guess for the fifth time this round
    And the guess is wrong
    Then the system will end the game

  #Failure path: Guess after word is guessed
    Given that I am playing a game
    And the word this round is guessed
    When I guess a word
    Then the system will not respond with guess feedback

  #Failure path: Guess when player is finished
    Given that I am playing a game
    And the player is finished
    When I guess a word
    Then the system will not respond with guess feedback

  #Failure path: Start new round when still playing
    Given that I am playing a game
    And the round is not over
    When I start a new round
    Then the system will not start a new round

  #Failure path: Start new round without a game
    Given that I am not playing a game
    When I start a new round
    Then the system will not start a new round