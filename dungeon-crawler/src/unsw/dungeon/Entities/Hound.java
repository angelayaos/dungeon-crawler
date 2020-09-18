package unsw.dungeon.Entities;  

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


public class Hound extends Enemy {
    
    public Hound(Dungeon d, int x, int y) {
        super(d, x, y);
        this.setSpeed(1);
        MoveStrategy initStrategy = new MoveCloserOptimized(this, d);
        this.moveStrategy = initStrategy;
    }
}
