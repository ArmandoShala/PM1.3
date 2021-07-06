package ch.zhaw.catan;

import java.awt.*;

/**
 * Class that represents a settlement of a player.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */

public class Settlement extends Structures {
    private Point position;

    /**
     * The constructor of class Settlement
     * @param typeOfStructure - Which type of structure: road, settlement, city
     * @param faction - Which faction the player belongs to
     * @param coordinate - On which coordinate the settlement is placed
     */
    public Settlement(Config.Structure typeOfStructure, Config.Faction faction, Point coordinate){
        super(typeOfStructure, faction, coordinate);
    }

}
