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




import java.io.*;

@TestInstance(Lifecycle.PER_CLASS)
public class TestLoader {
    
    public Dungeon dungeon;
    public Player player;

    @BeforeEach
    public void createDungeon() {
        this.dungeon = new Dungeon(4, 4);
        this.player = new Player(this.dungeon, 2, 2);
    }
    
    @Test
    public void simpleMove() {
        this.player.moveRight();
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 2);
        this.player.moveLeft();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 2);
        this.player.moveUp();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 1);
        this.player.moveDown();
        assertEquals(this.player.getX(), 2);
        assertEquals(this.player.getY(), 2);
    }

}



