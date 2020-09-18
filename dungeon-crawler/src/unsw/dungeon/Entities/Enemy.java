package unsw.dungeon.Entities; 

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;
import unsw.dungeon.Movement.MoveCloserOptimized;




public class Enemy extends NPC {

    public Enemy(Dungeon d, int x, int y) {
        super(d, x, y);
        this.setSpeed(0.5);
        MoveStrategy initStrategy = new MoveCloser(this, d);
        this.moveStrategy = initStrategy;
    }

    public boolean onPlayer() {
        Player player = this.dungeon.getPlayer();
        return ((player.getX() == this.getX()) && (player.getY() == this.getY()));
    }

}
