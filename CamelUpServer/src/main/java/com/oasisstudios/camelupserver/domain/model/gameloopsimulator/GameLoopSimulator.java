//package com.oasisstudios.camelupserver.domain.model.Gameloop;
//
//import com.oasisstudios.camelupserver.dataaccess.dto.*;
//import com.oasisstudios.camelupserver.domain.model.GameState;
//import com.oasisstudios.camelupserver.domain.service.IGameStateListener;
//
//import lombok.Getter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class GameLoopSimulator {
//
//    // this Controller Class initializes all the Etappen for a game Loop with their scripted sequences of actions. 
//    // then it runs the Gameloop by initialising the Gamestate Class and returning GameStateDTO Snapshot for each state that is "playing",  "visualising", "finished".
//    // NOTE: A freshly instantiated new GameState() is ALWAYS in "playing" mode, with a  gameboard fully set up according to hardcoded specs!
//    // NOTE: the state "created" represents a created lobby with players. "created" is not directly related to a running game, 
//    // therefore it is NOT included here, since for now we do not consider it part of the actual gameloop (open to different opinions).
//
//    GameState gameState;
//    @Getter
//    GameStateDTO currentGameStateDTO;
//    @Getter
//    ArrayList<GameStateDTO> recentGameStatesList;
//    @Getter
//    ArrayList<ArrayList<GameStateDTO>> recentGameStatesCollections;
//    private final List<IGameStateListener> listeners = new ArrayList<>();
//    private final ArrayList<ArrayList<Object>> etappenList = new ArrayList<>();
//    public GameLoopSimulator() {
//        
//        gameState = new GameState(); // Instantiating the GameState creates a full set up Gameboard with status "playing", ready for Iteration over the EtappenSequenzen
//        
//        etappenList.add(etappe1());
//        etappenList.add(etappe2());
//        etappenList.add(etappe3());
//        etappenList.add(etappe4());
//        etappenList.add(etappe5());
//        
//        this.currentGameStateDTO = new GameStateDTO();
//        this.recentGameStatesList = new ArrayList<>();
//        this.recentGameStatesCollections = new ArrayList<>();
//    }
//
//    public void runGame() {
//        for(var etappe : etappenList) {
//            CreateGameStatePerSequenceChange(etappe);
//        }
//    }
//
//    private void CreateGameStatePerSequenceChange(ArrayList<Object> etappe) {
//        // while (GameStateManager)
//        
//        for (int i = 0; i < etappe.size(); i++) {
//            try {
//                gameState.evalSequenceState(etappe.get(i));
//
//                // this produces a clean GameStateDTO Snapshot, only allowed when state "visualizing" or "playing" or "finished".
//                // you can grab it by the variable currentGameStateDTO
//                if (etappe.get(i).equals("visualizing") || etappe.get(i).equals("playing") || etappe.get(i).equals("finished")) {
//                    this.currentGameStateDTO = gameState.getSnap();
//                    notifyListeners();
//                }
//                // this sets the waiting timer, should be set according to Gameconfig
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    public void addGameStateListener(IGameStateListener listener) {
//        listeners.add(listener);
//    }
//
//    private void notifyListeners() {
//        for (IGameStateListener listener : listeners) {
//            listener.onGameStateChanged(this.currentGameStateDTO);
//        }
//    }
//    
//    private ArrayList<Object> etappe1(){
//        var data = new ArrayList<>();
//
//        // Add the elements to the ArrayList
//        data.add("playing");
//
//        // Create the first "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll1 = new HashMap<>();
//        roll1.put("playerId", 1);
//        roll1.put("camelId", 1);
//        roll1.put("number", 2);
//        HashMap<String, Object> rollMap1 = new HashMap<>();
//        rollMap1.put("roll", roll1);
//        data.add(rollMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the second "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll2 = new HashMap<>();
//        roll2.put("playerId", 2);
//        roll2.put("camelId", 0);
//        roll2.put("number", 1);
//        HashMap<String, Object> rollMap2 = new HashMap<>();
//        rollMap2.put("roll", roll2);
//        data.add(rollMap2);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the third "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll3 = new HashMap<>();
//        roll3.put("playerId", 3);
//        roll3.put("camelId", -2);
//        
//        roll3.put("number", -3);
//        HashMap<String, Object> rollMap3 = new HashMap<>();
//        rollMap3.put("roll", roll3);
//        data.add(rollMap3);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the fourth "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll4 = new HashMap<>();
//        roll4.put("playerId", 1);
//        roll4.put("camelId", 2);
//        
//        roll4.put("number", 2);
//        HashMap<String, Object> rollMap4 = new HashMap<>();
//        rollMap4.put("roll", roll4);
//        data.add(rollMap4);
//
//        data.add("visualizing");
//
//        // Add the final "visualizingStage" string
//        data.add("visualizingStage");
//
//        // Print the ArrayList to verify
////        System.out.println(data);
//        return data;
//    }
//
//    private ArrayList<Object> etappe2() {
//        ArrayList<Object> data = new ArrayList<>();
//
//        // Add "playing" Strings
//        data.add("playing");
//
//        // Add the elements to the ArrayList
//        data.add("playing");
//
//        // Create the first "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll1 = new HashMap<>();
//        roll1.put("playerId", 2);  // Using "playerId"
//        roll1.put("camelId", 0);
//        roll1.put("number", 3);
//        
//        HashMap<String, Object> rollMap1 = new HashMap<>();
//        rollMap1.put("roll", roll1);
//        data.add(rollMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the second "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll2 = new HashMap<>();
//        roll2.put("playerId", 3);  // Using "playerId"
//        roll2.put("camelId", 1);
//        
//        roll2.put("number", 2);
//        HashMap<String, Object> rollMap2 = new HashMap<>();
//        rollMap2.put("roll", roll2);
//        data.add(rollMap2);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the first "bettingCards" map and add it to the ArrayList
//        HashMap<String, Object> bettingCards1 = new HashMap<>();
//        bettingCards1.put("playerId", 1);  // Using "playerId"
//        bettingCards1.put("camelId", 0);
//        HashMap<String, Object> bettingCardsMap1 = new HashMap<>();
//        bettingCardsMap1.put("bettingCards", bettingCards1);
//        data.add(bettingCardsMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the third "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll3 = new HashMap<>();
//        roll3.put("playerId", 2);  // Using "playerId"
//        roll3.put("camelId", 2);
//        roll3.put("number", 2);
//        HashMap<String, Object> rollMap3 = new HashMap<>();
//        rollMap3.put("roll", roll3);
//        data.add(rollMap3);
//
//        // Create the third "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll4 = new HashMap<>();
//        roll4.put("playerId", 3);  // Using "playerId"
//        roll4.put("camelId", -2);
//        roll4.put("number", -1);
//        HashMap<String, Object> rollMap4 = new HashMap<>();
//        rollMap4.put("roll", roll4);
//        data.add(rollMap4);
//
//        data.add("visualizing");
//
//        // Add the final "visualizingStage" string
//        data.add("visualizingStage");
//
//        // Print the ArrayList to verify
//        System.out.println(data);
//        return data;
//    }
//
//    private ArrayList<Object> etappe3() {
//        ArrayList<Object> data = new ArrayList<>();
//        // Add the elements to the ArrayList
//        data.add("playing");
//
//        // Create the first "playerCard" map and add it to the ArrayList
//        HashMap<String, Object> playerCard1 = new HashMap<>();
//        playerCard1.put("spaceId", 12);
//        playerCard1.put("playerId", 1);
//        playerCard1.put("spacesMoved", 1);
//        HashMap<String, Object> playerCardMap1 = new HashMap<>();
//        playerCardMap1.put("playerCard", playerCard1);
//        data.add(playerCardMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the first "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll1 = new HashMap<>();
//        roll1.put("playerId", 2);  // Using "playerId"
//        roll1.put("camelId", -1);  // "camelId" with value -1
//        roll1.put("number", -3);    // "number" as 3
//        HashMap<String, Object> rollMap1 = new HashMap<>();
//        rollMap1.put("roll", roll1);
//        data.add(rollMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the second "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll2 = new HashMap<>();
//        roll2.put("playerId", 3);  // Using "playerId"
//        roll2.put("camelId", 2);   // "camelId" with value 2
//        roll2.put("number", 2);    // "number" as 2
//        HashMap<String, Object> rollMap2 = new HashMap<>();
//        rollMap2.put("roll", roll2);
//        data.add(rollMap2);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the third "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll3 = new HashMap<>();
//        roll3.put("playerId", 1);  // Using "playerId"
//        roll3.put("camelId", 1);   // "camelId" with value 1
//        roll3.put("number", 2);    // "number" as 2
//        HashMap<String, Object> rollMap3 = new HashMap<>();
//        rollMap3.put("roll", roll3);
//        data.add(rollMap3);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the fourth "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll4 = new HashMap<>();
//        roll4.put("playerId", 2);  // Using "playerId"
//        roll4.put("camelId", 0);   // "camelId" with value 0
//        roll4.put("number", 2);    // "number" as 2
//        HashMap<String, Object> rollMap4 = new HashMap<>();
//        rollMap4.put("roll", roll4);
//        data.add(rollMap4);
//
//        data.add("visualizing");
//
//        // Add the final "visualizingStage" string
//        data.add("visualizingStage");
//
//        // Print the ArrayList to verify
//        System.out.println(data);
//    return data;
//    }
//
//    private ArrayList<Object> etappe4() {
//        ArrayList<Object> data = new ArrayList<>();
//        // Add the elements to the ArrayList
//        data.add("playing");
//
//        // Create the first "finalWinnerBet" map and add it to the ArrayList
//        HashMap<String, Object> finalWinnerBet1 = new HashMap<>();
//        finalWinnerBet1.put("playerId", 3);
//        finalWinnerBet1.put("camelId", 0);
//        HashMap<String, Object> finalWinnerBetMap1 = new HashMap<>();
//        finalWinnerBetMap1.put("finalWinnerBet", finalWinnerBet1);
//        data.add(finalWinnerBetMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the first "finalLoserBet" map and add it to the ArrayList
//        HashMap<String, Object> finalLoserBet1 = new HashMap<>();
//        finalLoserBet1.put("playerId", 1);
//        finalLoserBet1.put("camelId", 2);
//        HashMap<String, Object> finalLoserBetMap1 = new HashMap<>();
//        finalLoserBetMap1.put("finalLoserBet", finalLoserBet1);
//        data.add(finalLoserBetMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the first "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll1 = new HashMap<>();
//        roll1.put("playerId", 2);
//        roll1.put("camelId", -2);
//        roll1.put("number", -3);
//        HashMap<String, Object> rollMap1 = new HashMap<>();
//        rollMap1.put("roll", roll1);
//        data.add(rollMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the second "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll2 = new HashMap<>();
//        roll2.put("playerId", 3);
//        roll2.put("camelId", 0);
//        roll2.put("number", 3);
//        HashMap<String, Object> rollMap2 = new HashMap<>();
//        rollMap2.put("roll", roll2);
//        data.add(rollMap2);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the third "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll3 = new HashMap<>();
//        roll3.put("playerId", 1);
//        roll3.put("camelId", 1);
//        roll3.put("number", 2);
//        HashMap<String, Object> rollMap3 = new HashMap<>();
//        rollMap3.put("roll", roll3);
//        data.add(rollMap3);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the fourth "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll4 = new HashMap<>();
//        roll4.put("playerId", 2);
//        roll4.put("camelId", 2);
//        roll4.put("number", 2);
//        HashMap<String, Object> rollMap4 = new HashMap<>();
//        rollMap4.put("roll", roll4);
//        data.add(rollMap4);
//
//        data.add("visualizing");
//
//        // Add the final "visualizingStage" string
//        data.add("visualizingStage");
//
//        // Print the ArrayList to verify
//        return data;
//    }
//
//    private ArrayList<Object> etappe5() {
//        ArrayList<Object> data = new ArrayList<>();
//
//        // Add the elements to the ArrayList
//        data.add("playing");
//
//        // Create the first "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll1 = new HashMap<>();
//        roll1.put("playerId", 3);
//        roll1.put("camelId", 1);
//        roll1.put("number", 3);
//        HashMap<String, Object> rollMap1 = new HashMap<>();
//        rollMap1.put("roll", roll1);
//        data.add(rollMap1);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the second "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll2 = new HashMap<>();
//        roll2.put("playerId", 1);
//        roll2.put("camelId", -1);
//        roll2.put("number", -3);
//        HashMap<String, Object> rollMap2 = new HashMap<>();
//        rollMap2.put("roll", roll2);
//        data.add(rollMap2);
//
//        data.add("visualizing");
//
//        data.add("playing");
//
//        // Create the third "roll" map and add it to the ArrayList
//        HashMap<String, Object> roll3 = new HashMap<>();
//        roll3.put("playerId", 2);
//        roll3.put("camelId", 0);
//        roll3.put("number", 2);
//        HashMap<String, Object> rollMap3 = new HashMap<>();
//        rollMap3.put("roll", roll3);
//        data.add(rollMap3);
//
//        data.add("visualizing");
//
//        // Add the final "finished" string
//        data.add("finished");
//
//        // Print the ArrayList to verify
////        System.out.println(data);
//        return data;
//    }
//}