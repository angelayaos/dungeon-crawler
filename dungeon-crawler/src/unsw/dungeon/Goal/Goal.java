package unsw.dungeon.Goal;

import javafx.beans.property.BooleanProperty;

import unsw.dungeon.Goal.Subgoal.BasicGoal;

import org.json.*;
import java.util.*;


public interface Goal {
    public boolean checkAchieved(LinkedHashMap<BasicGoal, BooleanProperty> goalState);
    public void parseInfo(JSONObject o);
}
