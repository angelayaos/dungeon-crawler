package unsw.dungeon;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;




public interface DungeonSubject {
	public void attach(PlayerProgressObserver o);
	public void attach(CurrentLevelObserver o);
	public void notifyPlayerMoved(Player p, Dungeon d);
	public void notifyEnemyMoved(Enemy e, Dungeon d);
	public void notifyBoulderMoved(Dungeon d);
	public void notifyPotionProduced(Potion p, Dungeon d);
	
	/**
	 * This method is created because the drill implementation does 
	 * not let the observer handles the interaction.
	 */
	public void notifyUsedDrill();
}

