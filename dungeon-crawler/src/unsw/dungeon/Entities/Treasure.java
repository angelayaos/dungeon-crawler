package unsw.dungeon.Entities;    

// import javafx.beans.property.IntegerProperty;
// import javafx.beans.property.SimpleIntegerProperty;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


/**
 * An Treasure entity in the dungeon.
 * @author Tuan Ho
 *
 */
public class Treasure extends Collectable {
    
    private double value;


    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Treasure(int x, int y) {
        super(x,y);
        this.value = 10;
    }

    
    public Treasure(int x, int y, double v) {
        super(x,y);
        this.value = v;
    }


    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
