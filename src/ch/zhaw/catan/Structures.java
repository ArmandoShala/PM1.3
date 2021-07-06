package ch.zhaw.catan;

import java.awt.*;

/**
 * Super class of the classes settlement and road
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */
public class Structures {

    private Config.Structure typeOfStructure;
    private Config.Faction faction;
    private Point coordinate;

    /**
     * Constructs Structures with the type, faction and coordinate
     * @param typeOfStructure type of the Structure
     * @param faction type of the Faction
     * @param coordinate the x,y Points
     */
    public Structures(Config.Structure typeOfStructure, Config.Faction faction, Point coordinate){
        this.typeOfStructure = typeOfStructure;
        this.faction = faction;
        this.coordinate = coordinate;
    }

    /**
     * Constructs Structures with the type and faction
     * @param typeOfStructure type of the Structure
     * @param faction type of the Faction
     */
    public Structures(Config.Structure typeOfStructure, Config.Faction faction){
        this.typeOfStructure = typeOfStructure;
        this.faction = faction;
    }


    // getters and setters

    public Config.Structure getTypeOfStructure() {
        return typeOfStructure;
    }

    public void setTypeOfStructure(Config.Structure typeOfStructure) {
        this.typeOfStructure = typeOfStructure;
    }

    public Config.Faction getFaction() {
        return faction;
    }

    public void setFaction(Config.Faction faction) {
        this.faction = faction;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }
}
