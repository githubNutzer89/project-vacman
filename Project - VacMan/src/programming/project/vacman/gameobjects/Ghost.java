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
	private static final float VELOCITY = 4;
	
	private Vector2D currentTile;
	private Vector2D destinationTile;
	
	private Vector2D target;
	private Mode mode;
	private Type type;
	
	/*
	 * The four different modes a {@code Ghost} can have.
	 */
	public enum Mode{
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
		target = new Vector2D(0, 0);											//Set position to (0, 0)
		setVelocity(VELOCITY);													//Set velocity to the default value
		mode = Mode.SCATTER;													//Default mode is SCATTER
		type = Type.BLINKY;														//Default type is BLINKY
		destinationTile = new Vector2D((int)pos.getX(), (int)pos.getY());		//The destinationTile is set to (0, 0) 
		currentTile = new Vector2D((int)pos.getX(), (int)pos.getY());			//The currentTile is set to (0, 0)
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
	}
	
	/*
	 * Sets the {@code destinationTile} to the next neighbor tile according to the set {@code direction}.
	 * 
	 */
	public void moveOneTile() {
		if(!isOnTheMove()) {
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
		this.mode = mode;
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
		}
	}
}
