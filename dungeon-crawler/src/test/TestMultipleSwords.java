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
public class TestMultipleSwords {
    
    private Dungeon dungeon;
    private Player player;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;
    
    private Sword sword1;
    private Sword sword2;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    private Enemy enemy5;
    private Enemy enemy6; // The enemy to kill the player
    
    
    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(20, 20);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.player = new Player(this.dungeon, 4, 5);
        
        this.sword1 = new Sword(3, 5);
        this.sword2 = new Sword(2, 5);
        this.dungeon.addEntity(sword1);
        this.dungeon.addEntity(sword2);

        this.enemy2 = new Enemy(this.dungeon, 4, 6);
        this.dungeon.addEntity(enemy2);
        this.enemy3 = new Enemy(this.dungeon, 4, 4);
        this.dungeon.addEntity(enemy3);
        this.enemy1 = new Enemy(this.dungeon, 5, 5);
        this.dungeon.addEntity(enemy1);
        this.enemy4 = new Enemy(this.dungeon, 6, 5);
        this.dungeon.addEntity(enemy4);
        this.enemy5 = new Enemy(this.dungeon, 7, 5);
        this.dungeon.addEntity(enemy5);
        
        // Register players to enemies
        this.dungeon.addPlayerToEnemies();
    }

    @Test
    public void testPickUpSwordWhileHolding() {
        // Player picks up the adj sword and walks
        // over the other
        this.player.moveLeft();
        this.player.moveLeft();
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.player.havingSword(), true);
        assertEquals(this.sword1.getDisplay(), false);
        assertEquals(this.sword2.getDisplay(), true);
    }

    @Test
    public void testKillEnemyGoalsAndPickOtherSword() {
        this.player.moveLeft();
        this.player.moveRight();
        
        // =================================
        // Let 5 enemies commit to die 
        // =================================
        this.enemy1.move();
        this.enemy2.move();
        this.enemy3.move();
        // The current implementation let the timer called this function.
        // If the enemy is dead, it cancels its own timer which is not 
        // initialized in the test. Hence, we catch the exception here but still set
        // it's dead.
        try {
            this.dungeon.notifyEnemyMoved(this.enemy3, this.dungeon);
            this.dungeon.notifyEnemyMoved(this.enemy1, this.dungeon);
            this.dungeon.notifyEnemyMoved(this.enemy2, this.dungeon);
        } catch(Exception e) {}
        
        assertEquals(this.enemy1.isDead(), true);
        assertEquals(this.enemy2.isDead(), true);
        assertEquals(this.enemy3.isDead(), true);
    }

}
