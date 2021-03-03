package programming.project.vacman;

import java.util.ArrayList;
import java.util.Random;

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
	
	//The score for collected coins
	public int score;
	
	//Time the ghosts spend in specific mode. 
	public float modeTime;
	
	// Maximum time spent in a certain mode.
	public static final float MAX_MODE_TIME = 20.0f;
	
	// Chase mode ?
	public boolean chase = false;
	
	// TODO Maybe let the ghosts change mode randomly?
	//Random mode generator.
	Random rnd = new Random();
	
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
				
				//Coin
				if(BlockType.COIN.sameColor(rgb)) {
					Cookie coin = new Cookie();
					coin.setPos(i, j);
					cookies.add(coin);
					gameStaticObjects[i][j] = coin;
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
		
		// Initialize ghosts and their respective mode.
		initGhostMode();
		

	}
	
	/*
	 * Updates each {@code GameObject} according to the {@code deltaTime} (Time between two frames).
	 * Mainly position calculates and collision detection takes place here.
	 * 
	 * @param deltaTime The time between two frames.
	 */
	public void update(float deltaTime) {		
		if(isGameOver == false) {
			//TODO Use gameStaticObjects[i][j] for collision detection instead of iterating through all GameObjects
			vacMan.update(deltaTime);
			
			// Initialize score.
			score = 0;
			
			// Count modeTime to trigger switching between ghosts mode.
			modeTime += deltaTime;
			
			//Collision Detection for all coins with VacMan
			for(Cookie cookie : cookies) {
				cookie.isCollided(vacMan);
				
				//Counts the collected cookies
				if(cookie.isCollected()) {
					score++;
				}
			}
			
			//Moving the ghosts
			for(Ghost ghost : ghosts) {
				
				// Check whether {@code VacMan} collides with {@code Ghost}
				if (vacMan.isCollided(ghost)) {
					if (vacMan.getLives() > 0) { // If VacMan is still alive, respawn
						resetVacMan();
						resetGhosts();
					}else {
						isGameOver = true;
					}
				}
				
				// If ghost is on or below spawn point, move up to escape spawn area. 
				if((ghost.getPosX() == ghost.getSpawnPos().getX() && ghost.getPosY() == ghost.getSpawnPos().getY()) || (ghost.getPosX() == ghost.getSpawnPos().getX() && ghost.getPosY() == ghost.getSpawnPos().getY()-1)) {
					ghost.setDirection(Direction.UP);
					ghost.moveOneTile();
				} else if (!ghost.isOnTheMove()) {		// else move in new possible direction.
					ghost.setDirection(getNewDirection(ghost));
					ghost.moveOneTile();
				}
				
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
			
			// If in chase mode, reset chase coordinates.
			if(chase) {
				resetModeGhosts(chase);
			}
			
			//Reset Ghost modes after approximately maximum mode time.
			if (modeTime>MAX_MODE_TIME) {
				
				// Reset ghost mode and timer.
				modeTime = 0.0f;
				// Toggle chase mode.
				chase = !chase;
				
				resetModeGhosts(chase);
			}
		}
	}
	
	/*
	 * This method resets VacMan to its starting position, i.e. when VacMan dies.
	 */
	private void resetVacMan() {
		vacMan.setPos((int) vacMan.getSpawnPos().getX(), (int) vacMan.getSpawnPos().getY());
	}

	/**
	 * This method initializes the ghosts to standard scatter mode with respective target vector
	 * and sets the overall mode to not chase.
	 */
	private void initGhostMode() {
		//Set the timer to zero and the overall mode to not chase.
		modeTime = 0.0f;
		chase = false;
		
		//Initialize the type and the target of each ghost
		//(target is the position where the ghost is supposed to go to)
		blinky = ghosts.get(0);
		blinky.setType(Type.BLINKY);
		blinky.setTarget(new Vector2D(0, 0));
		blinky.setMode(Mode.SCATTER);
		pinky = ghosts.get(1);
		pinky.setType(Type.PINKY);
		pinky.setMode(Mode.SCATTER);
		pinky.setTarget(new Vector2D(0, 14));
		inky = ghosts.get(2);
		inky.setType(Type.INKY);
		inky.setMode(Mode.SCATTER);
		inky.setTarget(new Vector2D(28, 0));
		clyde = ghosts.get(3);
		clyde.setType(Type.CLYDE);
		clyde.setMode(Mode.SCATTER);
		clyde.setTarget(new Vector2D(28, 14));
	}
	
	/*
	 * This method resets the ghosts to their starting positions and restarts the algorithm, i.e. when VacMan dies.
	 */
	private void resetGhosts() {
		// Starting position
		for(Ghost ghost : ghosts) {
			ghost.setPos((int) ghost.getSpawnPos().getX(), (int) ghost.getSpawnPos().getY());
		}
		
		// Reset to initial ghost mode.
		initGhostMode();
		
	}
	
	/**
	 * This method resets ghost mode from scatter to chase or vice versa.
	 * @param chase represents the mode for the ghosts. 
	 */
	private void resetModeGhosts(boolean chase) {
		// TODO Add special items to allow for other modes and use ghost mode as parameter instead of chase boolean.
		
		// Select random mode for each ghosts with specific target.
		// If chase mode, set target coordinates:
		if(chase) {
			// Direct target
			blinky.setMode(Mode.CHASE);
			blinky.setTarget(new Vector2D(vacMan.getPosX(), vacMan.getPosY()));
			// From the left
			pinky.setMode(Mode.CHASE);
			pinky.setTarget(new Vector2D(vacMan.getPosX() - 2, vacMan.getPosY()));
			// From the right
			inky.setMode(Mode.CHASE);
			inky.setTarget(new Vector2D(vacMan.getPosX() + 2, vacMan.getPosY()));
			// Stupid and follows halfway blinky and vacman.
			clyde.setMode(Mode.CHASE);
			clyde.setTarget(new Vector2D((int) ((vacMan.getPosX() + blinky.getPosX())/2), (int) ((vacMan.getPosY() + blinky.getPosY())/2)));
		} else { // else initial ghost mode, which is scatter
			initGhostMode();

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
