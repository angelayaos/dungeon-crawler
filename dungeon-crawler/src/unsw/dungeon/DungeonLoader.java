package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



import java.util.Timer;
import java.util.TimerTask;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Tuan Ho
 *
 */
public abstract class DungeonLoader {

    private JSONObject json;
    protected CurrentLevelObserver currLevelObserver;
    protected PlayerProgressObserver progressObserver;


    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }

        // Processing to Goal from the JSON input
        Goal gameGoal = new CompositeGoal();
        gameGoal.parseInfo(json.getJSONObject("goal-condition"));
        System.out.println("game Goal="+gameGoal.toString());
        

        // Start NPC movement
        dungeon.NPCStart();
        
        // Add the Dungeon Observers
        addDungeonObservers(dungeon, gameGoal);

        return dungeon;
    }


    public CurrentLevelObserver getCurrLevelObserver() {
        return this.currLevelObserver;
    }

    public void setCurrLevelObserver(CurrentLevelObserver currLevelObserver) {
        this.currLevelObserver = currLevelObserver;
    }

    public PlayerProgressObserver getProgressObserver() {
        return this.progressObserver;
    }

    public void setProgressObserver(PlayerProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;
        case "enemy":
            Enemy enemy = new Enemy(dungeon, x, y);
            onLoad(enemy);
            entity = enemy;
            break;
        case "hound":
            Hound hound = new Hound(dungeon, x, y);
            onLoad(hound);
            entity = hound;
            break;
        case "gnome":
            Gnome gnome = new Gnome(dungeon, x, y);
            onLoad(gnome);
            entity = gnome;
            break;

        case "boulder":
            Boulder boulder = new Boulder(dungeon, x, y);
            onLoad(boulder);
            entity = boulder;
            break;
            
        case "wall":
            Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;
        case "exit":
            Exit exit = new Exit(x, y);
            onLoad(exit);
            entity = exit;
            break;        
        case "switch":
            Switch s = new Switch(x, y);
            onLoad(s);
            entity = s;
            break;
        case "door":
            String doorId = json.getString("id");
            Door d = new Door(x, y, doorId);
            onLoad(d);
            entity = d;
            break;
        case "portal":
            String portalID = json.getString("id");
            Portal pl = new Portal(x, y, portalID);
            for (Entity e : dungeon.getEntities()) {
                if (e instanceof Portal) {
                    Portal pFound = (Portal) e;
                    matchPortal(pFound, pl);
                }
            }
            onLoad(pl);
            entity = pl;
            break;
            
            
        // Collectable objs
        case "key":
            String keyId = json.getString("id");
            Key k = new Key(x, y, keyId);
            onLoad(k);
            entity = k;
            break;
        case "sword":
            Sword sw = new Sword(x, y);
            onLoad(sw);
            entity = sw;
            break;
        case "drill":
            Drill drill = new Drill(x, y);
            onLoad(drill);
            entity = drill;
            break;
        case "potion":
            String potionType = json.getString("potionType");
            Potion p = new Potion(x, y, potionType);
            onLoad(p);
            entity = p;
            break;
        case "treasure":
            Treasure t = new Treasure(x, y);
            onLoad(t);
            entity = t;
            break;
        }
        dungeon.addEntity(entity);
    }

    protected void addDungeonObservers(Dungeon d, Goal goal) {
        // PlayerProgressObserver playerProgressObserver = new PlayerProgressObserver(goalStr);
        PlayerProgressObserver playerProgressObserver = new PlayerProgressObserver(goal);
        this.progressObserver = playerProgressObserver;
        this.progressObserver.setDungeonLoader(this);
        d.attach(playerProgressObserver);
        CurrentLevelObserver currentLevelObserver = new CurrentLevelObserver();
        this.currLevelObserver = currentLevelObserver;
        d.attach(currentLevelObserver);
    }

    protected void addOldObsToNewDungeon(Dungeon newDungeon, Dungeon oldDungeon) {
        newDungeon.setListObservers(oldDungeon.getListObservers());
    }


    public void matchPortal (Portal a, Portal b) {
        if ( !a.getID().equals(b.getID())) {
            return;
        }
     
        if (a.getX() == b.getX() && a.getY() == b.getY()) {
            return;
        }

        a.setDestPortal(b);
        b.setDestPortal(a);
    }

    public abstract void onLoad(Entity entity);
    
}
