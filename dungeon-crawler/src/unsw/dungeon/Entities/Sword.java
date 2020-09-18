package unsw.dungeon.Entities;  

// import javafx.beans.property.IntegerProperty;
// import javafx.beans.property.SimpleIntegerProperty;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


/**
 * An Sword entity in the dungeon.
 * @author Tuan Ho
 *
 */
public class Sword extends Collectable {
    
    private int durability;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Sword(int x, int y) {
        super(x,y);
        this.durability = 5;
    }

    public Sword(int x, int y, int v) {
        super(x,y);
        this.durability = v;
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }


}
