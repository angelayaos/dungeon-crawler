package unsw.dungeon;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



public interface DungeonObserver {
    public void updatePlayerMoved(Player p, Dungeon d);
    public void updateBoulderMoved(Dungeon d);
    public void updateEnemyMoved(Enemy e, Dungeon d);
    public void updatePotionProduced(Potion p, Dungeon d);

    /**
	 * This method is created because the drill implementation does 
	 * not let the observer handles the interaction.
	 */
    public void updateDrillUsed();
}



