package unsw.dungeon.Entities;  


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


import java.util.*;

/**
 * An Potion entity in the dungeon.
 * @author Tuan Ho
 *
 */
public class Potion extends Collectable {
    /**
     * Type of the potion.
     * Either Blue or Green only.
     */
    private String type;
    private int effectTime;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    // public Potion(int x, int y, String type) throws Exception {
    public Potion(int x, int y, String type){
        super(x,y);
        if (type.trim().equalsIgnoreCase("blue")) {
            this.type = type.trim().toLowerCase();
            this.effectTime = 5;
        } else {
            this.type = "green";
            this.effectTime = 10;
        } 
        // else if (type.trim().equalsIgnoreCase("green")) {
        //     this.type = type;
        //     this.effectTime = 10;
        // } else throw Exception("Invalid potion parsed in.");
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEffectTime() {
        return this.effectTime;
    }

    public void setEffectTime(int effectTime) {
        this.effectTime = effectTime;
    }

}
