package programming.project.vacman;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class World {
	public static final int WORLD_WIDTH = 28;
	public static final int WORLD_HEIGHT = 14;
	
	public int points;
	
	public VacMan vacMan;
	public ArrayList<WallPart> wallParts;
	public ArrayList<Coin> coins;
	public ArrayList<Ghost> ghosts;
	
	public World() {
		wallParts = new ArrayList<WallPart>();
		coins = new ArrayList<Coin>();
		ghosts = new ArrayList<Ghost>();
		//coin = new Coin();
		//ghost = new Ghost();
		
		init();
	}
	
	public void init() {
		int rgb = 0;
		
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
				
				//Ghosts
				if(BLOCK_TYPE.GHOST_SPAWNPOINT.sameColor(rgb)) {
					Ghost ghost = new Ghost();
					ghost.setPos(i, j);
					ghosts.add(ghost);
				}
			}
		}
	}
	
	public void update(float deltaTime) {
		int blockedWays = 0;
		
		int posGhostX = 0;
		int posGhostY = 0;
		
		boolean corners[] = new boolean[4];
		boolean isCorner = false;
		
		for(Ghost ghost : ghosts) {
			posGhostX = (int ) ghost.getPosX();
			posGhostY = (int ) ghost.getPosY();
			
			for(WallPart wallPart : wallParts) {
				if(wallPart.getPosX() == posGhostX - 1 && wallPart.getPosY() == posGhostY){
					corners[0] = true;
					blockedWays++;
				}
				
				if(wallPart.getPosX() == posGhostX && wallPart.getPosY() == posGhostY - 1) {
					corners[1] = true;
					blockedWays++;
				}
				
				if(wallPart.getPosX() == posGhostX + 1 && wallPart.getPosY() == posGhostY) {
					corners[2] = true;
					blockedWays++;
				}
				
				if(wallPart.getPosX() == posGhostX && wallPart.getPosY() == posGhostY + 1) {
					corners[3] = true;
					blockedWays++;
				}					
			}
		
			boolean lastHit = false;
			
			//Corner?
			for(int i=0; i<4; i++) {
				if(lastHit == corners[i]) {
					isCorner = true;
				}
				lastHit = corners[i];
			}
			
			switch(blockedWays) {
				case 1:
				case 3:
					moveRandomly(ghost, deltaTime);
				case 2:
					if(!isCorner) {
						ghost.advance(deltaTime);
					}else {
						moveRandomly(ghost, deltaTime);
					}
					
					break;
			}
		}
		
		for(WallPart wallPart : wallParts) {
			vacMan.isCollidated(wallPart);
			
			for(Ghost ghost : ghosts) {
				ghost.isCollidated(wallPart);
			}
		}
		
		for(Coin coin : coins) {
			coin.isCollidated(vacMan);
			//vacMan.isCollidated(coin);
		}
	}
	
	private void moveRandomly(GameObject gameObject, float deltaTime) {
		int rndNumber = (new Random()).nextInt(4);
		
		switch(rndNumber) {
			case 0:
				gameObject.moveUp(deltaTime);
				break;
			case 1:
				gameObject.moveRight(deltaTime);
				break;
			case 2:
				gameObject.moveDown(deltaTime);
				break;
			case 3:
				gameObject.moveLeft(deltaTime);
				break;
		}
	}
}
