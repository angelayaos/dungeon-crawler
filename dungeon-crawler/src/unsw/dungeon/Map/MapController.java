package unsw.dungeon.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import javafx.util.Pair;
import java.util.*;
import java.io.*;
import java.lang.reflect.Type;


public class MapController extends DungeonControllerLoader {
    
    private Dungeon oldDungeon;
    private DungeonController controller;
    private int level;
    public MapController(String filename, 
                        int newLevel, 
                        DungeonController controller, 
                        Dungeon d) 
                        throws FileNotFoundException {
        super(filename);
        this.controller = controller;
        this.oldDungeon = d;
        this.level = newLevel;
        this.wallImage = new Image((new File("images/wall"+String.valueOf(this.level%3)+".png")).toURI().toString()); // "+String.valueOf(this.level%3)+"
    }


    public void run(){
        MapBuilder map = new MapBuilder(this.level, this.oldDungeon);
        Dungeon newDungeon = map.creatDungeon();
        addOldObsToNewDungeon(newDungeon, oldDungeon);
        Platform.runLater(new Runnable(){
        
            @Override
            public void run() {
                controller.setPlayer(newDungeon.getPlayer());
                controller.setDungeon(newDungeon);
                
                // Verison 2: change the gridpane int the contoller
                updateGridPane(controller.getGridPane(), newDungeon);
                
                // Trigger timer of npc to move
                newDungeon.NPCStart();
            }
        });
        
    }

    /**
     * Update the GridPane in according to the new Dungeon
     * @param p
     * @param d the new Dungeon
     */
    private void updateGridPane(GridPane p, Dungeon d) {
        Platform.runLater(new Runnable(){        
            @Override
            public void run() {
                // Clear old children node of the gridpane
                p.getChildren().clear();

                // System.out.println("new dungeon has "+d.getHeight()+"x"+d.getWidth()+" elements");        
                
                // Added surrounding wall on UI
                initialize(p, d);
                for(Entity e: d.getEntities()) {                    
                    onLoad(e);     
                    p.add(entities.get(entities.size()-1), e.getX(), e.getY());               
                }
                // System.out.println("new entities has "+entities.size()+" elements before adding Player");        
                onLoad(d.getPlayer());
                p.add(entities.get(entities.size()-1), d.getPlayer().getX(), d.getPlayer().getY());               
                controller.setDungeon(d);
                controller.setPlayer(d.getPlayer());
                controller.setEntities(entities);

                d.NPCStart();
                // addDungeonObservers(d, progressObserver.getCurrGoal());
                addOldObsToNewDungeon(d, oldDungeon);
                d.setListObservers(oldDungeon.getListObservers());
            }
        });
    }
    /**
     * Adding surrounding walls on UI
     * @param p
     * @param dungeon
     */
    private void initialize(GridPane p, Dungeon dungeon) {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getHeight(); x++) {
            for (int y = 0; y < dungeon.getWidth(); y++) {
                p.add(new ImageView(ground), x, y);
            }
        }

    }
}

