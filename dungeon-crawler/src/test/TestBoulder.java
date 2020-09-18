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
public class TestBoulder {
    
    private Dungeon dungeon;
    private Player player;
    private Boulder b1, b2, b3;
    private Door door;
    private Switch switch1;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;
    private NPC npc;


    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(10, 10);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.b1 = new Boulder(this.dungeon, 3, 2);
        this.dungeon.addEntity(b1);
        this.b2 = new Boulder(this.dungeon, 4, 4);
        this.dungeon.addEntity(b2);
        this.b3 = new Boulder(this.dungeon, 1, 2);
        this.dungeon.addEntity(b3);

        this.switch1 = new Switch(5, 2);
        this.dungeon.addEntity(switch1);

        this.door = new Door(3, 3, "a");
        this.dungeon.addEntity(door);

        this.npc = new NPC(this.dungeon, 5, 4);
        this.dungeon.addEntity(npc);

        this.player = new Player(this.dungeon, 2, 2);
    }

    @Test
    public void testMoveBoulderRight() {
        this.player.moveRight();
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b1.getX(), 4);
        assertEquals(this.b1.getY(), 2);

    }

    @Test
    public void testMoveBoulderLeft() {
        this.player.moveUp();
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveDown();
        this.player.moveLeft(); 
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b1.getX(), 2);
        assertEquals(this.b1.getY(), 2);

    }

    @Test
    public void testMoveBoulderUp() {
        this.player.moveDown();
        this.player.moveLeft();
        this.player.moveUp();
        assertEquals(this.player.getX(), 1);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b3.getX(), 1);
        assertEquals(this.b3.getY(), 1);

    }

    @Test
    public void testMoveBoulderDown() {
        this.player.moveUp();
        this.player.moveLeft();
        this.player.moveDown();
        assertEquals(this.player.getX(), 1);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b3.getX(), 1);
        assertEquals(this.b3.getY(), 3);

    }

    @Test 
    public void testBoulderBlockedByEntity() {
        this.player.moveUp();
        this.player.moveRight();
        this.player.moveDown();
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 1);
    }

    @Test 
    public void testBoulderObstacleToNPC() {
        this.npc.moveLeft();
        assertEquals(this.npc.getX(), 5);
        assertEquals(this.npc.getY(), 4);
    }

    @Test
    public void testSwitchStateChanged() {
        assertEquals(this.switch1.getState(), false);
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.switch1.getState(), true);
        this.player.moveRight();
        assertEquals(this.player.getX(), 5);
        assertEquals(this.player.getY(), 2);
        assertEquals(this.b1.getX(), 6);
        assertEquals(this.b1.getY(), 2);
        assertEquals(this.switch1.getState(), false);
    }

}