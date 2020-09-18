package unsw.dungeon;

import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.time.*;
import javafx.util.Pair;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.io.File;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.util.converter.BooleanStringConverter;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;
import unsw.dungeon.Goal.Subgoal.BasicGoal;


/**
 * A JavaFX controller for the dungeon.
 * @author Tuan Ho
 *
 */
public class DungeonController {

    @FXML
    private AnchorPane menu;
    
    @FXML
    private GridPane squares;

    @FXML
    private Label levelUI;

    @FXML
    private Label moneyUI;
    
    @FXML
    private Label invincUI;
    
    @FXML
    private Label hitsUI;
    
    @FXML
    private Label drillsUI;
    

    @FXML
    private Label exitUI;
    
    @FXML
    private Label gameGoalUI;

    @FXML
    private Label enemiesUI;
    
    @FXML
    private Label goldUI;
    
    @FXML
    private Label doorUI;

    @FXML
    private Label bouldersUI;

    private List<ImageView> initialEntities;

    private Player player;
    private Dungeon dungeon;



    private CurrentLevelObserver currLevelObserver;
    private PlayerProgressObserver progressObserver;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.initialEntities = new ArrayList<>(initialEntities);
        this.currLevelObserver = null;
        this.progressObserver = null;
    }

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities,
                            CurrentLevelObserver currLevelObserver, 
                            PlayerProgressObserver progressObserver) {
        this(dungeon, initialEntities);
        this.currLevelObserver = currLevelObserver;
        this.progressObserver = progressObserver;
        this.progressObserver.setController(this);
    }

    @FXML
    public void initialize() {
        // this.squares = (GridPane) this.menu.getChildren().get(this.menu.getChildren().size() - 1);
        
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : initialEntities)
            squares.getChildren().add(entity);
        
        this.trackCurrLevelData();
        this.trackProgressData();
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case UP:
            player.moveUp();
            break;
        case DOWN:
            player.moveDown();
            break;
        case LEFT:
            player.moveLeft();
            break;
        case RIGHT:
            player.moveRight();
            break;
        default:
            break;
        }
    }


    public Label getCurrLvlUI() {
        return this.levelUI;
    }

    public void setCurrLvlUI(Label levelUI) {
        this.levelUI = levelUI;
    }
    

    public Label getMoneyUI() {
        return this.moneyUI;
    }

    public void setMoneyUI(Label moneyUI) {
        this.moneyUI = moneyUI;
    }


    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Dungeon getDungeon() {
        return this.dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }
    
    
    /**
     * Bind the trackCurrLevelData's attributes to the UI
     */
    private void trackCurrLevelData() {

        // Version 2: binding via type conversion
        StringProperty sp4money = new SimpleStringProperty();
        StringProperty sp4hits = new SimpleStringProperty();
        StringProperty sp4drills = new SimpleStringProperty();
        StringConverter<Number> converter = new NumberStringConverter();
        Bindings.bindBidirectional(sp4money, this.currLevelObserver.money(), converter);
        Bindings.bindBidirectional(sp4hits, this.currLevelObserver.hits(), converter);
        Bindings.bindBidirectional(sp4drills, this.currLevelObserver.drills(), converter);
        this.moneyUI.textProperty().bind(sp4money);
        this.hitsUI.textProperty().bind(sp4hits);
        this.drillsUI.textProperty().bind(sp4drills);
        
    }    

    /**
     * Bind the progressObserver's attributes to the UI
     */
    private void trackProgressData() {
        if (this.progressObserver == null) return;
        StringProperty sp4currLevel = new SimpleStringProperty();
        StringConverter<Number> converter = new NumberStringConverter();
        Bindings.bindBidirectional(sp4currLevel, this.progressObserver.currLevel(), converter);
        this.levelUI.textProperty().bind(sp4currLevel);

        // Display invincTime on UI
        Label invincTime = this.invincUI;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
        
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        long timeLeft = progressObserver.getInvincibleTimeEnd() - timeNow();
                        if (timeLeft>0)
                            invincTime.setText(String.valueOf(timeLeft));
                        else invincTime.setText("0");
                    }
                });      
            }       
        }, 1000, 1000);

        // Bind the goal status
        StringConverter<Boolean> bconverter = new BooleanStringConverter();
        for (BasicGoal g: BasicGoal.values()) {
            BooleanProperty goalState = this.progressObserver.getStateBasicGoal(g);
            if (g == BasicGoal.BOULDERS)
                Bindings.bindBidirectional(this.bouldersUI.textProperty(), goalState, bconverter);
            if (g == BasicGoal.TREASURE)
                Bindings.bindBidirectional(this.goldUI.textProperty(), goalState, bconverter);
            if (g == BasicGoal.ENEMIES)
                Bindings.bindBidirectional(this.enemiesUI.textProperty(), goalState, bconverter);
            if (g == BasicGoal.EXIT)
                Bindings.bindBidirectional(this.exitUI.textProperty(), goalState, bconverter);
            if (g == BasicGoal.DOORS)
                Bindings.bindBidirectional(this.doorUI.textProperty(), goalState, bconverter);
        }

        // Display the game of the Goal
        this.gameGoalUI.setText(this.progressObserver.getCurrGoal().toString());
    }    


    public GridPane getGridPane() {
        return this.squares;
    }

    private long timeNow() {
        return Instant.now().getEpochSecond();
    }


    public void setEntities(List<ImageView> entities) {
        this.initialEntities = entities;
    }

    // For debuggin purpose
    private void printDebug(String s) {
        System.out.println(s);
    }

    public void addEntityUI(Entity e) {
        if (e instanceof Potion) {
            Potion p = (Potion) e;
            System.out.println("Produced potion at ("+p.getX()+","+p.getY()+")");
            String type = p.getType();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    ImageView pImage = new ImageView(new Image((new File("images/blue_potion.png")).toURI().toString()));
                    if (type.equals("green"))
                        pImage =new ImageView(new Image((new File("images/green_potion.png")).toURI().toString()));            
                    squares.add(pImage, p.getX(), p.getY());
                    pImage.visibleProperty().bindBidirectional(p.display());
                }
            });  
        }
    }
}

