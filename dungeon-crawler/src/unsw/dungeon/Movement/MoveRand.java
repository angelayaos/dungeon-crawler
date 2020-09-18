package unsw.dungeon.Movement; 

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import java.util.Random;

public class MoveRand implements MoveStrategy {

    private NPC npc;

    public MoveRand(NPC c) {
        this.npc = c;
    }

    @Override
    public void execute() {
        int random = new Random().nextInt(4); 
        switch(random) {
            case 0:
                this.npc.moveRight();
                break;
            case 1:
                this.npc.moveLeft();
                break;
            case 2:
                this.npc.moveUp();
                break;
            case 3: 
                this.npc.moveDown();
                break;
        }

    }
}



