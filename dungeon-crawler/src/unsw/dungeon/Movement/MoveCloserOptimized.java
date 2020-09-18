package unsw.dungeon.Movement; 

import unsw.dungeon.*;
import unsw.dungeon.Goal.*;
import unsw.dungeon.Entities.*;
import unsw.dungeon.Movement.*;

import java.util.LinkedList;


public class MoveCloserOptimized implements MoveStrategy {

    private Enemy enemy;
    private Dungeon dungeon;

    public MoveCloserOptimized(NPC c, Dungeon d) {
        this.enemy = (Enemy) c;
        this.dungeon = d;
    }

    private class Cell {
		final int x;
        final int y;
        
	    Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

    @Override
    public void execute() {
		// printDebug("called move.execute()");
        Player player = this.dungeon.getPlayer();
        Cell[][] visited = new Cell[dungeon.getHeight()][dungeon.getWidth()];
        boolean found = false;
        LinkedList<Cell> queue = new LinkedList<Cell>();
        
        Cell enemyCell = new Cell(this.enemy.getX(), this.enemy.getY());
		visited[this.enemy.getX()][this.enemy.getY()] = enemyCell;
		queue.add(enemyCell);
        Cell curr = enemyCell;
        
        while (queue.size() != 0 && !found) {
			curr = queue.poll();
			// printDebug("   >> dequeue currcell("+curr.x+", "+curr.y+")");
			LinkedList<Cell> nearbyCell = new LinkedList<Cell>();

            // get adjacent nodes
			if (curr.x > 0)
				nearbyCell.add(new Cell(curr.x - 1, curr.y));
			if (curr.x < dungeon.getHeight() - 1)
				nearbyCell.add(new Cell(curr.x + 1, curr.y));
			if (curr.y > 0)
				nearbyCell.add(new Cell(curr.x, curr.y - 1));
			if (curr.y < dungeon.getWidth() - 1)
				nearbyCell.add(new Cell(curr.x, curr.y + 1));

			for (Cell cell : nearbyCell) {
				// printDebug("     >> nearbyCell("+cell.x+", "+cell.y+")");
				if (cell.x == player.getX() && cell.y == player.getY()) {
					found = true;
					visited[cell.x][cell.y] = curr;
					curr = cell;
					break;
				} else if (visited[cell.x][cell.y] == null && this.enemy.tryWalk(cell.x, cell.y, "")) {
					visited[cell.x][cell.y] = curr;
					queue.add(cell);
				}
			}
        }
        // retrieve path
		while (visited[curr.x][curr.y] != enemyCell) {
            curr = visited[curr.x][curr.y];  
		}

		// Debugging
		printDebug("finalCurr = ("+curr.x+", "+curr.y+"), queue now has");
		for (Cell e: queue) {
			// printDebug("  > cell("+e.x+", "+e.y+")");
		}

        // if (curr.x > enemy.getX()) {
        //     this.enemy.moveRight();
        // } else if (curr.x < enemy.getX()) {
        //     this.enemy.moveLeft();
		// } 
		// if (curr.y > enemy.getY()) {
        //     this.enemy.moveDown();
        // } else {
        //     this.enemy.moveUp();
        // }
		this.enemy.moveTo(curr.x, curr.y);
	}
	

	private void printDebug(String s) {
		System.out.println(s);
	}
}



