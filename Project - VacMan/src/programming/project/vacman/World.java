package programming.project.vacman;

import java.util.ArrayList;
import programming.project.vacman.gameobjects.Cookie;
import programming.project.vacman.gameobjects.GameObject;
import programming.project.vacman.gameobjects.GameObject.Direction;
import programming.project.vacman.gameobjects.Ghost;
import programming.project.vacman.gameobjects.Ghost.Mode;
import programming.project.vacman.gameobjects.Ghost.Type;
import programming.project.vacman.gameobjects.VacMan;
import programming.project.vacman.gameobjects.WallPart;
import programming.project.vacman.gameobjects.WallPart.Status;
import programming.project.vacman.util.Vector2D;

/* File: World.java
 * -----------------------
 * This class is a single entry into the data model. All {@code GameObjects} are created and managed here.
 * Especially the creation of the levels and the interaction of the {@code GameObject}s takes places here.
 * All {@code GameObject}s use {@code World} coordinates. The render method later on converts them into
 * pixel coordinates. This technique is used since it is unknown which resolution the player's display has. 
 */
public class World{
	//The size of the world
	public static final int WORLD_WIDTH = 28;
	public static final int WORLD_HEIGHT = 14;
	
	//Timer
	float timer;
	
	//The score for collected coins
	public int score;
	
	//Is the game over?
	public boolean isGameOver;
	
	//The GameObjects
	public VacMan vacMan;
	public ArrayList<WallPart> wallParts;
	public ArrayList<Cookie> cookies;
	public ArrayList<Ghost> ghosts;

	//The Ghosts
	public Ghost blinky;
	public Ghost pinky;
	public Ghost inky;
	public Ghost clyde;
	
	//An array of all static GameObjects (i.e. walls)
	public GameObject[][] gameStaticObjects = new GameObject[28][14];
	
	/**
	 * Initializes a newly created World object. 
	 * 
	 */
	public World() {		
		init();
	}
	
	/**
	 * Builds the level according lvl.png by comparing each pixel with the preset values in {@code BlockType}.
	 * If there is a match the appropriate {@code GameObject} is created.
	 */
	private void init() {
		wallParts = new ArrayList<WallPart>();
		cookies = new ArrayList<Cookie>();
		ghosts = new ArrayList<Ghost>();
		
		//The RGB value
		int rgb = 0;
		
		//The game is not over
		isGameOver = false;
		
		//Set timer to 0
		timer = 0;
		
		//Go through each pixel
		for(int i=0; i<WORLD_WIDTH; i++) {
			for(int j=0; j<WORLD_HEIGHT; j++) {
				//The RGB values of the pixel
				rgb = Assets.lvl1.getRGB(i, j);
				
				//Wall
				if(BlockType.WALL.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallParts.add(wallPart);
					gameStaticObjects[i][j] = wallPart;
				}
				
				//Gate
				if(BlockType.GHOST_GATE.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallPart.setStatus(Status.GATE);
					wallParts.add(wallPart);
					//TODO Implement different collision behavior for different types of WallParts
					//gameStaticObjects[i][j] = wallPart; //Uncomment would make the ghosts considering the gate as wall so they don't move 
				}
				
				//Cookie
				if(BlockType.COOKIE.sameColor(rgb)) {
					Cookie cookie = new Cookie();
					cookie.setPos(i, j);
					cookies.add(cookie);
					gameStaticObjects[i][j] = cookie;
				}
				
				//Injection
				if(BlockType.INJECTION.sameColor(rgb)) {
					Cookie cookie = new Cookie();
					cookie.setPos(i, j);
					cookie.setInjection(true);
					cookies.add(cookie);
					gameStaticObjects[i][j] = cookie;
				}
				
				//Vacman
				if(BlockType.VACMAN.sameColor(rgb)) {
					vacMan = VacMan.getInstance();
					vacMan.resetLives();
					vacMan.setPos(i, j);
					vacMan.setSpawnPos(i, j);
				}
				
				//Ghosts
				if(BlockType.GHOST_SPAWNPOINT.sameColor(rgb)) {
					Ghost ghost = new Ghost();
					ghost.setPos(i, j);
					ghost.setSpawnPos(i, j);
					ghosts.add(ghost);
				}
			}
		}
		
		//Set the type and the target of each ghost
		//(target is the position where the ghost is supposed to go to)
		blinky = ghosts.get(0);
		blinky.setType(Type.BLINKY);
		blinky.setTarget(new Vector2D(0, 0));
		pinky = ghosts.get(1);
		pinky.setType(Type.PINKY);
		pinky.setTarget(new Vector2D(28, 0));
		inky = ghosts.get(2);
		inky.setType(Type.INKY);
		inky.setTarget(new Vector2D(28, 14));
		clyde = ghosts.get(3);
		clyde.setType(Type.CLYDE);
		clyde.setTarget(new Vector2D(0, 14));
	}
	
