package ch.zhaw.catan;

import java.awt.*;

/**
 * Class that represents a road of a player.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */
public class Road extends Structures {
    private Point startCoordinate;
    private Point endCoordinate;

    public Road(Config.Structure typeOfStructure, Config.Faction faction, Point startCoordinate, Point endCoordinate){
        super(typeOfStructure, faction);
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
    }



    public Point getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(Point startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public Point getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(Point endCoordinate) {
        this.endCoordinate = endCoordinate;
    }
}
