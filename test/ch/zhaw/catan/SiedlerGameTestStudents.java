package ch.zhaw.catan;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SiedlerGameTestStudents {

    private SiedlerGame siedlerGame;
    private int winPoints;
    private int minVictoryPointsToWin;
    private int numberOfPlayersInGame;
    private SiedlerBoard siedlerBoard;
    private Map<Config.Structure, ArrayList<Structures>> structureList;
    private List<Player> players;
    private int currentPlayerIndex;
    private Map<Point, Field> fields;
    private Point pointPrevInitialSettlementSet;
    private HashMap<Config.Resource, Integer> resourcesOfBank;

    @Before
    public void setup() {
        siedlerGame = new SiedlerGame(winPoints, 2);
        siedlerGame.initGame();
    }

    @Test
    void initGame() {

        siedlerBoard = new SiedlerBoard();
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        fields = new HashMap<>();
       /*
        structureList = initStructures();
        resourcesOfBank = initResourcesBank();
        siedlerGame.buildPlayground();
        siedlerGame.assignPlayers();

        */
    }

    @Test
    void switchToNextPlayer1() {
        siedlerGame = new SiedlerGame(winPoints, 2);
        int testValue = 1;
        siedlerGame.switchToNextPlayer();
        int currentPlayerIndex = siedlerGame.testGetCurrentPlayerIndex();
        assertTrue(testValue == currentPlayerIndex);
    }

    @Test
    void switchToNextPlayer2() {
        siedlerGame = new SiedlerGame(winPoints, 4);
        int testValue = 3;
        siedlerGame.switchToNextPlayer();
        siedlerGame.switchToNextPlayer();
        siedlerGame.switchToNextPlayer();
        int currentPlayerIndex = siedlerGame.testGetCurrentPlayerIndex();
        assertTrue(testValue == currentPlayerIndex);
    }

    @Test
    void switchToPreviousPlayer1() {
        siedlerGame = new SiedlerGame(winPoints, 2);
        int testValue = 1;
        siedlerGame.switchToPreviousPlayer();
        int currentPlayerIndex = siedlerGame.testGetCurrentPlayerIndex();
        assertTrue(testValue == currentPlayerIndex);
    }

    @Test
    void switchToPreviousPlayer2() {
        siedlerGame = new SiedlerGame(winPoints, 4);
        int testValue = 2;
        siedlerGame.switchToPreviousPlayer();
        siedlerGame.switchToPreviousPlayer();
        int currentPlayerIndex = siedlerGame.testGetCurrentPlayerIndex();
        assertTrue(testValue == currentPlayerIndex);
    }

    @Test
    void placeInitialSettlement() {
    }

    @Test
    void placeInitialRoad() {
    }

    @Test
    void throwDice() {
        siedlerGame = new SiedlerGame(winPoints, 2);
        Map<Config.Faction, List<Config.Resource>> dc = siedlerGame.throwDice(3);
    }

    @Test
    void buildSettlement() {
    }

    @Test
    void buildCity() {
    }

    @Test
    void buildRoad() {
    }

    @Test
    void tradeWithBankFourToOne() {
    }

    @Test
    void getWinner() {
    }

    @Test
    void placeThiefAndStealCard() {
    }
}