	/*
	 * Updates each {@code GameObject} according to the {@code deltaTime} (Time between two frames).
	 * Mainly position calculates and collision detection takes place here.
	 * 
	 * @param deltaTime The time between two frames.
	 */
	public void update(float deltaTime) {		
		if(isGameOver == false) {
			//Increase timer
			timer += deltaTime;
			
			//TODO Use gameStaticObjects[i][j] for collision detection instead of iterating through all GameObjects
			vacMan.update(deltaTime);
			
			score = 0;
			
			//Collision Detection for all coins with VacMan
			for(Cookie cookie : cookies) {
				cookie.isCollided(vacMan);
				
				//Counts the collected cookies
				if(cookie.isCollected()) {
					score++;
					
					//If there was an injection collected
					if(cookie.isInjection()) {
						setGhostsFrigthened();
						cookie.setInjection(false);
					}
				}
			}
			
			//Moving the ghosts
			for(Ghost ghost : ghosts) {
				//Chose algorithm for the ghosts (basically starting with SCATTER, after 7s switching to CHASE for 20s
				//and then back to SCATTER and so on), if not FREIGHTEND or EATEN
				if(ghost.getMode() != Mode.FRIGHTEND && ghost.getMode() != Mode.EATEN) {
					if(timer % 27 >= 0 && timer % 27 <= 7 && timer <= 88) {
						//Scatter mode
						ghost.setMode(Mode.SCATTER);
					}else {
						//Chase mode
						ghost.setMode(Mode.CHASE);
					}
				}
				
				// Check whether {@code VacMan} collides with {@code Ghost}
				if (vacMan.isCollided(ghost)) {
					if (vacMan.getLives() > 0) { // If VacMan is still alive, respawn
						timer = 0;
						resetVacMan();
						resetGhosts();
					}else {
						isGameOver = true;
					}
				}
				
				if(!ghost.isOnTheMove()) {
					ghost.setDirection(getNewDirection(ghost));
					ghost.moveOneTile();
				}
				
				//System.out.println(ghost.getType().name() + ": (" + ghost.getPosX() + ", " + ghost.getPosY() + ") " + ghost.getDirection().name());
				ghost.update(deltaTime);
			}
			
			//Collision Detection for all WallParts with VacMan and all Ghosts
			for(WallPart wallPart : wallParts) {
				vacMan.isCollided(wallPart);
				
				blinky.isCollided(wallPart);
				pinky.isCollided(wallPart);
				inky.isCollided(wallPart);
				clyde.isCollided(wallPart);
			}
		}
	}
	
	/*
	 * This method sets all ghosts to {@code FRIGHTENED} mode if they are not in {@code EATEN} mode.
	 */
	private void setGhostsFrigthened() {
		for(Ghost ghost : ghosts) {
			if(ghost.getMode() != Mode.EATEN) {
				ghost.setMode(Mode.FRIGHTEND);
			}
		}
	}

	/*
	 * This method resets VacMan to its starting position, i.e. when VacMan dies.
	 */
	private void resetVacMan() {
		vacMan.setPos((int) vacMan.getSpawnPos().getX(), (int) vacMan.getSpawnPos().getY());
	}

	/*
	 * This method resets the ghosts to their starting positions and restarts the algorithm, i.e. when VacMan dies.
	 */
	private void resetGhosts() {
		// TODO Reset ghosts algorithm.
		// TODO One ghost doesn't leave the house after VacMan died.
		for(Ghost ghost : ghosts) {
			ghost.setPos((int) ghost.getSpawnPos().getX(), (int) ghost.getSpawnPos().getY());
		}
	}
	
	/*
	 * This method resets the world to its starting values, e.g. when VacMan finally dies (Gameover).
	 */
	public void resetWorld() {
		init();
	}
	
	/*
	 * This method calculates the next moving direction for the ghost. It facilitates its moving algorithm.
	 * 
	 * @param ghost The ghost for which the new direction is calculated for.
	 * @return Returns the new direction the ghost should move to.
	 */
	private Direction getNewDirection(Ghost ghost) {
		ArrayList<Direction> list = new ArrayList<Direction>();
		Direction lastDirection = ghost.getDirection();
		Vector2D pos;
		
		//If there is no wall blocking a specific direction add it to the list
		if(!(gameStaticObjects[(int)ghost.getCurrentTile().getX()][(int)ghost.getCurrentTile().getY() - 1] instanceof WallPart)) {
			list.add(Direction.UP);
		}
		
		if(!(gameStaticObjects[(int)ghost.getCurrentTile().getX() - 1][(int)ghost.getCurrentTile().getY()] instanceof WallPart)) {		
			list.add(Direction.LEFT);
		}
		
		if(!(gameStaticObjects[(int)ghost.getCurrentTile().getX()][(int)ghost.getCurrentTile().getY() + 1] instanceof WallPart)) {
			list.add(Direction.DOWN);
		}
		
		if(!(gameStaticObjects[(int)ghost.getCurrentTile().getX() + 1][(int)ghost.getCurrentTile().getY()] instanceof WallPart)) {
			list.add(Direction.RIGHT);
		}
		
		//Remove the U-Turn option 
		if(list.contains(lastDirection.getOppositeDirection())) {
			list.remove(lastDirection.getOppositeDirection());
		}
		
		//If there is only one direction left return it
		if(list.size() == 1) {
			return list.get(0); 
		}
		
		//Otherwise determine the shortest direction for each option
		float length;
		float minLength = 1000;
		
		//Go through each direction and calculate the distance from the middle of each edge to the target position
		//Note: This for-loop is counting backwards due to the index renumbering after removing elements from the list
		for(int i=list.size()-1; i>=0; i--) {
			pos = new Vector2D(ghost.getPosX(), ghost.getPosY());
			
			switch(list.get(i)) {
				case UP:
					pos.add(0.5f, 0);
					break;
				case LEFT:
					pos.add(0, 0.5f);
					break;
				case DOWN:
					pos.add(0.5f, 1);
					break;
				case RIGHT:
					pos.add(1, 0.5f);
					break;
			}
			
			length = pos.getVectorTo(ghost.getTarget()).getLength();
						
			if(length > minLength) {
				//If the length is greater than the minLength remove it
				list.remove(i);
			}else {
				//...otherwise set the new minLength
				minLength = length;
			}
		}
		
		//If there is more than one direction having the same distance to the target the priority is as follows: 
		//UP, LEFT, DOWN, RIGHT
		//It is catered for this due to the fact that the directions were added to the list in exact this order.
		//So just return the first element
		return list.get(0);
	}
}
