package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.Config.Structure;
import ch.zhaw.catan.Config.Land;

import java.awt.Point;
import java.util.*;


/**
 * This class performs all actions related to modifying the game state.
 *
 * <p>For example, calling the method {@link SiedlerGame#throwDice(int)}
 * will do the payout of the resources to the players according to
 * the payout rules of the game which take into account factors like
 * the amount of resources requeste of a certain type, the number of players
 * requesting it, the amount of resources available in the bank and
 * the settlement types.</p>
 *
 * @author Gruppe2 IT20ta_WIN
 */
public class SiedlerGame {

    private int minVictoryPointsToWin;
    private int numberOfPlayersInGame;
    private SiedlerBoard siedlerBoard;
    private Map<Structure, ArrayList<Structures>> structureList;
    private List<Player> players;
    private int currentPlayerIndex;
    private Map<Point, Field> fields;
    private Point pointPrevInitialSettlementSet;
    private HashMap<Config.Resource, Integer> resourcesOfBank;


    /**
     * Constructs a SiedlerGame game state object.
     *
     * @param winPoints the number of points required to win the game
     * @param players   the number of players
     * @throws IllegalArgumentException if winPoints is lower than
     *                                  three or players is not between two and four
     */
    public SiedlerGame(int winPoints, int players) throws IllegalArgumentException {
        if (1 < players && players < 5) {
            numberOfPlayersInGame = players;
            minVictoryPointsToWin = winPoints;
            initGame();
        } else {
            throw new IllegalArgumentException("To play this game 2 - 4 people needed");
        }
    }

    /**
     * This method is used to initialize the board.
     */
    public void initGame() {
        siedlerBoard = new SiedlerBoard();
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        fields = new HashMap<>();
        structureList = initStructures();
        resourcesOfBank = initResourcesBank();
        buildPlayground();
        assignPlayers();
    }

    private boolean assignPlayers() {
        //TODO Redo return value to void and remove code for testing
        players.add(new Player(Faction.BLUE));
        players.add(new Player(Faction.GREEN));
        if (numberOfPlayersInGame >= 3) {
            players.add(new Player(Faction.RED));
        }
        if (numberOfPlayersInGame == 4) {
            players.add(new Player(Faction.YELLOW));
        }
        //testing from this point
        return players.size() == numberOfPlayersInGame;

    }

    /**
     * Test purpose only
     *
     * @return True if assigning was successful
     */
    public boolean testAssignPlayers() {
        return assignPlayers();
    }

    private Map<Structure, ArrayList<Structures>> initStructures() {
        Map<Structure, ArrayList<Structures>> structures = new HashMap<>();

        for (Config.Structure structure : Config.Structure.values()) {
            structures.put(structure, new ArrayList<>());
        }
        return structures;
    }

    /**
     * Test purpose only
     *
     * @return
     */
    public Map<Structure, ArrayList<Structures>> testInitStructures() {
        return initStructures();
    }


    private HashMap<Resource, Integer> initResourcesBank() {
        HashMap<Resource, Integer> resourcesBank = new HashMap<>();
        for (Map.Entry<Resource, Integer> entry : Config.INITIAL_RESOURCE_CARDS_BANK.entrySet()) {
            resourcesBank.put(entry.getKey(), entry.getValue());
        }
        return resourcesBank;
    }

    /**
     * Test purpose only
     *
     * @return
     */
    public HashMap<Resource, Integer> testInitResourceBank() {
        return initResourcesBank();
    }


    /**
     * This method builds the board as a whole.
     */
    private boolean buildPlayground() {
        //TODO Redo return value to void and remove code for testing

        // Place default fields
        for (Map.Entry<Point, Land> entry : Config.getStandardLandPlacement().entrySet()) {
            fields.put(entry.getKey(), new Field(entry.getValue(), entry.getKey()));
        }

        // Set dice number needed per field
        for (Map.Entry<Point, Integer> entry : Config.getStandardDiceNumberPlacement().entrySet()) {
            fields.get(entry.getKey()).setFieldValue(entry.getValue());
        }

        // Put it all together
        for (Map.Entry<Point, Field> field : fields.entrySet()) {
            siedlerBoard.addField(field.getKey(), field.getValue().getTypeOfLand());
        }

        return false;
    }

    /**
     * Test purpose only
     *
     * @return false
     */
    public boolean testBuildPlayground() {
        return buildPlayground();
    }


