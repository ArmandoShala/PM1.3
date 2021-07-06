package ch.zhaw.catan;

import ch.zhaw.ENUMS.*;
import ch.zhaw.ENUMS.Menu;
import ch.zhaw.IO.Input;
import ch.zhaw.IO.Output;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class Catan {

    private SiedlerGame siedlerGame;
    private SiedlerBoardTextView boardView;
    private Input input;
    private Output output;
    private int numberOfPlayers = 2;
    private final int _MAX_VICTORY_POINTS = 7;

    public Catan() {
        input = new Input();
        output = new Output();
    }


    public static void main(String[] args) {
        new Catan().startCatan();
    }


    private void startCatan() {
        boolean isRunning = true;
        while (isRunning) {
            Menu userInput = input.getEnumValue(Menu.class);
            switch (userInput) {
                case START_GAME:
                    gamephaseOne();
                    gamephaseTwo();
                    gamephaseThree();
                    break;
                case QUIT_GAME:
                    isRunning = false;
                    break;
                default:
                    throw new IllegalStateException(userInput + " not implemented yet or unknown");
            }
        }
        output.disposeTextIO();
        input.disposeTextIO();
    }

    private void gamephaseOne() {
        PlayerCount playerCount = input.getEnumValue(PlayerCount.class);
        switch (playerCount) {
            case SET_PLAYERS_THREE:
                numberOfPlayers = 3;
                break;
            case SET_PLAYERS_FOUR:
                numberOfPlayers = 4;
                break;
            default:
                //Do nothing because players are set initially to 2
                break;
        }
        siedlerGame = new SiedlerGame(_MAX_VICTORY_POINTS, numberOfPlayers);
        output.printText(siedlerGame.getPlayers().size() + " players in this session");
    }


    private void gamephaseTwo() {
        System.out.println(siedlerGame.getBoard().getSiedlerBoardTextView());
        for (int i = 0; i < siedlerGame.getPlayers().size(); i++) {
            //First placement of structures without payout
            showBoard();
            placeFirstSettlement(false);
            placeFirstRoad();
            siedlerGame.switchToNextPlayer();
        }
        siedlerGame.switchToPreviousPlayer();
        for (int i = 0; i < siedlerGame.getPlayers().size(); i++) {
            //Second placement of structures with payout
            showBoard();
            placeFirstSettlement(true);
            placeFirstRoad();
            siedlerGame.switchToNextPlayer();
        }
    }


    private void gamephaseThree() {
        boolean hasPlayerEndedMove = false;
        boolean hasCurrentPlayerWon = false;

        while (!hasCurrentPlayerWon) {
            for (int i = 0; i < siedlerGame.getPlayers().size(); i++) {
                forceThrowDice();

                while (!hasPlayerEndedMove) {
                    switch (input.getEnumValue(Commands.class)) {
                        case TRADE:
                            tradeWithBank();
                            break;
                        case BUILD_ROAD:
                            placeRoad();
                            break;
                        case BUILD_SETTLEMENT:
                            placeSettlement();
                            break;
                        case BUILD_CITY:
                            placeCity(false);
                            break;
                        case SHOW_RESOURCES:
                            showCurrentPlayerResource();
                            break;
                        case SHOW_BOARD:
                            showBoard();
                            break;
                        case SHOW_BUILD_PRICES:
                            showBuildPrices();
                            break;
                        case END_MOVE:
                            if (siedlerGame.getWinner() != null) {
                                output.printText(siedlerGame.getCurrentPlayerFaction() + "has won with " + siedlerGame.getCurrentPlayer().getVictoryPoints() + " points");
                                hasCurrentPlayerWon = true;
                            }
                            siedlerGame.switchToNextPlayer();
                            hasPlayerEndedMove = true;
                            break;
                    }
                }
            }
        }
    }

    private void forceThrowDice() {
        boolean hasPlayerThrownDice = false;
        while (!hasPlayerThrownDice) {
            output.printText("Throw the dice to start your turn");
            Commands command = input.getEnumValue(Commands.class);
            if (command == Commands.THROW_DICE) {
                int diceNumber = siedlerGame.getBoard().rollDice();
                if (diceNumber == 7) {
                    output.printText("Robber activated!");
                    siedlerGame.activateRobber(diceNumber);
                }
                output.printText("You rolled a: " + diceNumber);
                Map<Config.Faction, List<Config.Resource>> aquiredRessource = siedlerGame.throwDice(diceNumber);
                // TODO: print how much resources each player got

                hasPlayerThrownDice = true;
            } else {
                output.printText("You need to throw the dice first!");
            }
        }
    }

    private void placeFirstSettlement(boolean withPayout) {
        boolean hasPlayerSetSuccessfullySettlement = false;

        while (!hasPlayerSetSuccessfullySettlement) {
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your free settlement at coordinate");
            int xCoordinate = input.readNextInt("X: ");
            int yCoordinate = input.readNextInt("Y: ");

            if (siedlerGame.placeInitialSettlement(new Point(xCoordinate, yCoordinate), withPayout)) {
                hasPlayerSetSuccessfullySettlement = true;
            } else {
                output.printError();
                hasPlayerSetSuccessfullySettlement = false;
            }
        }
        showBoard();
    }

    private void placeFirstRoad() {
        boolean hasPlayerSetSuccessfullyRoad = false;
        while (!hasPlayerSetSuccessfullyRoad) {
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your free road at start coordinate");
            int xRoadStart = input.readNextInt("X: ");
            int yRoadStart = input.readNextInt("Y: ");
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your free road at end coordinate");
            int xRoadEnd = input.readNextInt("X: ");
            int yRoadEnd = input.readNextInt("Y: ");

            if (siedlerGame.placeInitialRoad(new Point(xRoadStart, yRoadStart), new Point(xRoadEnd, yRoadEnd))) {
                hasPlayerSetSuccessfullyRoad = true;
            } else {
                output.printError();
                hasPlayerSetSuccessfullyRoad = false;
            }
        }
        showBoard();
    }


    private void placeCity(boolean withPayout) {
        boolean hasPlayerSetSuccessfullyCity = false;
        while (!hasPlayerSetSuccessfullyCity) {
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your city at coordinate");
            int xCoordinate = input.readNextInt("X: ");
            int yCoordinate = input.readNextInt("Y: ");

            if (siedlerGame.buildCity(new Point(xCoordinate, yCoordinate))) {
                //TODO: dont forget payout!
                hasPlayerSetSuccessfullyCity = true;
            } else {
                output.printError();
                hasPlayerSetSuccessfullyCity = false;
            }
        }
        showBoard();
    }

    private void placeSettlement() {
        boolean hasPlayerSetSuccessfullySettlement = false;

        while (!hasPlayerSetSuccessfullySettlement) {
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your settlement at coordinate");
            int xCoordinate = input.readNextInt("X: ");
            int yCoordinate = input.readNextInt("Y: ");

            if (siedlerGame.buildSettlement(new Point(xCoordinate, yCoordinate))) {
                hasPlayerSetSuccessfullySettlement = true;
            } else {
                output.printError();
                hasPlayerSetSuccessfullySettlement = false;
            }
        }
        showBoard();
    }


    private void placeRoad() {
        boolean hasPlayerSetSuccessfullyRoad = false;
        while (!hasPlayerSetSuccessfullyRoad) {
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your road at start coordinate");
            int xRoadStart = input.readNextInt("X: ");
            int yRoadStart = input.readNextInt("Y: ");
            output.printText(siedlerGame.getCurrentPlayerFaction() + " build your road at end coordinate");
            int xRoadEnd = input.readNextInt("X: ");
            int yRoadEnd = input.readNextInt("Y: ");

            if (siedlerGame.buildRoad(new Point(xRoadStart, yRoadStart), new Point(xRoadEnd, yRoadStart))) {
                hasPlayerSetSuccessfullyRoad = true;
            } else {
                output.printError();
                hasPlayerSetSuccessfullyRoad = false;
            }
        }
        showBoard();
    }

    private void showBuildPrices() {
        output.printText("Build prices: ");
        for (Config.Structure structure : Config.Structure.values()) {
            for (Map.Entry<Config.Resource, Long> entry : structure.getCostsAsMap().entrySet()) {
                output.printText(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    private void showCurrentPlayerResource() {
        output.printText("You have following resources: ");
        for (Config.Resource resource : Config.Resource.values()) {
            output.printText(resource.name() + ": " + siedlerGame.getCurrentPlayerResourceStock(resource));
        }
    }

    private void showBoard() {
        output.printText(siedlerGame.getBoard().getSiedlerBoardTextView().toString());
    }

    private void tradeWithBank() {
        boolean hasPlayerTradedSuccessfully = false;

        while (!hasPlayerTradedSuccessfully) {
            output.printText("What would you like to offer");
            Config.Resource offer = giveSelectedResource(input.getEnumValue(TradableResources.class));

            output.printText("What would you like to receive");
            Config.Resource want = giveSelectedResource(input.getEnumValue(TradableResources.class));

            if (siedlerGame.tradeWithBankFourToOne(offer, want)) {
                hasPlayerTradedSuccessfully = true;
            } else {
                output.printError();
                output.printText("Would you like to abort trading?");
                Prompt prompt = input.getEnumValue(Prompt.class);
                if (prompt == Prompt.YES) {
                    hasPlayerTradedSuccessfully = true;
                    output.printText("Trading aborted");
                }
            }
        }
    }

    private Config.Resource giveSelectedResource(TradableResources tradableResources) {
        Config.Resource resource = null;
        switch (tradableResources) {
            case CLAY:
                resource = Config.Resource.CLAY;
                break;
            case WOOD:
                resource = Config.Resource.WOOD;
                break;
            case WOOL:
                resource = Config.Resource.WOOL;
                break;
            case STONE:
                resource = Config.Resource.STONE;
                break;
            case GRAIN:
                resource = Config.Resource.GRAIN;
                break;
            default:
                resource = null;
                output.printError();
                break;
        }
        return resource;
    }


}