/**
 *
 */
package unsw.dungeon;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.*;



import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;



/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author 
 *
 */
public class Dungeon implements DungeonSubject {

	private int width;
    private int height;
    private Player player;
    private ArrayList<DungeonObserver> listObservers;
    private List<Entity> entities;
    


    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.player = null;
        
        this.listObservers = new ArrayList<>();
        this.entities = new ArrayList<>();
        
    }

    // ==========================================
    //      Setters & Getters of this class    ||
    // ==========================================


    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }


    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<DungeonObserver> getListObservers() {
        return this.listObservers;
    }

    public void setListObservers(ArrayList<DungeonObserver> obs) {
        this.listObservers = obs;
    }

    public void addListObservers(DungeonObserver listObservers) {
        this.listObservers.add(listObservers);
    }
    
    public void removeListObservers(DungeonObserver listObservers) {
        this.listObservers.remove(listObservers);
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }
    
    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    private List<NPC> getNPCs() {
        //  List<NPC> res = this.entities.stream().filter(o -> o instanceof NPC)
        // .map(o -> (NPC) o)
        // .collect(Collectors.toList());
        // return res;

        // Using generics
        return this.genericGetAllEntitiesByType("NPC").stream().map(o -> (NPC) o).collect(Collectors.toList());
    }

    public List<Enemy> getEnemies() {
        List<Enemy> res = this.entities.stream().filter(o -> o instanceof Enemy)
                                                .map(o -> (Enemy) o)
                                                .collect(Collectors.toList());
        return res;
    }



    public List<Wall> getWalls() {
        // Imperative way
        // List<Wall> res = new ArrayList<>();
        // for (Entity e: this.entities) {
        //     if (e instanceof Wall) {
        //         Wall w = (Wall) e;
        //         res.add(w);
        //     }
        // }
        // return res;

        // Using lambda expr
        List<Wall> res = this.entities.stream().filter(o -> o instanceof Wall)
                                                .map(o -> (Wall) o)
                                                .collect(Collectors.toList());
        return res;
    }


    public List<Boulder> getBoulders() {
        // List<Boulder> res = this.entities.stream().filter(o -> o instanceof Boulder)
        //                                         .map(o -> (Boulder) o)
        //                                         .collect(Collectors.toList());
        // return res;

        // Using generics
        List<Boulder> res = genericGetAllEntitiesByType("Boulder").stream()
                                                                        .map(o -> (Boulder) o)
                                                                        .collect(Collectors.toList());
        return res;
    }

    
    public List<Switch> getSwitches() {
        List<Switch> res = this.entities.stream().filter(o -> o instanceof Switch)
                                                .map(o -> (Switch) o)
                                                .collect(Collectors.toList());
        return res;
    }
    
    public List<Door> getDoors() {
        List<Door> res = this.entities.stream().filter(o -> o instanceof Door)
                                                .map(o -> (Door) o)
                                                .collect(Collectors.toList());
        return res;
    }

    

    public List<Collectable> getCollectables() {
        List<Collectable> res = this.entities.stream().filter(o -> o instanceof Collectable)
                                                .map(o -> (Collectable) o)
                                                .collect(Collectors.toList());
        return res;
    }


    public List<Potion> getPotions() {
        // List<Potion> res = this.entities.stream().filter(o -> o instanceof Potion)
        List<Potion> res = this.genericGetAllEntitiesByType("Potion").stream()
                                                .map(o -> (Potion) o)
                                                .collect(Collectors.toList());
        return res;
    }

    public List<Portal> getPortals() {
        // List<Portal> res = this.entities.stream().filter(o -> o instanceof Portal)
        List<Portal> res = this.genericGetAllEntitiesByType("Portal").stream()
                                                .map(o -> (Portal) o)
                                                .collect(Collectors.toList());
        return res;
    }


    // ==========================================
    //      Util funcs of this class           ||
    // ==========================================

    public void NPCStart() {
        List<NPC> NPCs = this.getNPCs();
        for (NPC n: NPCs) {
            n.startMoving();
        }
        this.addPlayerToEnemies();
    }


    public boolean isWall(int x, int y) {
        List<Wall> walls = getWalls();
        List<Wall> res = walls.stream()
        .filter(o -> o.getX() == x  && o.getY() == y)
        .collect(Collectors.toList());

        if (!res.isEmpty()) {
            // System.out.println("isWall("+x+","+y+")="+true);
            return true;
        }
        return false;

    }

    public boolean isOutside(int x, int y) {
        if (x < 0 || x >= this.width
            || y < 0 || y >= this.height) {
            return true;
        }

        return false;
    }

    public boolean isBoulder(int x, int y) {
        Boulder adjBoulder = findBoulderByPosition(x, y);
        return (adjBoulder != null);
    }

    public boolean isPotion(int x, int y) {
        Potion adjPotion = findPotionByPosition(x, y);
        return (adjPotion != null);
    }

    public boolean interactBoulder(int x, int y, Player player, String direction) {

        Boulder adjBoulder = findBoulderByPosition(x, y);
        switch(direction) {
            case "up":
                if (adjBoulder.tryWalk(x, y-1)
                && (player.getY() > 0)) {
                    if (adjBoulder != null){
                        adjBoulder.moveUp();
                        notifyBoulderMoved(this);
                    }
                    return true;
                }
                break;
            case "down":
                if (adjBoulder.tryWalk(x, y+1)
                && (player.getY() < getHeight() - 1)) {
                    if (adjBoulder != null){
                        adjBoulder.moveDown();
                        notifyBoulderMoved(this);
                    }
                    return true;
                }
                break;
            case "left":
                if (adjBoulder.tryWalk(x-1, y)
                && (player.getX() > 0)) {
                    if (adjBoulder != null){
                        adjBoulder.moveLeft();
                        notifyBoulderMoved(this);
                    }
                    return true;
                }
                break;
            case "right":
                if (adjBoulder.tryWalk(x+1, y)
                && (player.getX() < getWidth() - 1)) {
                    if (adjBoulder != null){
                        adjBoulder.moveRight();
                        notifyBoulderMoved(this);
                    }
                    return true;
                }
                break;
        }

        return false;
    }


    public boolean isPortal(int x, int y) {
        Portal adjPortal = findPortalByPosition(x, y);
        return (adjPortal != null);
    }

    public boolean isDoor(int x, int y) {
        Door adjDoor = (Door) genericFindEntityByPosition("Door", x, y);
        return (adjDoor != null);

    }

    public boolean isOpen(int x, int y) {
        Door adjDoor = (Door) genericFindEntityByPosition("Door", x, y);
        return adjDoor.getState();
    }

    public boolean checkKey(int x, int y, String id) {
        Door adjDoor = (Door) genericFindEntityByPosition("Door", x, y);
        return(id.equals(adjDoor.getId()));
    }

    public void breakWall(int x, int y) {
        Wall adjWall = (Wall) genericFindEntityByPosition("Wall", x, y);
        adjWall.setOpen();
        this.notifyUsedDrill();
    }

    public boolean isBroken(int x, int y) {
        Wall adjWall = (Wall) genericFindEntityByPosition("Wall", x, y);
        return adjWall.getState();
    }

    public Entity genericFindEntityByPosition(String type, int x, int y) {
        try {
            return this.genericGetAllEntitiesByType(type).stream()
                                                        .filter(o -> o.getY() == y && o.getX() == x)
                                                        .collect(Collectors.toList())
                                                        .get(0);
        } catch(Exception ex) {return null;}
    }
    
    /**
     * Find all the entity in the dungeon via the string of the type.
     * @param type The string of the type. This is strictly in camel case.
     * @return null if the string of the type is invalid
     */
    public List<Entity> genericGetAllEntitiesByType(String type) {
        List<Entity> res = new ArrayList<>();
        try {
            Class<?> cls = Class.forName("unsw.dungeon.Entities."+type);
            res = this.getEntities().stream()
                                    .filter(o -> cls.isInstance(o))
                                    .collect(Collectors.toList());
        } catch(Exception ex) {System.out.println(ex);ex.printStackTrace();}
        return res;
    }

    /**
     * Find all the entity in the dungeon via the list of string types.
     * @param types The strings of the type. This is strictly in camel case.
     * @return the list of entities of valid type strings.
     */
    public List<Entity> genericGetEntities(String... typeStrings) {
        List<Entity> res = new ArrayList<>();
        List<String> strings = Arrays.asList(typeStrings);
        for (String s:strings) {
            List<Entity> found = genericGetAllEntitiesByType(s);
            
            // Combine the found list to the current one
            res = Stream.concat(found.stream(), res.stream()).collect(Collectors.toList());
        }
        return res;
    }

    public Boulder findBoulderByPosition(int x, int y) {
        List<Boulder> found = this.getBoulders().stream()
                                          .filter(o -> o.getY() == y && o.getX() == x)
                                          .collect(Collectors.toList());

        if (!found.isEmpty()) return found.get(0);
        return null;
    }

    public Potion findPotionByPosition(int x, int y) {
        List<Potion> found = this.genericGetAllEntitiesByType("Potion").stream()
                                          .filter(o -> o.getY() == y && o.getX() == x)
                                          .map(o -> (Potion) o)
                                          .collect(Collectors.toList());

        if (!found.isEmpty()) return found.get(0);
        return null;
    }

    public Portal findPortalByPosition(int x, int y) {
        List<Portal> found = this.getPortals().stream()
                                          .filter(o -> o.getY() == y && o.getX() == x)
                                          .collect(Collectors.toList());

        if (!found.isEmpty()) return found.get(0);
        return null;
    }

 
    public Door findDoorByPosition(int x, int y) {
        List<Door> found = this.getDoors().stream()
                                          .filter(o -> o.getY() == y && o.getX() == x)
                                          .collect(Collectors.toList());

        if (!found.isEmpty()) {
            return found.get(0);
        }
        return null;
    }

    /**
     * Check if the COs should be a Wall.
     * If so, add the Wall there.
     * A cell should be a Wall if it AND its neighbours only
     * have 2 adjacent neighours diagonally and (horizontally or vertically)
     * @param x
     * @param y
     */
    public void addWallSuitably(int x, int y) {
        if (x==1||x==2||y==1||y==2) return;
        Wall wall = new Wall(x, y);
        this.addEntity((Entity)wall);
        if (!this.canBeWalled(x, y))
            this.removeEntity((Entity) wall);
    }


    private boolean canBeWalled(int x, int y) {
        int cDiag = 0;
        int cSide = 0;
        for (int i = x-1; i < x+1; i++) {            
            for (int j = y-1; j < y+1; j++) {
                // Ignore itself
                if (i==x&&j==y) continue;
                
                // Check if the vertical and horizontal side are Wall
                if (i==x||j==y) {
                    Entity w = this.genericFindEntityByPosition("Wall", i, j);
                    if (w!=null) cSide+=1;
                    continue;
                } 
                Entity w = this.genericFindEntityByPosition("Wall", i, j);
                if (w!=null) cDiag+=1;
            }
        }
        return ((cDiag<=2) && (cSide<=2));
    }


    private void addPlayerToEnemies() {
        List<NPC> entities = this.genericGetEntities("Enemy", "Hound").stream()
                                 .map(o -> (NPC) o)
                                 .collect(Collectors.toList());
        for (NPC e: entities) {
            e.addPlayer(this.player);                
        }
    }

    public Boolean anyEntityInPosition(int x, int y) {
        List<Entity> found = this.entities.stream()
                            .filter(o -> o.getX()==x && o.getY()==y)
                            .collect(Collectors.toList());

        return !found.isEmpty();
    }
   
    @Override
    public void attach(PlayerProgressObserver o) {
        if (!listObservers.contains(o))
            this.listObservers.add(o);
    }
    
    @Override
    public void attach(CurrentLevelObserver o) {
        if (!listObservers.contains(o))
            this.listObservers.add(o);
    }

    public void detachAllObs() {
        this.listObservers = new ArrayList<>();
    }


    
    @Override
    public void notifyPlayerMoved(Player p, Dungeon d) {
        for (DungeonObserver obs: this.listObservers) {
            obs.updatePlayerMoved(p, d);
        }
    }
    
    @Override
    public void notifyEnemyMoved(Enemy e, Dungeon d) {
        for (DungeonObserver obs: this.listObservers) {
            obs.updateEnemyMoved(e, d);
        }
    }

    @Override
    public void notifyBoulderMoved(Dungeon d) {
        for (DungeonObserver obs: this.listObservers) {
            obs.updateBoulderMoved(d);
        }
    }

    @Override
    public void notifyUsedDrill() {
        for (DungeonObserver obs: this.listObservers) 
            obs.updateDrillUsed();
    }

    @Override
    public void notifyPotionProduced(Potion p, Dungeon d) {
        for (DungeonObserver obs: this.listObservers) 
            obs.updatePotionProduced(p, this);
    }



    public void cancelGnomeTimerThreads() {
        try {
            Gnome gnome = (Gnome) this.genericGetAllEntitiesByType("Gnome").stream()
                                .map(o -> (Gnome) o)
                                .collect(Collectors.toList()).get(0);
            gnome.cancelTimer();
        } catch(Exception ex) {}
    }
}