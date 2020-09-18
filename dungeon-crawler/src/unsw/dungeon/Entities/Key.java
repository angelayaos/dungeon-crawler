package unsw.dungeon.Entities;    

// import javafx.beans.property.IntegerProperty;
// import javafx.beans.property.SimpleIntegerProperty;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


public class Key extends Collectable {
    
    private String id;



    public Key(int x, int y, String v) {
        super(x,y);
        this.id = v;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
