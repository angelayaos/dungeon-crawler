package unsw.dungeon;

import java.time.Instant;
import java.util.*;
import java.io.*;
import java.util.stream.Stream;
import java.util.stream.*;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;
import javafx.scene.layout.GridPane;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



import unsw.dungeon.Goal.Subgoal.BasicGoal;
import unsw.dungeon.Map.MapBuilder;
import unsw.dungeon.Map.MapController;

public class PlayerProgressObserver implements DungeonObserver {
    public enum GameState {
        PLAYING,
        LOSING,
        WINNING,
        SETTING_GOALS,
        MENU,
        SHOPPING
    }

    private IntegerProperty currLevel;
    private Goal currGoal;
    private LinkedHashMap<BasicGoal, BooleanProperty> goalState = new LinkedHashMap<>();
    private GameState gameState;
    private LongProperty invincibleTimeEnd;
    private DungeonController controller;
    private DungeonLoader loader;


    public PlayerProgressObserver(Goal goal) {
        this.currGoal = goal;
        this.currLevel = new SimpleIntegerProperty(1);
        this.gameState = GameState.PLAYING;
        for(BasicGoal gg: BasicGoal.values()) {
            this.goalState.put(gg,new SimpleBooleanProperty(false));
        }
        this.invincibleTimeEnd = new SimpleLongProperty(Instant.now().getEpochSecond()-1);
    }


    public DungeonController getController() {
        return this.controller;
    }

    public void setController(DungeonController controller) {
        this.controller = controller;
    }

    public IntegerProperty currLevel() {
        return this.currLevel;
    }

    public void setDungeonLoader(DungeonLoader l) {
        this.loader = l;
    }
    
    public LongProperty invincibleTimeEnd() {
        return this.invincibleTimeEnd;
    }

    public int getCurrLevel() {
        return this.currLevel().get();
    }

    public void setCurrLevel(int currLevel) {
        this.currLevel().set(currLevel);
    }

    public Goal getCurrGoal() {
        return this.currGoal;
    }

    public void setCurrGoal(Goal currGoal) {
        this.currGoal = currGoal;
    }

    public LinkedHashMap<BasicGoal, BooleanProperty> getGoalState() {
        return this.goalState;
    }

    public Boolean isPlayerInvincible() {
        return this.getInvincibleTimeEnd() > Instant.now().getEpochSecond();
    }


    public long getInvincibleTimeEnd() {
        return this.invincibleTimeEnd().get();
    }

    public BooleanProperty getStateBasicGoal(BasicGoal g) {
        return this.goalState.get(g);
    }

    public void addMoreInvincibleTime(long moreTime) {
        this.setInvincibleTimeEnd(this.getInvincibleTimeEnd()+moreTime);
    }


    public void setInvincibleTimeEnd(long invincibleTimeEnd) {
        this.invincibleTimeEnd().set(invincibleTimeEnd);
    }


