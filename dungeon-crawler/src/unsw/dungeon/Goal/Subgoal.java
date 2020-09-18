package unsw.dungeon.Goal;

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import javafx.beans.property.BooleanProperty;

import unsw.dungeon.Goal.Subgoal.BasicGoal;

import org.json.*;
import java.util.*;


public class Subgoal implements Goal {

	public enum BasicGoal {
		EXIT,
		ENEMIES,
		BOULDERS,
		TREASURE,
		DOORS,
		POTIONS
	}

    private String goalTitle;
    
    public Subgoal(String goalTitle) {
		this.goalTitle = goalTitle;
    }

    @Override
	public boolean checkAchieved(LinkedHashMap<BasicGoal, BooleanProperty> goalState) {
		// System.out.println(" >> evaluating "+this.goalTitle);
		if (goalTitle.equals("exit")) {
			return goalState.get(BasicGoal.EXIT).get();
        } else if (goalTitle.equals("enemies")) {
			return goalState.get(BasicGoal.ENEMIES).get();
		} else if (goalTitle.equals("boulders")) {
			return goalState.get(BasicGoal.BOULDERS).get();
		} else if (goalTitle.equals("treasure")) {
			return goalState.get(BasicGoal.TREASURE).get();
		} else if (goalTitle.equals("doors")) {
			return goalState.get(BasicGoal.DOORS).get();
		}
		return false;
	}

	/**
	 * Initialize Goal based on the IO JSON Object
	 * @param o the JSONObject from the IO Loader
	 */
	@Override
	public void parseInfo(JSONObject o) {
		
	}

	@Override
	public String toString() {
		return this.goalTitle;
	}

}