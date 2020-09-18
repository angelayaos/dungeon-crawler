package unsw.dungeon.Movement; 

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



public class MoveAway implements MoveStrategy {
    
    private Enemy enemy;
    private Dungeon dungeon;

    public MoveAway(NPC c, Dungeon d) {
        this.enemy = (Enemy) c;
        this.dungeon = d;
    }
    
    @Override
    public void execute() {
        Player player = this.enemy.getPlayer();
        double distX = player.getX() - this.enemy.getX();
        distX = distX * distX;
        
        double distY = player.getY() - this.enemy.getY();
        distY = distY * distY;
        
        double distance = Math.sqrt(distX + distY);
        distance = Math.floor(distance);
        
        // if (distance == 1.0) {
        //     if (!dungeon.isPortal(player.getX(),  player.getY())) {
        //          this.enemy.moveTo(player.getX(), player.getY());
        //     }
        // }

        if (player.getY() < this.enemy.getY()) {
            this.enemy.moveDown();
        }
        else if (player.getY() > this.enemy.getY()){
            this.enemy.moveUp();
        }

        if (player.getX() < this.enemy.getX()) {
            // System.out.println("execute called moveRight");
            this.enemy.moveRight();
        }
        else if (player.getX() > this.enemy.getX()) {
            this.enemy.moveLeft();
        }
    }
}



