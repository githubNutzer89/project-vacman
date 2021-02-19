package programming.project.vacman;

import java.awt.Color;
import java.util.ArrayList;

public class World {
	public static final int WORLD_WIDTH = 28;
	public static final int WORLD_HEIGHT = 14;
	
	public VacMan vacMan;
	public Coin coin;
	//public Ghost ghost;
	public ArrayList<WallPart> wallParts;
	public ArrayList<Coin> coins;
	
	private enum BLOCK_TYPE {
		WALL(0, 0, 0), // black
		COIN(255, 255, 0), // yellow
		VACMAN(255, 0, 0), // red
		GHOST_SPAWNPOINT(0, 255, 0), // green
		ITEM_GOLD_COINGHOST_GATE(0, 0, 255); // blue

		private int color;

		private BLOCK_TYPE (int r, int g, int b) {
			color = 0xff << 24 | r << 16 | g << 8 | b << 0;
		}

		public boolean sameColor (int color) {
			return this.color == color;
		}

		public int getColor () {
			return color;
		}
	}
	
	public World() {
		wallParts = new ArrayList<WallPart>();
		coins = new ArrayList<Coin>();
		vacMan = VacMan.getInstance();
		//coin = new Coin();
		//ghost = new Ghost();
		
		init();
	}
	
	public void init() {
		int rgb = 0;
		
		vacMan.setPos(1, 1);
		//ghost.setPos(10, 10);
		
		for(int i=0; i<WORLD_WIDTH; i++) {
			for(int j=0; j<WORLD_HEIGHT; j++) {
				rgb = Assets.lvl1.getRGB(i, j);
				
				//Wall
				if(BLOCK_TYPE.WALL.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallParts.add(wallPart);
				}
				
				//Coin
				if(BLOCK_TYPE.COIN.sameColor(rgb)) {
					Coin coin = new Coin();
					coin.setPos(i, j);
					coins.add(coin);
				}
				
				//Vacman
				if(BLOCK_TYPE.VACMAN.sameColor(rgb)) {
					vacMan = VacMan.getInstance();
					vacMan.setPos(i, j);
				}
			}
		}
	}
	
	public void update(float deltaTime) {
		//ghost.advance(deltaTime);
		
		for(WallPart wallPart : wallParts) {
			vacMan.isCollidated(wallPart);
			//ghost.isCollidated(wallPart);
		}
		
		for(Coin coin : coins) {
			coin.isCollidated(vacMan);
			//vacMan.isCollidated(coin);
		}
	}
}
