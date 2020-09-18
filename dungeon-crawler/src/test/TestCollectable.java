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


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;




import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;

// @TestInstance(Lifecycle.PER_CLASS)
public class TestCollectable {
    
    Dungeon dungeon;
    Player player;
    Sword sword;
    CurrentLevelObserver holdingObs;
    PlayerProgressObserver progress;

    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(4, 4);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.sword = new Sword(3, 2);
        this.dungeon.addEntity(sword);

        this.player = new Player(this.dungeon, 2, 2);
    }
    
    @Test
    public void testPickupSword() {
        this.player.moveRight();
        assertEquals(this.player.havingSword(), true);
        assertEquals(this.sword.getDurability(), 5);
        assertEquals(this.sword.getDisplay(), false);
    }

}



