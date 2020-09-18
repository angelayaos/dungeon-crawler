package unsw.dungeon.Entities;    

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


/**
 * An Door entity in the dungeon.
 * @author Tuan Ho
 *
 */
public class Door extends Entity {
    
    private String id;
    private BooleanProperty state;

    
    public Door(int x, int y, String id) {
        super(x,y);
        this.id = id;
        this.state = new SimpleBooleanProperty(false);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BooleanProperty state() {
        return this.state;
    }

    public boolean getState() {
        return this.state().get();
    }

    public void setState(Boolean state) {
        this.state().set(state);
    }
    
    public void setOpen() {
        this.setState(true);
    }
}
