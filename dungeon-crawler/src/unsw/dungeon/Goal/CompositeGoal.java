package unsw.dungeon.Goal; 

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import javafx.beans.property.BooleanProperty;
// import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import unsw.dungeon.Goal.Subgoal.BasicGoal;


public class CompositeGoal implements Goal {
    
    private String goalType;
	private List<Goal> subgoals;
	
	public CompositeGoal() {
		this.subgoals = new ArrayList<>();
		this.goalType = "";
	}

	public CompositeGoal(String goalType) {
		this();
		this.goalType = goalType.trim().toLowerCase();
	}

	public void addSubgoal(Goal g) {
		subgoals.add(g);
	}


    @Override 
    public boolean checkAchieved(LinkedHashMap<BasicGoal, BooleanProperty> goalState) {
        if (this.goalType.equalsIgnoreCase("and")) {
			for (Goal g : subgoals) {
				if (!g.checkAchieved(goalState)) {
					return false;
				}
			}
			return true;
		} else if (this.goalType.equalsIgnoreCase("or")) {
			for (Goal g : subgoals) {
				if (g.checkAchieved(goalState)) {
					return true;
				}
			}
		} else {
			Subgoal basicGoal = (Subgoal) this.subgoals.get(0);
			return basicGoal.checkAchieved(goalState);
		}
		
		return false;
	}

	/**
	 * Initialize Goal based on the IO JSON Object
	 * @param o the JSONObject from the IO Loader
	 */
	@Override
	public void parseInfo(JSONObject o) {
		String goalStr = o.getString("goal");
		this.goalType = goalStr;
		try {

			JSONArray subgoalObjs =  o.getJSONArray("subgoals");
			
			printDebug(" >> get many subgoals with "+this.goalType.toUpperCase()+": ");
			for(int j = 0; j < subgoalObjs.length(); j++) {
				// this.parseInfo(subgoalObjs.getJSONObject(j));
				CompositeGoal nCompositeGoal = new CompositeGoal();
				nCompositeGoal.parseInfo(subgoalObjs.getJSONObject(j));
				this.addSubgoal(nCompositeGoal);
			}
		} catch(Exception ex) {
			if (!goalStr.equalsIgnoreCase("and") && !goalStr.equalsIgnoreCase("or")) {
				Subgoal subgoal = new Subgoal(goalStr);
				this.subgoals.add(subgoal);
				this.goalType = goalStr;			
				printDebug(" >> get basic subgoal ="+goalStr);
			} 
			
		}
	}
	// Examples of complex Goals:
	// { "goal": "AND", 
	//   "subgoals":
	// 	[ { "goal": "exit" },
	// 		{ "goal": "OR", 
	// 		  "subgoals":
	// 			[ 
	// 				{"goal": "enemies" },
	// 				{"goal": "treasure" }
	// 			]
	// 		}
	// 	 ]
	//  }

	// toString of the goal above is:
	// (exit) AND ((enemies) OR (treasure))
	@Override
	public String toString() {
		String init = "";
		// printDebug(" 	>> in toString(), goalType="+this.goalType);
		if (this.goalType.equalsIgnoreCase("and") || this.goalType.equalsIgnoreCase("or")) {
			for (Goal g: this.subgoals) {
				init += "(";
				init += g.toString();
				init += ")";
				if (g!=this.subgoals.get(this.subgoals.size()-1)) 
					init += " \n\t" + this.goalType+"\n";
			}
		} else {
			for(Goal g: this.subgoals) {
				init += g.toString();
			}
		}

		return init;
	}

	private void printDebug(String s) {
		System.out.println(s);
	}
}