    public void setGoalState(LinkedHashMap<BasicGoal, BooleanProperty> goalState) {
        this.goalState = goalState;
    }


    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }



    @Override
    public void updatePlayerMoved(Player p, Dungeon d) {
        this.updatePotionCollected(p,d);
        this.updateGoalStates(d);
    }
    
    @Override
    public void updateEnemyMoved(Enemy e, Dungeon d) {
        Player p = d.getPlayer();
        if(e.onPlayer() && (this.isPlayerInvincible() || p.havingSword())) {
            // System.out.println(">> Enemy dead");
            e.die();
            if (!p.havingSword())
                d.removeEntity((Entity) e);
        }

        // If the player to be killed, reset Dungeon & level
        if (e.onPlayer() && !this.isPlayerInvincible() && !p.havingSword()) {
            this.reset4losing(d);
            printDebug("Player was killed at ("+e.getX()+","+e.getY()+")");
            // return;
        }


        // If the player is not invincible, set enemies to move closer.
        List<NPC> enemies = d.genericGetEntities("Enemy", "Hound").stream()
                             .map(o -> (NPC) o)
                             .collect(Collectors.toList());
        for (NPC ee: enemies) {
            if (this.isPlayerInvincible()) ee.setMoveAway2Player();
            else ee.setMoveCloser2Player();
        }
    }


    @Override
    public void updateBoulderMoved(Dungeon d) {
        this.activatedAllSwitches(d);
    }

    @Override
    public void updateDrillUsed() {
        return;
    }

    @Override
    public void updatePotionProduced(Potion p, Dungeon d) {
        // printDebug("Produced potion at ("+p.getX()+","+p.getY()+")");
        // this.loader.onLoad(p);
        if (this.controller.getDungeon() == d)
            this.controller.addEntityUI(p);
    }

    /**
     * Update invincibleTimeEnd. 
     * Note that the invincibleTime is accumulatively added.
     */
    private void updatePotionCollected(Player player, Dungeon d) {
        // System.out.println("called updatePotionCollected(), d.getPotions()="+d.getPotions().size());
        for (Potion pp: d.getPotions()) {
            if (player.getX() != pp.getX() || player.getY() != pp.getY()) continue;
            
            Potion p = pp;
            if (p == null) return;
            if (p.getDisplay() == false) return;
            p.hide();
            Long potionTime = Instant.now().getEpochSecond();
            Long adding = Long.valueOf(p.getEffectTime());
            if (this.isPlayerInvincible())
                potionTime = this.getInvincibleTimeEnd() + adding;
            else potionTime += adding;
            setInvincibleTimeEnd(potionTime);
    
            // Set all enemies to move away
            // List<NPC> enemies = d.genericGetEntities("Enemy", "Hound").stream()
            List<NPC> enemies = d.getEnemies().stream()
                                .map(o -> (NPC) o)
                                .collect(Collectors.toList());
            
            for (NPC e: enemies) {
                e.setMoveAway2Player();
            }
            d.removeEntity(p);
            player.setInvincible(true);
            return;
            
        }
        
        
    }

    public Boolean finishedGoal() {
        return this.currGoal.checkAchieved(this.goalState);
    }

    private void updateGoalStates(Dungeon d) {
        this.collectedAllTreasure(d);
        this.activatedAllSwitches(d);
        this.killedAllEnemies(d);
        this.updateFoundExit(d.getPlayer(), d);
        this.updateUnlockedAllDoors(d);

        // If the current goal is achieved,
        // build the new dungeon and set it on 
        // UI controller
        if (this.currGoal.checkAchieved(goalState)) {
            printDebug("currGoal is achieved");
            this.reset4newLevel();
            this.setCurrLevel(this.getCurrLevel()+1);
            try {
                d.cancelGnomeTimerThreads();
                MapController newController = new MapController("portal.json", this.getCurrLevel(), this.controller, d);
                newController.run();
            } catch(FileNotFoundException ex) {}
        }
    }

    private void reset4newLevel() {
        this.invincibleTimeEnd().set(0);
        for(BasicGoal gg: BasicGoal.values()) {
            this.goalState.get(gg).set(false);
        }
        // this.currLevel().set(this.getCurrLevel()+1);
    }

    private void reset4losing(Dungeon d) {
        this.reset4newLevel();
        this.setCurrLevel(1);
        try {
            d.cancelGnomeTimerThreads();
            MapController newController = new MapController("portal.json", this.getCurrLevel(), this.controller, d);
            newController.run();
        } catch(FileNotFoundException ex) {}
    }

    /**
     * Checking if the treasure goal is finished
     */
    private void collectedAllTreasure(Dungeon d) {
        List<Treasure> treasures = d.genericGetAllEntitiesByType("Treasure").stream()
                                    .map(o -> (Treasure) o)
                                    .collect(Collectors.toList());
        if (treasures.isEmpty())
            this.goalState.get(BasicGoal.TREASURE).set(true);
        else {
            for (Treasure t : treasures) {
                if (t.getDisplay() == true) {
                    this.goalState.get(BasicGoal.TREASURE).set(false);
                    return;
                }
            }
            this.goalState.get(BasicGoal.TREASURE).set(true);
        }
            
    }

    private void activatedAllSwitches(Dungeon d) {
        List<Switch> switches = d.getSwitches();
        for (Switch s: switches) {
            if (!s.getState()) {
                this.goalState.get(BasicGoal.BOULDERS).set(false);
                return;
            }
        }
        this.goalState.get(BasicGoal.BOULDERS).set(true);
        
    }

    private void killedAllEnemies(Dungeon d) {
        List<Enemy> enemies = d.genericGetEntities("Enemy", "Hound").stream()
                                .map(o -> (Enemy) o)
                                .filter(o -> !o.isDead())
                                .collect(Collectors.toList());
        
        if (!enemies.isEmpty()) {
            this.goalState.get(BasicGoal.ENEMIES).set(false);
            return;
        } else this.goalState.get(BasicGoal.ENEMIES).set(true);
        
    }

    private void updateFoundExit(Player p, Dungeon d) {
        List<Entity> exits = d.genericGetAllEntitiesByType("Exit");
        if (exits.isEmpty()) {
            this.goalState.get(BasicGoal.EXIT).set(true);
            return;
        }
        Exit e = (Exit) exits.get(0);
        if (e.getX() == p.getX() && e.getY() == p.getY())
            e.activate();
        if (e.isActive())
            this.goalState.get(BasicGoal.EXIT).set(true);
        else
            this.goalState.get(BasicGoal.EXIT).set(false);
        
    }

    private void updateUnlockedAllDoors(Dungeon d) {
        List<Entity> doors = d.genericGetAllEntitiesByType("Door");
        if (doors.isEmpty()) {
            this.goalState.get(BasicGoal.DOORS).set(true);
            return;
        }
        // If any door is still closed this goal is not finished
        for (Entity e: doors) {
            Door dd = (Door) e;
            if(dd.getState()==false) {
                this.goalState.get(BasicGoal.DOORS).set(false);
                return;
            }
        }

        // Otherwise this goal is finished
        this.goalState.get(BasicGoal.DOORS).set(true);
    }

    private void printDebug(String s) {
        System.out.println(s);
    }


}
