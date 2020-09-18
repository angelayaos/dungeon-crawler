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


import unsw.dungeon.Goal.Subgoal.BasicGoal;

@TestInstance(Lifecycle.PER_CLASS)
public class TestGoalProgress {

    private Dungeon dungeon;
    private Player player;
    private int playerX = 5;
    private int playerY = 5;
    private NPC npc;
    private Treasure treasure;
    private Enemy enemy;
    private Exit exit;
    private Switch switch1;
    private Sword sword;
    private Boulder boulder;
    private CurrentLevelObserver currLevel;
    private PlayerProgressObserver progress;

    @BeforeEach
    public void createDungeon() {
        this.currLevel = new CurrentLevelObserver();
        this.progress = new PlayerProgressObserver("");
        this.dungeon = new Dungeon(10, 10);
        this.dungeon.attach(currLevel);
        this.dungeon.attach(progress);

        this.player = new Player(this.dungeon, this.playerX, this.playerY);
        
        this.enemy = new Enemy(this.dungeon, this.playerX+2, this.playerY);
        this.dungeon.addEntity(this.enemy);
        this.sword = new Sword(this.playerX+1, this.playerY);
        this.dungeon.addEntity(this.sword);

        this.switch1 = new Switch(this.playerX-2, this.playerY);
        this.dungeon.addEntity(this.switch1);
        this.boulder = new Boulder(this.dungeon, this.playerX-1, this.playerY);
        this.dungeon.addEntity(this.boulder);

        this.treasure = new Treasure(this.playerX, this.playerY-1);
        this.dungeon.addEntity(this.treasure);
        this.exit = new Exit(this.playerX, this.playerY+1);
        this.dungeon.addEntity(this.exit);

        this.dungeon.addPlayerToEnemies();
        
        // Set the Goal = TREASURE AND BOULDERS AND ENEMIES AND EXIT
        CompositeGoal parentGoal = new CompositeGoal("and");
        parentGoal.addSubgoal(new Subgoal("treasure"));
        parentGoal.addSubgoal(new Subgoal("boulders"));
        parentGoal.addSubgoal(new Subgoal("enemies"));
        parentGoal.addSubgoal(new Subgoal("exit"));
        this.progress.setCurrGoal(parentGoal);
    }

    @Test 
    public void testInitGoalStates() {
        assertEquals(this.progress.finishedGoal(), false);
    }

    @Test
    public void testTreasureGoal() {
        this.player.moveUp();
        this.player.moveDown();
        assertEquals(this.treasure.getDisplay(), false);
        assertEquals(this.dungeon.getEntities().contains(this.treasure), false);
        assertEquals(this.progress.getGoalState().get(BasicGoal.TREASURE), true);
        assertEquals(this.progress.finishedGoal(), false);
    }


    @Test
    public void testExitGoal() {
        this.player.moveDown();
        assertEquals(this.player.getY(), this.playerY+1);
        this.player.moveUp();
        assertEquals(this.exit.isActive(), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.EXIT), true);
        assertEquals(this.progress.finishedGoal(), false);
    }

    @Test
    public void testEnemyGoal() {
        this.player.moveRight();
        assertEquals(this.player.havingSword(), true);
        // this.player.moveRight();
        this.enemy.moveLeft();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy, this.dungeon);        
        assertEquals(this.player.getX(), this.enemy.getX());
        assertEquals(this.player.getY(), this.enemy.getY());
        this.player.moveRight();
        assertEquals(this.enemy.isDead(), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.ENEMIES), true);
        assertEquals(this.progress.finishedGoal(), false);
    }

    @Test
    public void testSwitchGoal() {
        this.player.moveLeft();
        this.player.moveRight();
        assertEquals(this.switch1.getState(), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.BOULDERS), true);
    }
    
    @Test
    public void testAllGoals() {
        this.player.moveLeft();
        this.player.moveRight();
        this.player.moveDown();
        this.player.moveUp();
        this.player.moveUp();
        this.player.moveDown();
        this.player.moveRight();
        this.enemy.moveLeft();
        // The current implementation let the timer called this function.
        this.dungeon.notifyEnemyMoved(this.enemy, this.dungeon);        
        assertEquals(this.player.getX(), this.enemy.getX());
        assertEquals(this.player.getY(), this.enemy.getY());
        this.player.moveRight();
        
        assertEquals(this.progress.getGoalState().get(BasicGoal.EXIT), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.ENEMIES), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.TREASURE), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.BOULDERS), true);
        assertEquals(this.progress.finishedGoal(), true);
    }
}