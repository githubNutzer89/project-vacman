package programming.project.vacman;

import java.util.ArrayList;

public class World {
	public static final int WORLD_WIDTH = 28;
	public static final int WORLD_HEIGHT = 14;
	
	public VacMan vacMan;
	public Coin coin;
	public Ghost ghost;
	public ArrayList<WallPart> wallParts;
	
	public World() {
		vacMan = VacMan.getInstance();
		wallParts = new ArrayList<WallPart>();
		coin = new Coin();
		ghost = new Ghost();
		
		init();
	}
	
	public void init() {
		vacMan.setPos(1, 1);
		coin.setPos(10, 12);
		ghost.setPos(10, 10);
		
		for(int i=0; i<WORLD_WIDTH; i++) {
			for(int j=0; j<WORLD_HEIGHT; j++) {
				if(i==0 || j==0 || i==WORLD_WIDTH - 1 || j==WORLD_HEIGHT - 1) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallParts.add(wallPart);
				}
			}
		}
	}
	
	public void update(float deltaTime) {
		ghost.advance(deltaTime);
		
		for(WallPart wallPart : wallParts) {
			vacMan.isCollidated(wallPart);
			ghost.isCollidated(wallPart);
		}
		coin.isCollidated(vacMan);
	}
}
