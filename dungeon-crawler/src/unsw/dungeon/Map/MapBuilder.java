package unsw.dungeon.Map;

import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.File;
import java.util.*;
import java.util.Random;
import java.lang.Math.*;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Entities.Portal;
import unsw.dungeon.Movement.*;
import unsw.dungeon.Map.RandomString;

/**
 * Relevant issue: https://gitlab.cse.unsw.edu.au/COMP2511/20T2/W13B-angela_tuan_steven/-/issues/14
 * 
 */
public class MapBuilder {
    private int level;
    private final int maxHeight = 31;
    private final int maxWidth = 21;
    private int height;
    private int width;
    private ArrayList<DungeonObserver> obs;
    private Dungeon oldDungeon;
    
    public MapBuilder(int level, Dungeon oldDungeon) {
        this.level = level > 1 ? level : 1;

        int lastHeight = oldDungeon.getHeight();
        int lastWidth = oldDungeon.getWidth();
        if (level == 1) {
            this.height = 15;
            this.width = 15;
        } else {
            this.height = lastHeight + level-1 > this.maxHeight ? maxHeight : lastHeight + level-1;
            this.width = lastWidth + level-1 > this.maxWidth ? maxWidth : lastWidth + level-1; 
        }

        this.obs = oldDungeon.getListObservers();
        this.oldDungeon = oldDungeon;
    }


    public Dungeon creatDungeon() {
        Dungeon d = new Dungeon(this.width, this.height);
        d.setListObservers(this.obs);
        
        this.addedSurroundingwalls(d);
        this.addWallMaze1(d);
        this.addEntities(d);
        Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
        // Randomly allocating the player would get confused to find out
        // where he is
        // d.setPlayer(new Player(d, co.getKey(), co.getValue()));
        d.setPlayer(new Player(d, 2, 2));
        return d;
    }

    private void addedSurroundingwalls(Dungeon d) {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (i == 0 || j == 0 || i == this.height - 1 || j == this.width - 1) {
                    d.addEntity(new Wall(i, j));
                }
            }
        }
        this.addMoreWall(d);
    }

    private void addMoreWall(Dungeon d) {
        // A cell should be a Wall if it AND its neighbours only
        // have 2 adjacent neighours diagonally and (horizontally or vertically)
        for (int i = 0 ; i < this.height*2; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addWallSuitably(co.getKey(), co.getValue());
        }
    }

    private void addWallMaze1(Dungeon d) {
        MazeGenerator maze = new MazeGenerator(this.height, this.width);
        int[][] mazeM = maze.getMatrix();
        for (int i = 1; i < this.height-1; i++) {
            for (int j = 1; j < this.width-1; j++) {
                if (((mazeM[i][j] & 1) == 0)&&((mazeM[i][j] & 8) == 0)) {
                    d.addEntity(new Wall(i, j));
                }
            }
        }
    }

    /** Acceptance criteria
     *     - The Player progresses to another higher Level when he completes the goal(s) 
     *       at the current Level
     *     - When the Player enter the new Level, all entities are renewed.
     *     - When he reaches to the new level, 
     *          - The # of Enemy = level * 2
     *          - The # of Treasure = level * 2
     *          - # Boulder = # of Switches + 2
     *          - # pairs of Portal = floor (level * 1.5)
     *          - # of drills = ceil (level)
     *          - Map becomes a more sophisticated and 
     *
     */
    private void addEntities(Dungeon d) {
        // Add one exit
        Pair<Integer, Integer> newCO = this.genRandNotFilledPostion(d);
        Exit exit = new Exit(newCO.getKey(), newCO.getValue());
        d.addEntity((Entity) exit);

        // Add # of Enemy = level * 2
        int nEnemies = (int) Math.floor((double)this.level*1.9);
        int midX = (int) Math.floor((double) this.height/2);
        int midY = (int) Math.floor((double) this.width/2);
        for (int i = 0; i < nEnemies; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            while(co.getKey() < midX && co.getValue() < midY)
                co = this.genRandNotFilledPostion(d);
            d.addEntity(new Enemy(d, co.getKey(), co.getValue()));
            Pair<Integer, Integer> co2 = this.genRandNotFilledPostion(d);
            while(co2.getKey() < midX && co.getValue() < midY)
                co2 = this.genRandNotFilledPostion(d);
            d.addEntity(new Hound(d, co2.getKey(), co2.getValue()));
        }

        // By the specs requirements, add up to 3 closed door and key
        for (int i = 0; i < 3; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            Pair<Integer, Integer> co2 = this.genRandNotFilledPostion(d);
            RandomString rand = new RandomString();
            String randId = rand.getAlphaNumericString(10);
            d.addEntity(new Key(co.getKey(), co.getValue(), randId));
            d.addEntity(new Door(co2.getKey(), co2.getValue(), randId));            
        }

        // Add # of Sword = ceil(level * 1)
        int nSwords = (int) Math.ceil((double)this.level);
        for (int i = 0; i < nSwords; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addEntity(new Sword(co.getKey(), co.getValue()));
        }
        
        // Add The # of Treasure = level * 2
        int nTreasure = (int) Math.ceil((double)this.level*1.5);
        for (int i = 0; i < nTreasure; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addEntity(new Treasure(co.getKey(), co.getValue()));
        }
        
        // Add # Boulder = # of Switches + 2 where #Switches = ceil(level*1.15)
        int nSwitches = (int) Math.ceil((double)this.level/(double)1.15);
        for (int i = 0; i < nSwitches; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addEntity(new Switch(co.getKey(), co.getValue()));
        }
        int nBoulders = nSwitches+3;
        for (int i = 0; i < nBoulders; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addEntity(new Boulder(d, co.getKey(), co.getValue()));
        }
        
        // Add # pairs of Portal = floor (level * 1.5)
        int nPortals = (int) Math.floor((double)this.level*(double)1.5);
        for (int i = 0; i < nPortals; i++) {
            RandomString rand = new RandomString();
            String randId = rand.getAlphaNumericString(10);
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            Portal portal1 = new Portal(co.getKey(), co.getValue(), randId);
            d.addEntity(portal1);
            Pair<Integer, Integer> co2 = this.genRandNotFilledPostion(d);
            d.addEntity(new Portal(co2.getKey(), co2.getValue(), portal1));
        }
     
        // Add # of drills = ceil (level/2)
        int nDrills = (int) Math.ceil((double)this.level*3);
        for (int i = 0; i < nDrills; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addEntity(new Drill(co.getKey(), co.getValue()));
        }

        // Added some potion:
        // # potions = floor(level*1.5)
        int nPotions = (int) Math.floor((double)this.level*1.1);
        for (int i = 0; i < nPotions; i++) {
            Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
            d.addEntity(new Potion(co.getKey(), co.getValue(), "blue"));
            Pair<Integer, Integer> co2 = this.genRandNotFilledPostion(d);
            d.addEntity(new Potion(co2.getKey(), co2.getValue(), "green"));
        }

        // Add one Gnome
        Pair<Integer, Integer> co = this.genRandNotFilledPostion(d);
        d.addEntity(new Gnome(d, co.getKey(), co.getValue()));

    }


    private Pair<Integer, Integer> genRandNotFilledPostion(Dungeon d) {
        // Pair<Integer, Integer> pair = new Pair<Integer, Integer>();
        int x = 0;
        int y = 0;
        while(d.anyEntityInPosition(x, y)) {
            x = new Random().nextInt(d.getHeight());
            y = new Random().nextInt(d.getWidth());
        }
        return new Pair<Integer, Integer>(x,y);
    }

}

