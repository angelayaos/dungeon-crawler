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
public class TestTreasure {

    Dungeon dungeon;
    Player player;
    NPC npc;
    Treasure t1, t2, t3, t4, t5;
    CurrentLevelObserver currLevel;
    PlayerProgressObserver progress;

    @BeforeEach
    public void createDungeon() {
        this.currLevel = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(5, 5);
        this.dungeon.attach(currLevel);
        this.dungeon.attach(progress);

        this.t1 = new Treasure(3, 2);
        this.dungeon.addEntity(t1);
        this.t2 = new Treasure(2, 1);
        this.dungeon.addEntity(t2);
        this.t3 = new Treasure(1, 4);
        this.dungeon.addEntity(t3);
        this.t4 = new Treasure(2, 3);
        this.dungeon.addEntity(t4);
        this.t5 = new Treasure(4, 4);
        this.dungeon.addEntity(t5); 

        this.npc = new NPC(this.dungeon, 3, 4);
        this.dungeon.addEntity(npc);

        this.player = new Player(this.dungeon, 2, 2);
        
    }
    
    @Test
    public void testPickupTreasure() {
        this.player.moveUp();
        assertEquals(this.t2.getDisplay(), false);
        this.player.moveRight();
        this.player.moveDown();
        assertEquals(this.t1.getDisplay(), false);
        this.player.moveDown();
        this.player.moveLeft();
        assertEquals(this.t4.getDisplay(), false);
        this.player.moveLeft();
        this.player.moveDown();
        assertEquals(this.t3.getDisplay(), false);
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.t5.getDisplay(), false);
    }

    @Test
    public void testEnemyPickupFail() {
        this.npc.moveRight();
        assertEquals(this.t5.getDisplay(), true);
    }
}