    /**
     * Switches to the next player in the defined sequence of players.
     */
    public void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1 == players.size()) ? 0 : currentPlayerIndex + 1;
    }

    public int testGetCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Switches to the previous player in the defined sequence of players.
     */
    public void switchToPreviousPlayer() {
        if (currentPlayerIndex == 0) {
            currentPlayerIndex = players.size() - 1;
        } else {
            currentPlayerIndex = currentPlayerIndex - 1;
        }
    }

    /**
     * Gets the current player
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Returns the {@link Faction}s of the active players.
     *
     * <p>The order of the player's factions in the list must
     * correspond to the oder in which they play.
     * Hence, the player that sets the first settlement must be
     * at position 0 in the list etc.
     *
     * <strong>Important note:</strong> The list must contain the
     * factions of active players only.</p>
     *
     * @return the list with player's factions
     */
    public List<Faction> getPlayerFactions() {
        List<Faction> activeFactions = new ArrayList<>();
        for (Player player : players) {
            activeFactions.add(player.getFaction());
        }
        return activeFactions;
    }


    /**
     * Returns the game board.
     *
     * @return the game board
     */
    public SiedlerBoard getBoard() {
        return siedlerBoard;
    }

    /**
     * Returns the {@link Faction} of the current player.
     *
     * @return the faction of the current player
     */
    public Faction getCurrentPlayerFaction() {
        return getCurrentPlayer().getFaction();
    }

    /**
     * Gets the current amount of resources of the specified type
     * in the current players' stock (hand).
     *
     * @param resource the resource type
     * @return the amount of resources of this type
     */
    public int getCurrentPlayerResourceStock(Resource resource) {
        return getCurrentPlayer().getResources().get(resource);
    }

    /**
     * Places a settlement in the founder's phase (phase II) of the game.
     *
     * <p>The placement does not cost any resources. If payout is
     * set to true, one resource per adjacent field is taken from the
     * bank and added to the players' stock of resources.</p>
     *
     * @param position the position of the settlement
     * @param payout   true, if the player shall get the resources of the surrounding fields
     * @return true, if the placement was successful
     */
    public boolean placeInitialSettlement(Point position, boolean payout) {
        if (!isAllowedBuildSettlement(position)) {
            // If it's now allowed to build there -> abort
            return false;
        }
        // It is allowed to build on that position -> continue

        // Get value of structure by KEY (SETTLEMENT), then add a new SETTLEMENT
        structureList.get(Structure.SETTLEMENT).add(new Settlement(Structure.SETTLEMENT, getCurrentPlayerFaction(), position));
        // Add structure to playground
        siedlerBoard.setCorner(position, getCurrentPlayerFaction().toString());
        // Remember last position in order to be able to set road correctly
        pointPrevInitialSettlementSet = position;

        if (payout) {
            // if this is with payout -> give resources
            for (Land land : siedlerBoard.getFields(position)) {
                payoutByPlayer(land);
            }
        }

        // Give some point to current player
        getCurrentPlayer().addVictoryPoint();
        // Return value of true implies that everything worked
        return true;
    }

    /**
     * Places a road in the founder's phase (phase II) of the game.
     * The placement does not cost any resources.
     *
     * @param roadStart position of the start of the road
     * @param roadEnd   position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
        if (!isAllowedBuildRoad(roadStart, roadEnd) && (pointPrevInitialSettlementSet.equals(roadStart) || pointPrevInitialSettlementSet.equals(roadEnd))) {
            return false;
        }

        structureList.get(Config.Structure.ROAD).add(new Road(Structure.ROAD, players.get(currentPlayerIndex).getFaction(), roadStart, roadEnd));
        siedlerBoard.setEdge(roadStart, roadEnd, getCurrentPlayerFaction().toString());
        return true;
    }

    /**
     * This method checks if the desired coordinates for a road is within the boundary.
     *
     * @param roadStart - Coordinate of one end for the road
     * @param roadEnd   - Coordinate of the other end for the road
     * @return - True if the coordinates are within the boundary
     */
    private boolean isAllowedBuildRoad(Point roadStart, Point roadEnd) {
        return siedlerBoard.hasEdge(roadStart, roadEnd) && !isOnCoordinateRoad(roadStart, roadEnd) && !(isLandBorderedByWater(roadStart) || isLandBorderedByWater(roadEnd));
    }

    public boolean testIsAllowedBuildRoad(Point roadStart, Point roadEnd) {
        return isAllowedBuildRoad(roadStart, roadEnd);
    }


    /**
     * This method checks if the desired coordinates for a settlement is within the boundary.
     *
     * @param coordinate - The desired coordinate of a player
     * @return - True if the coordinate is within the boundary
     */
    private boolean isAllowedBuildSettlement(Point coordinate) {
        return siedlerBoard.hasCorner(coordinate) && !isOnCoordinateSettlement(coordinate) && isOnCoordinateEmpty(coordinate) && !isLandBorderedByWater(coordinate) && remainingStructuresEnough(Structure.SETTLEMENT);
    }

    public boolean testIsAllowedBuildSettlement(Point coordinate) {
        return isAllowedBuildSettlement(coordinate);
    }

    /**
     * This method checks if there is already a road on the desired coordinates.
     *
     * @param roadStart - Coordinate of one end for the road
     * @param roadEnd   - Coordinate of the other end for the road
     * @return - True if there is no road on the desired coordinates
     */
    private boolean isOnCoordinateRoad(Point roadStart, Point roadEnd) {
        for (Structures structures : structureList.get(Structure.ROAD)) {
            Road road = (Road) structures;
            if (road.getStartCoordinate().equals(roadStart) && road.getEndCoordinate().equals(roadEnd)) {
                return true;
            }
        }
        return false;
    }

    public boolean testIsOnCoordinateRoad(Point roadStart, Point roadEnd) {
        return isOnCoordinateRoad(roadStart, roadEnd);
    }


    /**
     * This method checks if there is already a settlement on the desired coordinate.
     *
     * @param point - The desired coordinate of a player
     * @return - True if there is no settlement on the desired coordinate
     */
    private boolean isOnCoordinateSettlement(Point point) {
        for (Structures structures : structureList.get(Structure.SETTLEMENT)) {
            Settlement settlement = (Settlement) structures;
            if (settlement.getCoordinate().equals(point)) {
                return true;
            }
        }
        return false;
    }

    public boolean testIsOnCoordinateSettlement(Point point) {
        return isOnCoordinateSettlement(point);
    }


    /**
     * This method checks if a neighbour borders the desired coordinate
     *
     * @param position - The desired coordinate of a player
     * @return - True if there are neighbours around the desired coordinate
     */
    private boolean isOnCoordinateEmpty(Point position) {
        return siedlerBoard.getNeighboursOfCorner(position).isEmpty();
    }

    public boolean testIsOnCoordinateEmpty(Point position) {
        return isOnCoordinateEmpty(position);
    }


    /**
     * This method checks if a player has enough of the desired structure to build.
     *
     * @param inStructure - The desired structure which the player wants to build
     * @return True if a player has enough of the desired structure
     */
    private boolean remainingStructuresEnough(Structure inStructure) {
        boolean isBuildPossible = true;
        int structuresStock = 0;
        for (Structures structure : structureList.get(inStructure)) {
            if (structure.getFaction().equals(getCurrentPlayerFaction())) {
                structuresStock++;
            }
        }
        if (!(inStructure.getStockPerPlayer() > structuresStock)) {
            isBuildPossible = false;
        }
        return isBuildPossible;
    }


    public boolean testRemainingStructuresEnough(Structure inStructure) {
        return remainingStructuresEnough(inStructure);
    }


    /**
     * This method checks if the land is bordered by water.
     *
     * @param point - The desired coordinate of a player
     * @return True if the land is bordered by water.
     */
    private boolean isLandBorderedByWater(Point point) {
        boolean isLandBorderedByWater = true;
        List<Land> fieldLands = siedlerBoard.getFields(point);
        for (Land land : fieldLands) {
            if (!land.equals(Land.WATER)) {
                isLandBorderedByWater = false;
            }
        }
        return isLandBorderedByWater;
    }

    public boolean testIsLandBorderedByWater(Point point) {
        return isLandBorderedByWater(point);
    }


    /**
     * This method checks if the bank has enough of the desired resource to trade with the player.
     *
     * @param land - The type of resources depends on what kind of land it is
     * @return - True if the bank has enough resources of the desired type of the player
     */
    private boolean isBankPayoutPossible(Land land) {
        if (resourcesOfBank.get(land.getResource()) > 0) {
            resourcesOfBank.merge(land.getResource(), -1, Integer::sum);
            return true;
        }
        return false;
    }

    public boolean testIsBankPayoutPossible(Land land) {
        return isBankPayoutPossible(land);
    }


    /**
     * This method adds one resource of the concerning land to the current player.
     *
     * @param land - The type of resources depends on what kind of land it is
     * @return - True if payout is successful
     */
    private boolean payoutByPlayer(Land land) {
        if (land.equals(Land.DESERT) && land.equals(Land.WATER) && !isBankPayoutPossible(land)) {
            return false;
        }
        getCurrentPlayer().getResources().merge(land.getResource(), 1, Integer::sum);
        return true;
    }


    public boolean testPayoutByPlayer(Land land) {
        return payoutByPlayer(land);
    }


    /**
     * This method adds resources of the concerning land to the faction
     *
     * @param land           - The type of resources depends on what kind of land it is
     * @param faction        - The faction who will receive the resource
     * @param amountOfPayout - The amount of the resource which was removed from the bank and added to the faction
     * @return True if payout is successful
     */
    private boolean payoutByFaction(Land land, Faction faction, int amountOfPayout) {
        if (land.equals(Land.DESERT) && land.equals(Land.WATER) && !isBankPayoutPossible(land)) {
            return false;
        }
        for (Player player : players) {
            if (player.getFaction().equals(faction)) {
                player.getResources().merge(land.getResource(), 1, Integer::sum);
            }
        }
        return true;
    }

    public boolean testPayoutByFaction(Land land, Faction faction, int amountOfPayout) {
        return payoutByFaction(land, faction, amountOfPayout);
    }

    /**
     * This method adds one resource of the concerning land to the faction
     *
     * @param land    - The type of resources depends on what kind of land it is
     * @param faction - The faction who will receive the resource
     * @return True if payout is successful
     */
    private boolean payoutByFaction(Land land, Faction faction) {
        return payoutByFaction(land, faction, 1);
    }


    public boolean testPayoutByFaction(Land land, Faction faction) {
        return payoutByFaction(land, faction);
    }


    /**
     * This method takes care of the payout of the resources to the players
     * according to the payout rules of the game. If a player does not get resources,
     * the list for this players' faction is empty.
     *
     * <p>The payout rules of the game take into account factors like
     * the amount of resources of a certain type, the number of players
     * requesting resources of this type, the amount of resources available
     * in the bank and the settlement types.</p>
     *
     * @param dicethrow the result of the dice throw
     * @return the resources that have been paid to the players
     */
    public Map<Faction, List<Resource>> throwDice(int dicethrow) {
        activateRobber(dicethrow);
        HashMap<Faction, List<Resource>> resourceByFactionInGame = getResourcesByFaction();

        for (Map.Entry<Point, Field> field : fields.entrySet()) {
            if (field.getValue().getFieldValue() == dicethrow) {
                List<String> cornersOfFields = siedlerBoard.getCornersOfField(field.getValue().getCoordinate());
                Config.Land typeOfCurrentLand = field.getValue().getTypeOfLand();
                for (String corner : cornersOfFields) {
                    //s = settlement
                    //C = CITY
                    //R, G, B, Y = color of faction
                    switch (corner) {
                        case "sr":
                            if (payoutByFaction(typeOfCurrentLand, Faction.RED, 1)) {
                                resourceByFactionInGame.get(Faction.RED).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "sg":
                            if (payoutByFaction(typeOfCurrentLand, Faction.GREEN, 1)) {
                                resourceByFactionInGame.get(Faction.GREEN).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "sy":
                            if (payoutByFaction(typeOfCurrentLand, Faction.YELLOW, 1)) {
                                resourceByFactionInGame.get(Faction.YELLOW).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "sb":
                            if (payoutByFaction(typeOfCurrentLand, Faction.BLUE, 1)) {
                                resourceByFactionInGame.get(Faction.BLUE).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "CR":
                            if (payoutByFaction(typeOfCurrentLand, Faction.RED, 2)) {
                                resourceByFactionInGame.get(Faction.RED).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "CY":
                            if (payoutByFaction(typeOfCurrentLand, Faction.YELLOW, 2)) {
                                resourceByFactionInGame.get(Faction.YELLOW).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "CG":
                            if (payoutByFaction(typeOfCurrentLand, Faction.GREEN, 2)) {
                                resourceByFactionInGame.get(Faction.GREEN).add(typeOfCurrentLand.getResource());
                            }
                            break;
                        case "CB":
                            if (payoutByFaction(typeOfCurrentLand, Faction.BLUE, 2)) {
                                resourceByFactionInGame.get(Faction.BLUE).add(typeOfCurrentLand.getResource());
                            }
                            break;
                    }
                }
            }
        }
        return resourceByFactionInGame;
    }


    /**
     * This method activates the robber.
     *
     * @param dicethrow - The number played at dice
     */
    public boolean activateRobber(int dicethrow) {
        //TODO Redo return value to void and remove code for testing
        //TODO: call robber and lose resources (or something like this)

        for (Player player : players) {

        }
        return false;
    }

    /**
     * Test purpose only
     *
     * @param dicethrow - Number played at dice
     * @return false
     */
    public boolean testActivateRobber(int dicethrow) {
        return activateRobber(dicethrow);
    }


    /**
     * Lists of all resources owned by a faction.
     *
     * @return - Returns all the resources a faction owns
     */
    private HashMap<Faction, List<Resource>> getResourcesByFaction() {
        HashMap<Faction, List<Resource>> resourceByFactionInGame = new HashMap<>();
        for (Faction faction : getPlayerFactions()) {
            resourceByFactionInGame.put(faction, new ArrayList<>());
        }
        return resourceByFactionInGame;
    }

    public HashMap<Faction, List<Resource>> testGetResourcesByFaction() {
        return getResourcesByFaction();
    }

    /**
     * Builds a settlement at the specified position on the board.
     *
     * <p>The settlement can be built if:
     * <ul>
     * <li> the player has the resource cards required</li>
     * <li> a settlement to place on the board</li>
     * <li> the specified position meets the build rules for settlements</li>
     * </ul>
     *
     * @param position the position of the settlement
     * @return true, if the placement was successful
     */
    public boolean buildSettlement(Point position) {
        if (!remainingStructuresEnough(Config.Structure.SETTLEMENT) && !isAllowedBuildSettlement(position)) {
            return false;
        }

        structureList.get(Config.Structure.SETTLEMENT).add(new Settlement(Structure.SETTLEMENT, getCurrentPlayerFaction(), position));
        siedlerBoard.setCorner(position, getCurrentPlayerFaction().toString());
        return true;
    }

    /**
     * Builds a city at the specified position on the board.
     *
     * <p>The city can be built if:
     * <ul>
     * <li> the player has the resource cards required</li>
     * <li> a city to place on the board</li>
     * <li> the specified position meets the build rules for cities</li>
     * </ul>
     *
     * @param position the position of the city
     * @return true, if the placement was successful
     */
    public boolean buildCity(Point position) {
        // TODO: OPTIONAL task - Implement


        return false;
    }

    /**
     * Builds a road at the specified position on the board.
     *
     * <p>The road can be built if:
     * <ul>
     * <li> the player has the resource cards required</li>
     * <li> a road to place on the board</li>
     * <li> the specified position meets the build rules for roads</li>
     * </ul>
     *
     * @param roadStart the position of the start of the road
     * @param roadEnd   the position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean buildRoad(Point roadStart, Point roadEnd) {
        if (!isAllowedBuildRoad(roadStart, roadEnd) && !remainingStructuresEnough(Config.Structure.ROAD) && !remainingStructuresEnough(Config.Structure.ROAD)) {
            return false;
        }
        structureList.get(Config.Structure.ROAD).add(new Road(Structure.ROAD, getCurrentPlayerFaction(), roadStart, roadEnd));
        siedlerBoard.setEdge(roadStart, roadEnd, getCurrentPlayerFaction().toString());
        return true;
    }


    /**
     * Trades in {@value #FOUR_TO_ONE_TRADE_OFFER} resources of the
     * offered type for {@value #FOUR_TO_ONE_TRADE_WANT} resource of the wanted type.
     *
     * @param offer offered type
     * @param want  wanted type
     * @return true, if player and bank had enough resources and the trade was successful
     */
    public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
        return players.get(currentPlayerIndex).getResources().get(offer) >= 4;
    }

    /**
     * Returns the winner of the game, if any.
     *
     * @return the winner of the game or null, if there is no winner (yet)
     */
    public Faction getWinner() {
        return players.stream().filter(player -> player.getVictoryPoints() >= minVictoryPointsToWin).findAny().orElse(null).getFaction();
    }


    /**
     * Places the thief on the specified field and steals a random resource card (if
     * the player has such cards) from a random player with a settlement at that
     * field (if there is a settlement) and adds it to the resource cards of the
     * current player.
     *
     * @param field the field on which to place the thief
     * @return false, if the specified field is not a field or the thief cannot be
     * placed there (e.g., on water)
     */
    public boolean placeThiefAndStealCard(Point field) {
        //TODO: Implement (or longest road functionality)
        return false;
    }

// getters and setters beyond this point only

    public int getMinVictoryPointsToWin() {
        return minVictoryPointsToWin;
    }

    public void setMinVictoryPointsToWin(int minVictoryPointsToWin) {
        this.minVictoryPointsToWin = minVictoryPointsToWin;
    }

    public int getNumberOfPlayersInGame() {
        return numberOfPlayersInGame;
    }

    public void setNumberOfPlayersInGame(int numberOfPlayersInGame) {
        this.numberOfPlayersInGame = numberOfPlayersInGame;
    }


    public void setSiedlerBoard(SiedlerBoard siedlerBoard) {
        this.siedlerBoard = siedlerBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Map<Point, Field> getFields() {
        return fields;
    }

    public void setFields(Map<Point, Field> fields) {
        this.fields = fields;
    }
}
