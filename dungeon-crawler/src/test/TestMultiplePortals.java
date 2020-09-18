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
public class TestMultiplePortals {
    
    private Dungeon dungeon;
    private Player player;
    private Portal p1;
    private Portal p2; 
    private Portal p3;
    private Portal p4;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;

    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(10, 10);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.p1 = new Portal(3, 2, "1");
		this.dungeon.addEntity(p1);
        this.p2 = new Portal(7, 2, p1);
        this.dungeon.addEntity(p2);
        this.p3 = new Portal(7, 4, "1");
		this.dungeon.addEntity(p3);
        this.p4 = new Portal(3, 5, p3);
        this.dungeon.addEntity(p4);

        this.player = new Player(this.dungeon, 2, 2);
    }

    @Test
    public void testMultiplePortals() {
        this.player.moveRight();

        assertEquals(this.player.getX(), 7);
        assertEquals(this.player.getY(), 2);

        this.player.moveDown();
        this.player.moveDown();

        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 5);

    }
    
}