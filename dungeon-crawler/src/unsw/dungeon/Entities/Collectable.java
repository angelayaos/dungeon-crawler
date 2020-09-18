package unsw.dungeon.Entities;    

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


/**
 * An Collectable entity in the dungeon.
 * @author Tuan Ho
 *
 */
public class Collectable extends Entity {
    
    private BooleanProperty display;
    
    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Collectable(int x, int y) {
        super(x,y);
        this.display = new SimpleBooleanProperty(true);
    }

    public BooleanProperty display() {
        return this.display;
    }

    public boolean getDisplay() {
        return this.display().get();
    }

    public void hide() {
        this.display().setValue(false);
    }

    public void toDisplay() {
        this.display().setValue(true);
    }

    @Override 
    public void setX(int x) {
        super.setX(x);
    }

    @Override 
    public void setY(int x) {
        super.setY(x);
    }
}
