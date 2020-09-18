package unsw.dungeon.Entities; 

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;
import java.util.stream.*;
import java.util.stream.Stream;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import java.util.concurrent.TimeUnit;



public class Player extends Entity {

    private Dungeon dungeon;
    private BooleanProperty hasSword;
    private BooleanProperty usingDrill;
    private boolean invincible;

    /**
     * Since the player can holds one key at a time,
     * this attribute is to check if the player can 
     * walk over the adj door.
     */
    private Key key;
    private Drill drill;

    /**
     * Create a player positioned in square (x,y)
     * @param dungeon
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.dungeon.setPlayer(this);
        this.hasSword = new SimpleBooleanProperty(false);
        this.usingDrill = new SimpleBooleanProperty(false);
        this.invincible = false;
        this.key = null;
        this.drill = new Drill(0, 0);
        this.drill.setDurability(0);
    }

    public Key getKey() {
        return this.key;
    }

    public Boolean hasKey() {
        return this.getKey() != null;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setNoKey() {
        this.key = null;
    }

    public Drill getDrill() {
        return this.drill;
    }

    public void setDrill(Drill d) {
        this.drill = d;
    }


    public boolean isInvincible() {
        return this.invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    /**
     * Moves the player model up if the move is valid
     */
    public void moveUp() {
        int nextX = this.getX();
        int nextY = this.getY() - 1;
        if (tryWalk(nextX, nextY, "up")) {
            this.y().set(nextY);
            this.dungeon.notifyPlayerMoved(this, this.dungeon);
        }   
    }
    
    /**
     * Moves the player model down if the move is valid
     */
    public void moveDown() {
        int nextX = this.getX();
        int nextY = this.getY() + 1;
        if (tryWalk(nextX, nextY, "down")) {
            this.y().set(nextY);
            this.dungeon.notifyPlayerMoved(this, this.dungeon);
        }  
    }
    
    /**
     * Moves the player model left if the move is valid
     */
    public void moveLeft() {
        int nextX = this.getX() - 1;
        int nextY = this.getY();
        if (tryWalk(nextX, nextY, "left")) {
            this.x().set(nextX);
            this.dungeon.notifyPlayerMoved(this, this.dungeon);
        }  
    }
    
    /**
     * Moves the player model right if the move is valid
     */
    public void moveRight() {
        int nextX = this.getX() + 1;
        int nextY = this.getY();
        if (tryWalk(nextX, nextY, "right")) {
            this.x().set(nextX);
            this.dungeon.notifyPlayerMoved(this, this.dungeon);
        } 

    }

    /**
     * Check if the Player can move over the next coordinates.
     * @param nextX Next X coordinate
     * @param nextY Next Y coordinate
     * 
     * @return false if the Player is touching a Wall OR has no key OR has no key to open the specific door OR cannot move because of the boulder's path.
     */
    private Boolean tryWalk(int nextX, int nextY, String direction) {

        // Check if position is within boundaries 
        if (dungeon.isOutside(nextX, nextY)) {
            return false;
        }

        // Hide drill model if player is no longer using it
        if (!dungeon.isWall(this.getX(), this.getY()) && this.havingDrill()) {
            drillOff();
        }

        // Check if path is obstructed by boulder
        // If true, interact with boulder
        if(dungeon.isBoulder(nextX, nextY)) {
            return dungeon.interactBoulder(nextX, nextY, this, direction);
        }

        // Check if path is obstructed by a wall
        // If player has a drill, break the wall
        if(dungeon.isWall(nextX, nextY)){
            
            if (dungeon.isBroken(nextX, nextY)) {
                return true;
            }

            if (drill.getDurability() > 0) {
                drillOn();
                dungeon.breakWall(nextX, nextY);
                drill.use();
                return true;
            }

            return false;
        }

        // If there is a door, check if it's open or if the player has the correct key
        if(dungeon.isDoor(nextX, nextY)) {
            if (dungeon.isOpen(nextX, nextY)) {
                return true;
            }
            if (this.key == null) {
                return false;
            }
            return dungeon.checkKey(nextX, nextY, key.getId());
        }

        return true;
    }

    
    public BooleanProperty hasSword() {
        return this.hasSword;
    }

    public Boolean havingSword() {
        return this.hasSword().get();
    }

    public void setNoSword() {
        this.hasSword().setValue(false);
    }

    public void setHasSword() {
        this.hasSword().setValue(true);
    }

    public BooleanProperty usingDrill() {
        return this.usingDrill;
    }

    public Boolean havingDrill() {
        return this.usingDrill().get();
    }

    public void drillOff() {
        this.usingDrill().setValue(false);
    }

    public void drillOn() {
        this.usingDrill().setValue(true);
    }
}