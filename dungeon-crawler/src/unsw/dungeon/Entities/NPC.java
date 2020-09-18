package unsw.dungeon.Entities; 

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.application.Platform;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;
import java.util.stream.*;
import java.util.stream.Stream;
import java.lang.*;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import java.lang.Math; 

public class NPC extends Entity{
    /**
     * Set protected to access from the subclasses.
     */
    protected Dungeon dungeon;
    protected volatile BooleanProperty isAlive;
    protected double speed;
    protected Player player;
    protected Timer timer;
    protected MoveStrategy moveStrategy;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public NPC(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.isAlive = new SimpleBooleanProperty(true);
        this.moveStrategy = new MoveRand(this);
        this.speed = 1;
    }    


    public Player getPlayer() {
        return this.player;
    }

    // public void setPlayer(Player player) {
    //     this.player = player;
    // }


    public BooleanProperty display() {
        return this.isAlive;
    }

    /**
     * This function is the opposite of isDead()
     */
    // public boolean getDisplay() {
    //     return this.display().get();
    // }
    
    public boolean isDead() {
        return !this.display().get();
    }


    public void hide() {
        this.display().setValue(false);
    }

    public void setToDead() {
        this.display().setValue(false);
    }

    public void toDisplay() {
        this.display().setValue(true);
    }

    public void moveUp() {
        int nextX = this.getX();
        int nextY = this.getY() - 1;

        if (this.tryWalk(nextX, nextY, "up")) {
            this.setX(nextX);
            this.setY(nextY);
        }
        
    }
    
    public void moveDown() {
        int nextX = this.getX();
        int nextY = this.getY() + 1;
        if (this.tryWalk(nextX, nextY, "down")) {
            this.setX(nextX);
            this.setY(nextY);
        }

    }
    
    public void moveLeft() {
        int nextX = this.getX() - 1;
        int nextY = this.getY();
        if (this.tryWalk(nextX, nextY, "left")) {
            this.setX(nextX);
            this.setY(nextY);
        }

    }
    
    public void moveRight() {
        int nextX = this.getX() + 1;
        int nextY = this.getY();
        if (this.tryWalk(nextX, nextY, "right") == true) {
            this.setX(nextX);
            this.setY(nextY);
        }

    }

    public void setMoveCloser2Player() {
        this.setMoveStrategy(new MoveCloser(this, this.dungeon));
    }
    
    public void setMoveAway2Player() {
        // System.out.println("called setMoveAway2Player()");
        this.setMoveStrategy(new MoveAway(this, this.dungeon));
    }

    /**
     * Check if the NPC can moves the intended coordinates
     * @param nextX
     * @param nextY
     * @param direction
     * @return true if there's no Wall AND closed door AND no (portal OR BOULDER)
     */
    public Boolean tryWalk(int nextX, int nextY, String direction) {

        // Check if next position is within boundaries
        if (dungeon.isOutside(nextX, nextY)) {
            return false;
        }
        
        // NPCs can move through broken walls
        if(dungeon.isWall(nextX, nextY)){
            if (dungeon.isBroken(nextX, nextY)) {
                return true;
            }
            
            return false;
        }

        // NPCs can only move through open doors
        if(dungeon.isDoor(nextX, nextY)) {
            if (dungeon.isOpen(nextX, nextY)) {
                return true;
            } else {
                return false;
            }
        }

        // NPCs cannot travel through portals
        if (!dungeon.isPortal(nextX, nextY) && !dungeon.isBoulder(nextX, nextY)) {
            return true;
        } else {
            return false;
        }
    }


    public void moveTo(int x, int y) {
        this.setY(y);
        this.setX(x);
    }

    public MoveStrategy getMoveStrategy() {
        return this.moveStrategy;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }


    public BooleanProperty isAlive() {
        return this.isAlive;
    }
    
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void startMoving() {
        timer = new Timer();
        int delay = 5000;
        int timeInterval = (int) Math.ceil(1000/speed);
        
        NPC n = this;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                move();   
                Platform.runLater(new Runnable() {
                 @Override public void run() {
                    if (n instanceof Enemy || n instanceof Hound)
                        dungeon.notifyEnemyMoved((Enemy) n, dungeon);
                    }
                });             
            }

        }, delay, timeInterval);
    }

    public void move() {
        this.moveStrategy.execute();
    }

    public void addPlayer(Player p) {
        this.player = p;
    }


    public void die() {
        this.hide();
        Thread.currentThread().interrupt();
        if (this.timer != null)
            this.timer.cancel();
    }
}