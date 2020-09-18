package unsw.dungeon.Entities;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import java.lang.*;
import java.time.Instant;

public class Gnome extends NPC {
    private enum ToProduceType {
        BLUE,
        GREEN
    }
    
    private int timeToProduce;
    private ToProduceType type = ToProduceType.GREEN;
    private long lastTimeProduced;
    private Dungeon dungeon;
    private Timer timer;

    public Gnome(Dungeon d, int x, int y) {
        super(d, x, y);
        this.lastTimeProduced = 30; // 30 secs by default
        this.lastTimeProduced = Instant.now().getEpochSecond();

        this.timer = new Timer();
        int delay = 1000;
        int interval = 10000;
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                String typeStr = "";
                if (type == ToProduceType.GREEN) {
                    typeStr = "green";
                    type = ToProduceType.BLUE;
                } else if (type == ToProduceType.BLUE) {
                    typeStr = "blue";
                    type = ToProduceType.GREEN;
                }
                Potion p = new Potion(getX(), getY(), typeStr);
                d.addEntity((Entity) p);
                d.notifyPotionProduced(p, d);
            }
        }, delay, interval);
    }

    // @Override
    // public Boolean tryWalk(int nextX, int nextY, String direction) {
    //     if (!super.tryWalk(nextX,nextY,direction)) return false;
    //     // System.out.println("Dungeon of Gnome ="+(dungeon==null));
    //     // System.out.println("Dungeon of Gnome has "+dungeon.getEntities().size());
    //     // if (dungeon.isPotion(nextX, nextY)) return false;
    //     return true;
    // }
    public void cancelTimer() {
        this.timer.cancel();
    }
}
