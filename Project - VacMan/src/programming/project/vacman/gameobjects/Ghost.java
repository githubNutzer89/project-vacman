package programming.project.vacman.gameobjects;

import programming.project.vacman.gameobjects.WallPart.Status;
import programming.project.vacman.util.Vector2D;

/* File: Ghost.java
 * -----------------------
 * This class represents a Ghost. There are four different types of {@code Ghost}s which can be in four different modes 
 * leading to different behavior.
 * 
 * The movement of a {@code Ghost} is tile based, i.e. a {@code Ghost} moves always from one tile to a neighbor tile
 * and stops. The {@code Ghost} can receive new commands to move to the next tile whenever it is not moving. Since a {@code Ghost}
 * is not supposed to stop it gets a new command just right after it has stopped. For the player it will appear that the ghost is
 * permanently moving since it stops only for one frame.
 */
public class Ghost extends GameObject{
	private static final float VELOCITY = 3;
	private static final float EATEN_VELOCITY = 7;
	
	private Vector2D currentTile;
	private Vector2D destinationTile;
	
	private Vector2D target;
	private Mode mode;
	private Type type;
	
	private float timer = 0;
	
	public static final Vector2D IN_FRONT_OF_HOUSE = new Vector2D(13, 1);
	public static final Vector2D IN_HOUSE = new Vector2D(13, 3);
	
	public static final Vector2D SCATTER_TGT_BLINKY = new Vector2D(28, 0);
	public static final Vector2D SCATTER_TGT_PINKY = new Vector2D(0, 0);
	public static final Vector2D SCATTER_TGT_INKY = new Vector2D(28, 14);
	public static final Vector2D SCATTER_TGT_CLYDE = new Vector2D(0, 14);
	
	/*
	 * The four different modes a {@code Ghost} can have.
	 */
	public enum Mode{
		WAITING,
		MOVE_OUT,
		MOVE_IN,
		SCATTER,
		CHASE,
		FRIGHTEND,
		EATEN
	}
	
	/*
	 * The four different types of {@code Ghost}s. The behavior of each type is not implemented in the {@code Ghost} class.
	 * It's implemented in the world class.
	 */
	public enum Type{
		BLINKY,
		PINKY,
		INKY,
		CLYDE
	}
	
	/**
	 * Initializes a newly created {@code Ghost} object. 
	 * 
	 */
	public Ghost() {			
		setVelocity(VELOCITY);													//Set velocity to the default value
		mode = Mode.WAITING;													//Default mode is SCATTER
		type = Type.BLINKY;														//Default type is BLINKY
		destinationTile = new Vector2D((int)pos.getX(), (int)pos.getY());		//The destinationTile is set to the current position 
		currentTile = new Vector2D((int)pos.getX(), (int)pos.getY());			//The currentTile is set to the current position
		target = new Vector2D((int)pos.getX(), (int)pos.getY());				//The target is set to the current position
	}
	
	/*
	 * Sets a new position of the {@code Ghost} and updates currentTile and destinationTile to the new position.
	 * 
	 * @param x The new x-coordinate.
	 * @param y The new y-coordinate.
	 */
	@Override
	public void setPos(int x, int y) {
		super.setPos(x, y);
		currentTile = new Vector2D((int)pos.getX(), (int)pos.getY());
		destinationTile = new Vector2D((int)pos.getX(), (int)pos.getY());
		target = new Vector2D((int)pos.getX(), (int)pos.getY());				//The target is set to the current position
	}
	
	/*
	 * Sets a new direction of the {@code Ghost} (only when it's not moving).
	 * 
	 * @param dir The new direction.
	 */
	@Override
	public void setDirection(Direction dir) {
		if(!isOnTheMove()) {
			super.direction = dir;
		}
	}
	
	/*
	 * Updates the position of the {@code Ghost}.
	 * 
	 * @param deltaTime The time between two frames.
	 */
	@Override
	public void update(float deltaTime) {
		if(mode == Mode.EATEN) {
			setVelocity(EATEN_VELOCITY);
		}else {
			setVelocity(VELOCITY);
		}
		
		//Using max(), min() to cover under-/overshoots
		if(isOnTheMove()) {
			switch(direction) {
				case UP:
					pos.setY(Math.max(pos.getY() - speed * deltaTime, destinationTile.getY()));
					break;
				case DOWN:
					pos.setY(Math.min(pos.getY() + speed * deltaTime, destinationTile.getY()));
					break;
				case LEFT:
					pos.setX(Math.max(pos.getX() - speed * deltaTime, destinationTile.getX()));
					break;
				case RIGHT:
					pos.setX(Math.min(pos.getX() + speed * deltaTime, destinationTile.getX()));
					break;
			}
		}
		
		if(mode == Mode.FRIGHTEND) {
			timer += deltaTime;
		}
	}
	
	/*
	 * Sets the {@code destinationTile} to the next neighbor tile according to the set {@code direction}.
	 * 
	 */
	public void moveOneTile() {
		if(!isOnTheMove() && !target.isEqual(currentTile)) {
			switch(direction) {
				case UP:
					destinationTile.setPos(new Vector2D(currentTile.getX(), currentTile.getY() - 1));
					break;
				case DOWN:
					destinationTile.setPos(new Vector2D(currentTile.getX(), currentTile.getY() + 1));
					break;
				case LEFT:
					destinationTile.setPos(new Vector2D(currentTile.getX() - 1, currentTile.getY()));
					break;
				case RIGHT:
					destinationTile.setPos(new Vector2D(currentTile.getX() + 1, currentTile.getY()));
					break;
			}
		}
	}
	
