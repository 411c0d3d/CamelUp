[//]: # (This Table summarizes method calls implemented by the following Handler / Services / Classes relevant for GameLoop, regarding turn and stage cases. In general, classes with the „Helper“ suffix get used by Handlers etc. and don’t offer methods to be called directly by GameLoop.)

[//]: # ()
[//]: # (Involved Classes:)

[//]: # ()
[//]: # (- GameState)

[//]: # (- Pyramid)

[//]: # (- ActionValidationService)

[//]: # (- DicePyramid)

[//]: # (- PlayerHandler)

[//]: # (- PlayerRollHandler)

[//]: # (- PlayerCardHandler)

[//]: # (- PlayerBetHandler)

[//]: # (- FinalBetHandler)

[//]: # ()
[//]: # (**Table of Content**)

[//]: # ()
[//]: # (1. **Stage or GameLoop Related Method Calls &#40;Appendable&#41;**)

[//]: # (2. **Turn Related Method Calls &#40;Appendable&#41;**)

[//]: # (3. **List of Important Events for branching out in the GameLoop &#40;Appendable&#41;**)

[//]: # ()
[//]: # (**1\. Stage or GameLoop Related Method Calls &#40;Appendable&#41;**)

[//]: # ()
[//]: # (- - GameState)

[//]: # (    - isGameWithinTurnsLimit&#40;&#41;)

[//]: # (    - // actually we have to have a general game timer that gets initialized with gameState maxGameDuration, counts that down. When it hits 0: branch out and call EndGameRatingService. performEndGameRatingForAllPlayers&#40;&#41; 🡪 phase finished)

[//]: # (    - Pyramid)

[//]: # (        - isEmpty&#40;&#41;)

[//]: # (        - initialLineupRefill&#40;&#41;)

[//]: # (        - regularStageRefill&#40;&#41;)

[//]: # (        - handleCamelLineupRoll&#40;&#41;)

[//]: # (    - PlayerHandler)

[//]: # (        - hasGameSufficientPlayingPlayers&#40;&#41;)

[//]: # (        - determineCurrentPlayerForTurn&#40;&#41;)

[//]: # (        - endCurrentPlayersTurn&#40;&#41;)

[//]: # ()
[//]: # (**2\. Turn Related Method Calls &#40;Appendable&#41;**)

[//]: # ()
[//]: # (- - GameState)

[//]: # (    - incrementTurnCount&#40;&#41;)

[//]: # (    - ActionValidationService)

[//]: # (        - isPlayersTurn&#40;&#41;)

[//]: # (        - isPlacingFinalBetValid&#40;&#41;)

[//]: # (        - isPlacingBettingCardValid&#40;&#41;)

[//]: # (        - isRollingValid&#40;&#41;)

[//]: # (        - isPlacingPlayerCardValid&#40;&#41;)

[//]: # (    - DicePyramid)

[//]: # (        - roll&#40;&#41;)

[//]: # (    - PlayerHandler)

[//]: # (        - penalizePlayerDisconnect)

[//]: # (        - penalizeIllegalMove)

[//]: # (    - PlayerRollHandler)

[//]: # (        - handlePlayerRoll&#40;&#41;)

[//]: # (    - PlayerCardHandler)

[//]: # (        - placePlayerCard&#40;&#41;)

[//]: # (    - PlayerBetHandler)

[//]: # (        - handleStageBet&#40;&#41;)

[//]: # (    - FinalBetHandler)

[//]: # (        - handleFinalBet&#40;&#41;)

[//]: # (    - BoardSpaceCourseHelper)

[//]: # (      - isFinishLineReached&#40;&#41;)

[//]: # ()
[//]: # (**3\. List of Important Events for branching out in the GameLoop &#40;Appendable&#41;**)

[//]: # ()
[//]: # (- if ActionValidationService returns false within turn time 🡪 PlayerHandler has to penalize player.)

[//]: # (  - If player was actually the current player 🡪 Turn has to end &#40;phase visualizing needs to be performed as well because some bets and cards etc. of he player might have been revoked due to penalty&#41;)

[//]: # (  - If player was not the current player 🡪 just penalize him while waiting for Action of current player.)

[//]: # (    - - after phase visualizing prepare next turn in GameLoop)

[//]: # (- if ActionValidationService returns true within turn time 🡪 perform the action by calling the dedicated Handler)

[//]: # (    - after phase visualizing prepare next turn in GameLoop)

[//]: # (- if turn timer runs out: PlayerHandler has to penalize player 🡪 Turn ends and visualizing needs to be performed because some bets and cards etc. of he player might have been revoked due to penalty.)

[//]: # (    - after phase visualizing prepare next turn in GameLoop)

[//]: # (- if Pyramid.isEmpty&#40;&#41; returns true 🡪 call StageRatingService. performStageRatingForAllPlayers&#40;&#41; --> StageRating has been done 🡪 call StageResetService.resetStage&#40;&#41;🡪 visualize again  🡪 prepare next turn in GameLoop)

[//]: # (- if GameState. isGameWithinTurnsLimit&#40;&#41; returns false 🡪 max number of turns has been exceeded 🡪 Game has to end, call EndGameRatingService. performEndGameRatingForAllPlayers&#40;&#41; 🡪 set GameLoop to phase finished &#40;which visualizes the EndGameRatingService and then terminate the Loop&#41;)

[//]: # (- if boardSpacesCourseHelper.isFinishLineReached&#40;&#41; returns true 🡪 Game has to end, call EndGameRatingService.performEndGameRatingForAllPlayers&#40;&#41; 🡪 set GameLoop to phase finished &#40;which visualizes the EndGameRatingService and then terminate the Loop&#41;)

[//]: # (- if PlayerHandler.hasGameSufficientPlayingPlayers&#40;&#41; returns false 🡪 end game by "admin" --> we will randomize TODO the winner order &#40;isnteead of admin deciding it&#41;)

[//]: # (- if gameLoop general Timer exceeds the gameState maxGameDuration 🡪 Game has to end, call EndGameRatingService.performEndGameRatingForAllPlayers&#40;&#41; 🡪 set GameLoop to phase finished &#40;which visulizes the EndGameRatingService and then terminate the Loop&#41;)

[//]: # (  )
[//]: # (- SALAH 19.1. TODO if Admin hits PAUSED --> Pause GameLoopTimer &#40;wich tracks the general game time&#41; AND TurnTimer &#40;tracks the "playing" phase&#41; --> Pause ONLY ALLOWED during "PLAYING")

[//]: # (- SALAH 19.1. TODO gamestate gameDuration = gameDuration has to be set to gameloop time and broadcasted each time we broadcast "visualize" or "playing" GameState)

[//]: # (gameDuration gibt die aktuelle Gesamtspieldauer, abzüglich der Pausenzeit, in Millisekunden,)

[//]: # (    an. Der Wert ist eine Ganzzahl. Initial ist der Wert 0. Pausenzeit ist die Zeit, die vergeht,)

[//]: # (    während die gamePhase des GameState paused ist.)

[//]: # (- SALAH 19.1. TODO gamestate moveTimeRemaining = moveTimeRemaining has to be set to gameconfig thinkingTime everytime we broadcast "palying", so a player counts down from thinkingTime while doping turn / waiting on others to do their turn. moveTimeRemaining should be set to 0 if we broadcast "visualize")

[//]: # (  moveTimeRemaining gibt die verbleibende Zeit in Millisekunden an, die der aktuelle Spieler)

[//]: # (  hat, um seinen Zug zu tätigen. Der Wert ist eine Ganzzahl. Ist gerade kein Spieler am Zug, so)

[//]: # (  ist der Wert kleiner als −1. Wurde der vorherige Zug durch Zeitüberschreitung abgebrochen)

[//]: # (  und wurde der neue Zug noch nicht begonnen, so ist der Wert −1.)