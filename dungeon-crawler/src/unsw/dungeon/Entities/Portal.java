package unsw.dungeon.Entities;   


import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;


public class Portal extends Entity {

    private String id;
    private Portal destPortal;

    public Portal(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }
    
    public Portal(int x, int y, Portal destPort) {
        super(x, y);
        this.destPortal = destPort;
        destPort.setDestPortal(this);
    }
    
    public Portal getDestPortal() {
        return this.destPortal;
    }

    public void setDestPortal(Portal destPortal) {
        this.destPortal = destPortal;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public int getX() {
        return super.getX();
    }
}
