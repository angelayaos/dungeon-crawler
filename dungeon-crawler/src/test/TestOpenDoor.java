package test;

// For testing and generate coverage with junit4. 
// Please don't delete the following comments.
// import org.junit.*;
// import static org.junit.Assert.*;
// import org.junit.runners.Parameterized;
// import org.junit.runners.Parameterized.Parameters;
// import org.junit.runner.RunWith;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;




import java.io.*;

@TestInstance(Lifecycle.PER_CLASS)
public class TestOpenDoor {
    
    private Dungeon dungeon;
    private Player player;
    private Key key;
    private Door door;
    private Enemy enemy;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;

    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(10, 1);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.key = new Key(2, 1, "a");
        this.dungeon.addEntity(key);
        this.door = new Door(4, 1, "a");
        this.dungeon.addEntity(door);
        assertFalse(this.door.getState());
        
        this.player = new Player(this.dungeon, 3, 1);

        this.enemy = new Enemy(this.dungeon, 5, 1);
        this.dungeon.addEntity(this.enemy);
        this.dungeon.addPlayerToEnemies();
    }
    
    @Test
    public void testMoveOverClosedDoor() {
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 1);
        assertEquals(this.door.getState(), false);
        
        this.enemy.moveLeft();
        assertEquals(this.enemy.getX(), 5);
        assertEquals(this.enemy.getY(), 1);
    }

    @Test
    public void testPickupKey() {
        this.player.moveRight();
        assertEquals(this.key.getDisplay(), true);
        this.player.moveLeft();
        assertEquals(this.key.getDisplay(), false);
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.door.getState(), true);
        assertEquals(this.player.getX(), 4);
        assertEquals(this.player.getY(), 1);
        
        this.player.moveLeft();
        this.player.moveLeft();
        this.enemy.moveLeft();
        assertEquals(this.enemy.getX(), 4);
        assertEquals(this.enemy.getY(), 1);
    }

}



