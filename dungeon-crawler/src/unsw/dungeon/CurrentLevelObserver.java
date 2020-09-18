package unsw.dungeon;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


import java.util.*;
import java.time.*;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.stream.Collectors;

public class CurrentLevelObserver implements DungeonObserver {
    private IntegerProperty enemyHits;
    private IntegerProperty drillHits;
    private DoubleProperty money;
    /**
     * Entities that the Player collected over different levels.
     * This list will never remove anything unless the game is reset.
     */
    private List<Collectable> collected;
    
    

    public CurrentLevelObserver() {
        this.enemyHits = new SimpleIntegerProperty(0);
        this.money = new SimpleDoubleProperty(0);
        this.drillHits = new SimpleIntegerProperty(0);
        this.collected = new ArrayList<>();        
    }

    public IntegerProperty hits() {
        return this.enemyHits;
    }

    public DoubleProperty money() {
        return this.money;
    }

    public IntegerProperty drills() {
        return this.drillHits;
    }

    public int getEnemyHits() {
        return this.hits().get();
    }

    public void setEnemyHits(int enemyHits) {
        this.hits().set(enemyHits);
    }

    public int getDrillHits() {
        return this.drills().get();
    }

    public void setDrills(int drills) {
        this.drills().set(drills);
    }

    public double getMoney() {
        return this.money().get();
    }

    public void setMoney(double money) {
        this.money().set(money);
    }

    public List<Collectable> getCollected() {
        return this.collected;
    }

    public void setCollected(List<Collectable> collected) {
        this.collected = collected;
    }
    
    @Override
    public void updatePlayerMoved(Player p, Dungeon d) {
        this.updateCollectableDisplayAndHolding(p, d);
        this.updateDoorOpen(p, d);
        this.updateTeleporting(p, d);
        this.updateNoSword(p);
        this.updateEnemyKilled(d);
    }


    @Override
    public void updateEnemyMoved(Enemy e, Dungeon d) {
        if(e.onPlayer() && this.getEnemyHits() > 0) {
            System.out.println(">> Enemy dead by swords");
            e.die();
            d.removeEntity((Entity) e);
            
            this.setEnemyHits(this.getEnemyHits()-1);;
            this.updateNoSword(d.getPlayer());
        }
    }

    
    @Override
    public void updateBoulderMoved(Dungeon d) {
        this.updateSwitchesState(d);
    }

    
    @Override
    public void updateDrillUsed() {
        if (this.getDrillHits()>0)
            this.setDrills(this.getDrillHits()-1);
    }

    @Override
    public void updatePotionProduced(Potion p, Dungeon d) {
        return;
    }
    
    private void updateCollectableDisplayAndHolding(Player p, Dungeon d) {
        int currX = p.getX();
        int currY = p.getY();
        List<Collectable> collecteds = d.getCollectables().stream()
                                        .filter( o -> o.getX() == p.getX() && o.getY() == p.getY())
                                        .collect(Collectors.toList());
        if (collecteds.isEmpty()) return;
        Collectable c = collecteds.get(0);

        if (c instanceof Sword) {
            // If the player is holding a sword or having enemyHits > 0,
            // just walk over it.
            if (p.havingSword() && this.getEnemyHits() > 0) return;
            
            Sword sword = (Sword) c;
            this.setEnemyHits(this.getEnemyHits()+sword.getDurability());;
            p.setHasSword();
        }

        if (c instanceof Key) {
            Key k = (Key) c;
            // If the player is holding a key,
            // just walk over it.
            if (p.hasKey() == true) return;
            p.setKey(k);
        }

        if (c instanceof Drill) {
            Drill dr = (Drill) c;
            p.setDrill(dr);
            this.setDrills(dr.getDurability());
        }

        this.collected.add(c);
        c.hide();
        // The other observer will remove the other collectable 
        // entities from the dungeon
        if (c instanceof Treasure) {
            Treasure t = (Treasure) c;
            this.setMoney(this.getMoney()+t.getValue());
            // System.out.println("now having "+this.getMoney());
            d.removeEntity((Entity) c);
        }
            
    }

