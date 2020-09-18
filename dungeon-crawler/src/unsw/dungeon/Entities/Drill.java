package unsw.dungeon.Entities; 
/**
 * A Drill entity in the dungeon.
 *
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Drill extends Collectable {
    
    private IntegerProperty durability;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Drill(int x, int y) {
        super(x,y);
        this.durability = new SimpleIntegerProperty(3);
    }

    public IntegerProperty durability() {
        return this.durability;
    }

    public int getDurability() {
        return this.durability().get();
    }

    public void setDurability(int durability) {
        this.durability().set(durability);
    }

    public void use() {
        this.setDurability(this.getDurability()-1);;
    }

}
