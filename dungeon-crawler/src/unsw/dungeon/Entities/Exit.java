package unsw.dungeon.Entities;    


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



public class Exit extends Entity {

    private boolean isActive; 

    public Exit(int x, int y) {
        super(x, y);
        this.isActive = false;
    }

    public void activate() {
      if (this.isActive == false) {
        this.isActive = true;
      }
    }

    public void deactivate() {
      if (this.isActive == true) {
        this.isActive = false;
      }
    }
    
    public boolean isActive() {
      return isActive;
    }

}
