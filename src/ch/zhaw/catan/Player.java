package ch.zhaw.catan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ch.zhaw.catan.Config.Faction;

/**
 * This class represents the players in this game.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */


public class Player {

    private HashMap <Config.Resource, Integer> resources;
    private Config.Faction faction;
    private HashMap<Config.Structure, Integer> structures;
    private int victoryPoints;


    /**
     * Constructor to build a player.
     * @param desiredFaction - To which faction the player wish to belong
     */
    public Player(Config.Faction desiredFaction){
        resources = new HashMap<>();
        structures = new HashMap<>();
        faction = desiredFaction;
        victoryPoints = 0;
    }

    /**
     * This method adds the victory points to the concerning player.
     */
    public void addVictoryPoint() { victoryPoints++; }

    // getters and setters beyond this point only

    public HashMap<Config.Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Config.Resource, Integer> resources) {
        this.resources = resources;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Config.Faction faction) {
        this.faction = faction;
    }

    public HashMap<Config.Structure, Integer> getStructures() {
        return structures;
    }

    public void setStructures(HashMap<Config.Structure, Integer> structures) {
        this.structures = structures;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

}