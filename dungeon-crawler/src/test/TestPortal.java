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
public class TestPortal {
    
    private Dungeon dungeon;
    private Player player;
    private Portal p1;
    private Portal p2; 
    private NPC npc;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;

    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(10, 10);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.p1 = new Portal(2, 1, "1");
		this.dungeon.addEntity(p1);
        this.p2 = new Portal(5, 3, p1);
        this.dungeon.addEntity(p2);

        this.npc = new NPC(this.dungeon, 5, 4);
        this.dungeon.addEntity(npc);

        this.player = new Player(this.dungeon, 2, 2);
    }

    @Test
    public void testPortal() {
        this.player.moveUp();

        assertEquals(this.player.getX(), 5);
        assertEquals(this.player.getY(), 3);

        this.player.moveDown();
        this.player.moveUp();

        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 1);

    }

    @Test
    public void testNPCPortalFail() {
        this.npc.moveUp();
        assertEquals(this.npc.getX(), 5);
        assertEquals(this.npc.getY(), 4);
    }

    
}