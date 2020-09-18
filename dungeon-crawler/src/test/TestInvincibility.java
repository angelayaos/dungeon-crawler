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
public class TestInvincibility {
    
    private Dungeon dungeon;
    private Player player;
    private int originalY = 5;
    private CurrentLevelObserver holdingObs;
    private PlayerProgressObserver progress;
    
    private int dungeonSize = 12;
    private Enemy enemy1;
    private int original1X = 10;
    private Enemy enemy2;
    private int original2X = 9;
    private Potion potion1;
    private Potion potion2;

    @BeforeEach
    public void createDungeon() {
        this.holdingObs = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(this.dungeonSize, this.dungeonSize);
        this.dungeon.attach(holdingObs);
        this.dungeon.attach(progress);

        this.player = new Player(this.dungeon, 3, this.originalY);
        
        this.dungeon.addEntity(new Wall(this.dungeonSize, this.originalY));
        this.dungeon.addEntity(new Wall(this.dungeonSize, this.originalY-1));
        this.dungeon.addEntity(new Wall(this.dungeonSize, this.originalY+1));
        this.dungeon.addEntity(new Wall(this.dungeonSize, this.originalY+2));
        this.dungeon.addEntity(new Wall(this.dungeonSize, this.originalY-2));

        this.potion1 = new Potion(2, this.originalY, "green");
        this.dungeon.addEntity(this.potion1);
        this.potion2 = new Potion(1, this.originalY, "asdfsda");
        this.dungeon.addEntity(this.potion2);
        this.enemy1 = new Enemy(this.dungeon, this.original1X, this.originalY);
        this.dungeon.addEntity(enemy1);
        this.enemy2 = new Enemy(this.dungeon, this.original2X, this.originalY);
        this.dungeon.addEntity(enemy2);
        this.dungeon.addEntity(new Wall(this.dungeonSize, this.originalY));
        // Register players to enemies
        this.dungeon.addPlayerToEnemies();
    }

    @Test
    public void testEnemyCloserBehaviour() {
        this.enemy1.move();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy1, this.dungeon);
        assertEquals(this.enemy1.getX() < this.original1X, true);
        
        this.enemy2.move();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy1, this.dungeon);
        assertEquals(this.enemy2.getX() < this.original2X, true);
        
        this.player.moveUp();
        this.player.moveUp();
        
        this.enemy1.move();
        this.enemy1.move();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy1, this.dungeon);
        assertEquals(this.enemy1.getX() < this.original1X, true);
        assertEquals(this.enemy1.getY(), 3);
        
        this.player.moveDown();
        this.player.moveDown();
        
        this.enemy1.move();
        this.enemy1.move();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy1, this.dungeon);
        assertEquals(this.enemy1.getX() < this.original1X, true);
        assertEquals(this.enemy1.getY(), this.originalY);
    }

    @Test
    public void testAwayBehaviour() {
        this.player.moveLeft();
        assertEquals(this.potion1.getDisplay(), false);
        assertEquals(this.progress.isPlayerInvincible(), true);
        assertEquals(this.enemy1.getMoveStrategy() instanceof MoveAway, true);
        assertEquals(this.enemy2.getMoveStrategy() instanceof MoveAway, true);

        this.enemy1.move();
        assertEquals(this.enemy1.getX() <= this.dungeonSize-1, true);
        this.enemy1.move();
        assertEquals(this.enemy1.getX() <= this.dungeonSize-1, true);
        this.enemy2.move();
        this.enemy2.move();
        this.enemy2.move();
        this.enemy2.move();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy1, this.dungeon);
        this.dungeon.notifyEnemyMoved(this.enemy2, this.dungeon);
        assertEquals(this.enemy1.getX(), this.dungeonSize-1);
        assertEquals(this.enemy1.getY(), this.originalY);
        assertEquals(this.enemy2.getX(), this.dungeonSize-1);
        assertEquals(this.enemy2.getY(), this.originalY);
        
        this.player.moveUp();
        assertEquals(this.dungeon.isWall(this.dungeonSize, this.originalY), true);
        assertEquals(this.enemy1.tryWalk(this.dungeonSize, this.originalY, "right"), false);
        this.enemy1.move();
        assertEquals(this.enemy1.getX(), this.dungeonSize-1);
        assertEquals(this.enemy1.getY(), this.originalY+1);
        
        this.player.moveDown();
        this.player.moveDown();
        this.player.moveDown();
        assertEquals(this.enemy1.tryWalk(this.dungeonSize, this.originalY, "right"),false);
        this.enemy1.move();
        assertEquals(this.enemy1.getX(), this.dungeonSize-1);
        assertEquals(this.enemy1.getY(), this.originalY);
    }

    @Test
    public void testKillEnemyWithPotion() {
        assertEquals(this.player.getX(), 3);
        assertEquals(this.player.getY(), 5);
        assertEquals(this.enemy1.getY(), 5);
        assertEquals(this.enemy2.getY(), 5);
        // Get the potion on the left and then move right end
        // to kill the enemy
        this.player.moveLeft();
        assertEquals(this.player.isInvincible(), true);
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveRight();
        this.player.moveRight();
        assertEquals(this.player.getX(), this.enemy2.getX());
        // The current implementation let the timer called this function.
        // If the enemy is dead, it cancels its own timer which is not 
        // initialized in the test. Hence, we catch the exception here but still set
        // it's dead.
        try {
            this.dungeon.notifyEnemyMoved(this.enemy2, this.dungeon);        
        } catch(Exception e) {}
        assertEquals(this.enemy2.isDead(), true);
        
    }
}