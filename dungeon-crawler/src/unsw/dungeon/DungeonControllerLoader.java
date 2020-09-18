package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import java.io.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
// import javafx.scene.text.Text;

import java.io.File;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Tuan Ho
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    protected List<ImageView> entities = new ArrayList<>();
    protected List<ImageView> collectables = new ArrayList<>();
    protected List<ImageView> doors = new ArrayList<>();
    protected List<ImageView> NPC = new ArrayList<>();
    protected List<ImageView> walls = new ArrayList<>();

    //Moving Images
    protected Image playerImage = new Image((new File("images/human_new.png")).toURI().toString());
    protected Image playerHasSwordImage = new Image((new File("images/human_sword.png")).toURI().toString());
    protected Image playerDrillImage = new Image((new File("images/human_drill.png")).toURI().toString());   
    protected Image playerDrillSword = new Image((new File("images/human_sword_drill.png")).toURI().toString());
    protected Image houndImage = new Image((new File("images/hound.png")).toURI().toString());
    protected Image enemyImage = new Image((new File("images/enemy.png")).toURI().toString());
    protected Image gnomeImage = new Image((new File("images/gnome.png")).toURI().toString());

    protected Image boulderImage = new Image((new File("images/boulder.png")).toURI().toString());

    protected Image wallImage = new Image((new File("images/wall1.png")).toURI().toString()); // "+String.valueOf(this.level%3)+"
    protected Image brokenWallImage = new Image((new File("images/broken_wall.png")).toURI().toString());
    protected Image exitImage = new Image((new File("images/exit.png")).toURI().toString());
    protected Image portalImage = new Image((new File("images/portal.png")).toURI().toString());
    protected Image switchImage = new Image((new File("images/switch.png")).toURI().toString());
    protected Image openedDoorImage = new Image((new File("images/open_door.png")).toURI().toString());
    protected Image closedDoorImage = new Image((new File("images/closed_door.png")).toURI().toString());
    protected Image drillImage = new Image((new File("images/drill.png")).toURI().toString());
    


    // Collectable image
    protected Image keyImage = new Image((new File("images/key.png")).toURI().toString());
    protected Image swordImage = new Image((new File("images/sword.png")).toURI().toString());
    protected Image greenPotionImage = new Image((new File("images/green_potion.png")).toURI().toString());
    protected Image bluePotionImage = new Image((new File("images/blue_potion.png")).toURI().toString());
    protected Image treasureImage = new Image((new File("images/gold.png")).toURI().toString());


    public DungeonControllerLoader(String filename)
            throws FileNotFoundException {
        super(filename);        
    }

    @Override
    public void onLoad(Entity entity) {

        if (entity instanceof Player) {
            ImageView view = new ImageView(this.playerImage);
            view.toFront();
            trackPlayerWeapon((Player)entity, view);
            trackPlayerDrill((Player)entity, view);
            addEntity(entity, view);
        }
    
        if (entity instanceof Enemy) {
            ImageView view = new ImageView(this.enemyImage);
            addEntity(entity, view);
            addNPC((NPC)entity, view);
        }
    
        if (entity instanceof Hound) {
            ImageView view = new ImageView(this.houndImage);
            addEntity(entity, view);
            addNPC((NPC) entity, view);
        }
    
        if (entity instanceof Gnome) {
            ImageView view = new ImageView(this.gnomeImage);
            addEntity(entity, view);
            addNPC((NPC) entity, view);
        }
        
        if (entity instanceof Boulder) {
            ImageView view = new ImageView(this.boulderImage);
            addEntity(entity, view);
        }
    
        // Static objs
    
        if (entity instanceof Wall) {
            ImageView view = new ImageView(this.wallImage);
            addEntity(entity, view);
            addWall((Wall)entity, view);
        }
        
        if (entity instanceof Exit) {
            ImageView view = new ImageView(this.exitImage);
            addEntity(entity, view);
        }
        
        if (entity instanceof Switch) {
            ImageView view = new ImageView(this.switchImage);
            addEntity(entity, view);
        }
    
        if (entity instanceof Portal) {
            ImageView view = new ImageView(this.portalImage);
            addEntity(entity, view);
        }
    
        if (entity instanceof Door) {
            ImageView view = new ImageView(this.closedDoorImage);
            addEntity(entity, view);
            addDoor((Door) entity, view);
        }
        
        // Collectable objs
        if (entity instanceof Key) {
            ImageView view = new ImageView(this.keyImage);
            addEntity(entity, view);
            addCollectable((Collectable) entity, view);
        }
    
        if (entity instanceof Sword) {
            ImageView view = new ImageView(this.swordImage);
            addEntity(entity, view);
            addCollectable((Collectable)entity, view);
        }
    
        if (entity instanceof Drill) {
            ImageView view = new ImageView(this.drillImage);
            addEntity(entity, view);
            addCollectable((Collectable)entity, view);
        }
        
        if (entity instanceof Potion) {
            Potion potion = (Potion) entity;
            Image image = potion.getType().equalsIgnoreCase("green") ? this.greenPotionImage : this.bluePotionImage;
            ImageView view = new ImageView(image);
            addEntity(potion, view);
            addCollectable((Collectable) potion, view);
        }
        
        if (entity instanceof Treasure) {
            ImageView view = new ImageView(this.treasureImage);
            addEntity(entity, view);
            addCollectable((Collectable)entity, view);
        }
    }
    
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entities.add(view);
    }

    private void addCollectable(Collectable c, ImageView view) {
        trackCollectableDisplay(c, view);
        this.collectables.add(view);
    }

    private void addDoor(Door d, ImageView view) {
        trackDoorDisplay(d, view);
        this.doors.add(view);
    }

    private void addWall(Wall w, ImageView view) {
        trackWallDisplay(w, view);
        this.walls.add(view);
    }

    private void addNPC(NPC n, ImageView view) {
        trackNPCDisplay(n, view);
        this.NPC.add(view);
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });
    }

    private void trackCollectableDisplay(Collectable c, Node node) {                
        c.display().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) {
                node.setVisible(newValue.booleanValue());
                node.toBack();
                c.setX(1);
                c.setY(1);
                GridPane.setColumnIndex(node, c.getX());
                GridPane.setRowIndex(node, c.getY());
            }
        });
    }

    
    private void trackNPCDisplay(NPC n, Node node) {
        GridPane.setColumnIndex(node, n.getX());
        GridPane.setRowIndex(node, n.getY());
        
        n.display().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    node.setVisible(newValue.booleanValue());
                    node.toBack();
                    n.setX(1);
                    n.setY(1);
                }
            }
        });
    }

    private void trackDoorDisplay(Door d, Node node) {
        d.state().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    ImageView n = (ImageView) node;
                    n.setImage(openedDoorImage);
                }
            }
        });
    }

    private void trackWallDisplay(Wall w, Node node) {
        w.state().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    ImageView n = (ImageView) node;
                    n.setImage(brokenWallImage);
                }
            }
        });
    }

    private void trackPlayerWeapon(Player p, Node node) {
        p.hasSword().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
                ImageView n = (ImageView) node;
                if (newValue) {
                    if (p.havingDrill()) {
                        n.setImage(playerDrillSword);
                    } else {
                        n.setImage(playerHasSwordImage);
                    }
                } else { 
                    if (p.havingDrill()) {
                        n.setImage(playerDrillImage);
                    } else n.setImage(playerImage);
                }
            }
        });
    }

    private void trackPlayerDrill(Player p, Node node) {
        p.usingDrill().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue) {
                ImageView n = (ImageView) node;
                if (newValue) {
                    if (p.havingSword()) {
                        n.setImage(playerDrillSword);
                    } else {
                        n.setImage(playerDrillImage);
                    }
                } else { 
                    if (p.havingSword()) {
                        n.setImage(playerHasSwordImage);
                    } else {
                    n.setImage(playerImage);
                    }
                 }
            }
        });
    }



    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities, this.getCurrLevelObserver(), this.getProgressObserver());
    }


}
