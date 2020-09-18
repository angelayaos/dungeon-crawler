package unsw.dungeon.Entities;    


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


public class Switch extends Entity {
    /**
     * The sate of the Swtich.
     * True if activated.
     * False if not activated (by default).
     */
    private Boolean state;

    public Switch(int x, int y) {
        super(x, y);
        this.state = false;
    }

    public Boolean getState() {
        return this.state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

}
