package unsw.dungeon.Entities;  


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity in the dungeon.
 * @author Tuan Ho
 *
 */
public class Entity {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private IntegerProperty x, y;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getY() {
        return y().get();
    }

    public int getX() {
        return x().get();
    }

    public void setX(int x) {
        this.x().set(x);
    }

    public void setY(int y) {
        this.y().set(y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass().equals(obj.getClass()))
            return false;
        
        Entity e = (Entity) obj;
        return this.getX() == e.getX() && this.getY() == e.getY();
        
    }

    // @Override
    // public Entity clone() {
    //     Entity newE = new Entity(this.getX(), this.getY());
    //     return newE;
    // }
}
