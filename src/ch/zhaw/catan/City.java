package ch.zhaw.catan;

import java.awt.*;

/**
 * Class that represents a city of a player.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */

public class City extends Settlement {

    /**
     * Constructor to build the city.
     * @param typeOfStructure - Which type of structure
     * @param faction - Which faction the player belongs to
     * @param coordinate - The coordinate where the city is build
     */
    public City(Config.Structure typeOfStructure, Config.Faction faction, Point coordinate){
        super(typeOfStructure, faction, coordinate);
    }

}
