package test;

// For testing and generate coverage with junit4. 
// Please don't delete the following comments
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
import org.junit.jupiter.api.condition.*;


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;





@TestInstance(Lifecycle.PER_CLASS)
public class TestPlayerMovement {
    private Dungeon dungeon;
    private Player player;
    private Boulder b;
    private Switch switch1;
    private Door door;
    private Key key;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;


    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(10, 10);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        Wall w1 = new Wall(1,1);
        this.dungeon.addEntity(w1);
        Wall w2 = new Wall(1,2);
        this.dungeon.addEntity(w2);

        this.b = new Boulder(this.dungeon, 3,2);
        this.dungeon.addEntity(b);
        this.switch1 = new Switch(5, 2);
        this.dungeon.addEntity(switch1);
        this.door = new Door(2, 3, "a");
        this.dungeon.addEntity(door);
        this.key = new Key(2, 2, "a");
        this.dungeon.addEntity(key);

        this.player = new Player(this.dungeon, 2, 2);
    }

    @Test
    public void testHittingWall() {
        this.player.moveLeft();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 2);
    }

    @Test
    public void testMoveBoulder() {
        this.player.moveRight();
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b.getX(), 4);
        assertEquals(this.b.getY(), 2);
    }

    @Test
    public void switchStateChanged() {
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.switch1.getState(), true);
        this.player.moveRight();
        assertEquals(this.player.getX(), 5);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b.getX(), 6);
        assertEquals(this.b.getY(), 2);
        assertEquals(this.switch1.getState(), false);
    }

    @Test
    public void testWalkClosedDoor() {
        assertFalse(this.door.getState());
        this.player.moveDown();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 2);
    }

    @Test
    public void testWalkOpenDoor() {
        assertFalse(this.door.getState());
        this.player.moveUp();
        this.player.moveDown();
        this.player.moveDown();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 3);
        assertEquals(this.door.getState(), true);
        this.player.moveDown();
        this.player.moveUp();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 3);

    }

}