	/*
	 * Returns the {@code currentTile} of the {@code Ghost}.
	 * 
	 * @return Returns the {@code currentTile} of the {@code Ghost}. 
	 * 
	 */
	public Vector2D getCurrentTile() {
		return currentTile;
	}
	
	/*
	 * Sets a new {@code target} where for the {@code Ghost}.
	 * 
	 * Note: This doesn't affect the position update since the {@code World} class calculates the route. It only saves the
	 * target coordinate here. 
	 * 
	 * @param tgt The coordinates of the target. 
	 * 
	 */
	public void setTarget(Vector2D tgt) {
		this.target = tgt;
	}
	
	/*
	 * Returns the coordinate of the {@code target}.
	 * 
	 * @return Returns the coordinate of the {@code target}.
	 */
	public Vector2D getTarget() {
		return target;
	}
	
	/*
	 * Sets the {@code Mode} of the ghost.
	 * 
	 * @param mode Determines which {@code Mode} the {@code Ghost} is in.
	 */
	public void setMode(Mode mode) {
		//It shouldn't be possible to set mode WAITING from outside
		if(mode != Mode.WAITING) {
			this.mode = mode;
		}
		
		if(mode == Mode.MOVE_OUT || mode == Mode.EATEN) {
			setTarget(IN_FRONT_OF_HOUSE);
		}
		
		if(mode == Mode.SCATTER) {
			switch(type) {
				case BLINKY:
					setTarget(SCATTER_TGT_BLINKY);
					break;
				case PINKY:
					setTarget(SCATTER_TGT_PINKY);
					break;
				case INKY:
					setTarget(SCATTER_TGT_INKY);
					break;
				case CLYDE:
					setTarget(SCATTER_TGT_CLYDE);
					break;
				}
		}
		
		if(mode == Mode.MOVE_IN) {
			setTarget(IN_HOUSE);
		}
	}
	
	/*
	 * Returns the {@code Mode} of the ghost.
	 * 
	 * @return Returns the {@code Mode} of the ghost.
	 */
	public Mode getMode() {
		return mode;
	}
	
	/*
	 * Returns true if the ghost was frightened for less than 8s.
	 * 
	 * @return Returns true if the ghost was frightened for less than 8s.
	 */
	public boolean isStillFrightened() {
		if(mode == Mode.FRIGHTEND) {
			if(timer < 8) {
				return true;
			}else {
				timer = 0;
				return false;
			}
		}
		
		return false;
	}
	
	/*
	 * Sets the {@code Type} of the ghost.
	 * 
	 * @param type Determines which {@code Type} the {@code Ghost} has.
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/*
	 * Returns the {@code Type} of the ghost.
	 * 
	 * @return Returns the {@code Type} of the {@code Ghost}.
	 */
	public Type getType() {
		return type;
	}
	
	public void turnAround() {
		direction = direction.getOppositeDirection();
	}
	
	/*
	 * Checks if the {@code Ghost} is moving.
	 * 
	 * @return {@code True} if the {@code Ghost} is moving otherwise {@code false}.
	 */
	public boolean isOnTheMove() {
		if(!(pos.getX() == destinationTile.getX() && pos.getY() == destinationTile.getY())) {
			return true;
		}else {
			currentTile = new Vector2D(destinationTile.getX(), destinationTile.getY());
			return false;
		} 
	}
	
	/*
	 * Checks if the {@code Ghost} is at the target position.
	 * 
	 * @return {@code True} if the {@code Ghost} is at the target position otherwise {@code false}.
	 */
	public boolean isAtTarget() {
		if(target.isEqual(pos)) {
			return true;
		}else {
			return false;
		} 
	}
	
	/*
	 * Resets the {@code Ghost} into the ghost house.
	 */
	public void resetGhost() {
		setPos((int) getSpawnPos().getX(), (int) getSpawnPos().getY());
		mode = Mode.WAITING;
	}
	
	/*
	 * Checks collision with a {@code WallPart}.
	 * 
	 * @param obj The {@code WallPart} the {@code Ghost} has collided with.
	 * @param edge The edge on which the collision happened.
	 */
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		if(obj instanceof WallPart) {
			if(((WallPart)obj).getStatus() == Status.NORMAL) {
				switch(edge) {
					case LEFT:
						pos.setX(obj.getPosX() + DIMENSION_X);
						break;
					case TOP:
						pos.setY(obj.getPosY() + DIMENSION_Y);
						break;
					case RIGHT:
						pos.setX(obj.getPosX() - DIMENSION_X);
						break;
					case BOTTOM:
						pos.setY(obj.getPosY() - DIMENSION_Y);
						break;
				}
			}
		}else if(obj instanceof VacMan){
			if(mode == Mode.FRIGHTEND) {
				turnAround();
				setMode(Mode.EATEN);
			}
		}
	}
}