    private void updateNoSword(Player p) {
        if (this.getEnemyHits() <= 0) 
            p.setNoSword();
    }

    private void updateEnemyKilled(Dungeon d) {
        List<Enemy> enemies = d.getEnemies();
        if (enemies.isEmpty()) return;
        for (Enemy e: enemies) {
            if (!e.onPlayer()) continue;
            if (this.getEnemyHits() > 0) {
                System.out.println(">> Enemy dead by swords");
                e.die();
                d.removeEntity((Entity) e);
                
                this.setEnemyHits(this.getEnemyHits()-1);;
                this.updateNoSword(d.getPlayer());
            }

        }
    }


    private void updateDoorOpen(Player p, Dungeon d) {
        int x = p.getX();
        int y = p.getY();
        try {
            Door door = d.findDoorByPosition(x, y);
            if (door == null) return;
            String doorId = door.getId();
            Key key = p.getKey();
            if (key == null) return;
            if (!key.getId().equalsIgnoreCase(doorId)) return;
            door.setOpen();
            p.setNoKey();
            
        } catch(Exception ex) {}
    }

    private void updateTeleporting(Player p, Dungeon d) {
        int currX = p.getX();
        int currY = p.getY();
        List<Portal> touchingPortals = d.getPortals().stream()
                                        // .filter(o -> o instanceof Portal)
                                        .filter(o -> o.getX() == currX && o.getY() == currY)
                                        .collect(Collectors.toList());

        if (!touchingPortals.isEmpty()) {
            Portal adjPortal = (Portal) touchingPortals.get(0);
            Portal destPortal = adjPortal.getDestPortal();
            int newX = destPortal.getX();
            int newY = destPortal.getY();
            p.x().set(newX);
            p.y().set(newY);
        }

    }

    private void updateSwitchesState(Dungeon d) {
        // Using lmabda expr
        // Set<Map.Entry<Integer, Integer>> boulderCOs = d.getBoulders().stream()
        //                                         .map(new AbstractMap.SimpleEntry<Integer, Integer>(o -> o.getX(), o -> o.getY()))
        //                                         .collect(Collectors.toSet());
        
        // Imperative way
        // Get the set COs of Boulders
        Set<Map.Entry<Integer, Integer>> boulderCOs = new LinkedHashSet<>();
        List<Boulder> boulders = d.getBoulders();
        for (Boulder bb: boulders) {
            int x = bb.getX();
            int y = bb.getY();
            AbstractMap.SimpleEntry<Integer, Integer> boulderCOTupple 
                = new AbstractMap.SimpleEntry<Integer, Integer>(x,y);
            boulderCOs.add(boulderCOTupple);
        }

        // Set all switches to unactivated
        List<Switch> switches = d.getSwitches();
        for(Switch s: switches) {
            s.setState(false);
        }
        
        // Then set all switches having same COs with boulders to be activated
        if (switches.isEmpty()) return;
        for (Map.Entry<Integer, Integer> i: boulderCOs) {
            try {
                Switch notActivatedSwitch = (Switch) switches.stream()
                                                    .filter(o -> o.getX() == i.getKey() 
                                                            && o.getY() == i.getValue())
                                                    .collect(Collectors.toList())
                                                    .get(0);
                notActivatedSwitch.setState(true);
                // System.out.println("Switch at ("+notActivatedSwitch.getX()+","+notActivatedSwitch.getY()+") is activated");
            } catch (Exception ex) {}
        }
    }

    private void updateEnemyHits() {
        
    }

    public void resetForLosing() {
        this.reset4newLevel();
        this.setMoney(0);
    }

    public void reset4newLevel() {
        this.setEnemyHits(0);
        this.setDrills(0);
        this.collected = new ArrayList<>();       
    }

}
