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
public class TestGoalProgress2 {

    private Dungeon dungeon;
    private Player player;
    private int playerX = 5;
    private int playerY = 5;
    private NPC npc;
    private Treasure treasure;
    private Switch switch1;
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

        this.player = new Player(this.dungeon, 5, 5);
        
        
        this.switch1 = new Switch(this.playerX-2, this.playerY);
        this.dungeon.addEntity(this.switch1);
        this.boulder = new Boulder(this.dungeon, this.playerX-1, this.playerY);
        this.dungeon.addEntity(this.boulder);

        this.treasure = new Treasure(this.playerX, this.playerY-1);
        this.dungeon.addEntity(this.treasure);
        
        // Set the Goal = TREASURE OR BOULDERS OR ENEMIES OR EXIT
        CompositeGoal parentGoal = new CompositeGoal("or");
        parentGoal.addSubgoal(new Subgoal("treasure"));
        parentGoal.addSubgoal(new Subgoal("boulders"));
        parentGoal.addSubgoal(new Subgoal("enemies"));
        parentGoal.addSubgoal(new Subgoal("exit"));
        this.progress.setCurrGoal(parentGoal);
    }

    @Test 
    public void testInitGoalStates() {
        assertEquals(this.progress.finishedGoal(), true);
    }

    @Test 
    public void testSwitchGoal() {
        this.player.moveLeft();
        assertEquals(this.switch1.getState(), true);
        assertEquals(this.progress.getGoalState().get(BasicGoal.BOULDERS), true);
    }

    @Test
    public void testAllGoals() {
        assertEquals(this.progress.finishedGoal(), true);
    }
}