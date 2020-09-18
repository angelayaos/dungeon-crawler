package unsw.dungeon.Entities;    


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Wall extends Entity {

    private BooleanProperty state;

    public Wall(int x, int y) {
        super(x, y);
        this.state = new SimpleBooleanProperty(false);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) return false;
        if (this.getClass().equals(obj.getClass()) == false) return false;
        Wall w = (Wall) obj;
        return w.getX() == this.getX() && this.getY() == w.getY();
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
