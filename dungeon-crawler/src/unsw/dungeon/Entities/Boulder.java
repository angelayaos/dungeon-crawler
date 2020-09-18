package unsw.dungeon.Entities;    

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



import java.util.*;
import java.util.stream.*;

public class Boulder extends Entity {
    private Dungeon dungeon;

    public Boulder(Dungeon d, int x, int y) {
        super(x, y);
        this.dungeon = d;
    }

    public void moveUp() {
        int nextX = this.getX();
        int nextY = this.getY() - 1;
        
        if (getY() > 0 && this.tryWalk(nextX, nextY))
        y().set(nextY);
    }
    
    public void moveDown() {
        int nextX = this.getX();
        int nextY = this.getY() + 1;
        if (getY() < dungeon.getHeight() - 1 && this.tryWalk(nextX, nextY))
        y().set(nextY);
    }
    
    public void moveLeft() {
        int nextX = this.getX() - 1;
        int nextY = this.getY();
        if (getX() > 0 && this.tryWalk(nextX, nextY))
        x().set(getX() - 1);
    }
    
    public void moveRight() {
        int nextX = this.getX() + 1;
        int nextY = this.getY();
        if (getX() < dungeon.getWidth() - 1 && this.tryWalk(nextX, nextY))
            x().set(getX() + 1);
    }


    /**
     * Boulders cannot touch other entities except for Switches
     * @param nextX
     * @param nextY
     * @return
     */
    public Boolean tryWalk(int nextX, int nextY) {
        List<Entity> obstacles = this.dungeon.getEntities().stream()
                                        .filter(o -> o != null && o.getX() == nextX && o.getY() == nextY)
                                        .collect(Collectors.toList());
        if (dungeon.isWall(nextX, nextY)) {
            if(dungeon.isBroken(nextX, nextY)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (!obstacles.isEmpty()) {
            if ((obstacles.get(0) instanceof Switch)) {
                return true;
            } else {
                return false;
            } 
        }
        return true;
    }

}
