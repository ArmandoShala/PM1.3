package ch.zhaw.catan;

import java.awt.*;


/**
 * This class represents a tile (hexagon) on the playground.
 * @author Gruppe2 IT20ta_WIN
 * @version 2020-12-04
 */
public class Field {

    private int fieldValue;
    private Config.Land typeOfLand;
    private Point coordinate;


    /**
     * Constructor to build the field.
     * @param typeOfLand - The type this field is holding
     * @param coordinate - Where on the playground the field is
     */
    public Field(Config.Land typeOfLand, Point coordinate){
        this.typeOfLand = typeOfLand;
        this.coordinate = coordinate;
    }


    // getters and setters beyond this point only

    public int getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(int fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Config.Land getTypeOfLand() {
        return typeOfLand;
    }

    public void setTypeOfLand(Config.Land typeOfLand) {
        this.typeOfLand = typeOfLand;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }
}
