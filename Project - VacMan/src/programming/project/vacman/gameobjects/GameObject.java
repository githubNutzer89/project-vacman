package programming.project.vacman.gameobjects;

import programming.project.vacman.util.Vector2D;

/* File: GameObject.java
 * -----------------------
 * An abstract class which takes care of position and collision calculation for all {@code GameObject}s.
 */
public abstract class GameObject {
	/*
	 * The 4 directions a GameObject can have.
	 */
	public enum Direction{
		UP (0), 
		LEFT (1), 
		DOWN (2), 
		RIGHT (3);
	
		private int direction;
		
		/*
		 * Initializes {@code direction}.
		 */
		private Direction(int direction) {
			this.direction = direction;
		}
		
		/*
		 * Returns the opposite direction of the direction set.
		 * 
		 *  @return Returns the opposite direction of the direction set or null.
		 */
		public Direction getOppositeDirection() {
			switch(direction) {
				case 0:
					return DOWN;
				case 1:
					return RIGHT;
				case 2:
					return UP;
				case 3:
					return LEFT;
				default:
					return null;
			}
		}
	}
	
	/*
	 * 4 possibilities of collision.
	 */
	public enum CollisionEdge{
		LEFT, TOP, RIGHT, BOTTOM
	}
	
	protected Vector2D pos;
	
	protected float speed;
	
	public static final int DIMENSION_X = 1;
	public static final int DIMENSION_Y = 1;
	
	protected Direction direction;
	
	/**
	 * Initializes a position and speed with 0.
	 * The default {code direction} is {@code UP}. 
	 * 
	 */
	public GameObject() {
		pos = new Vector2D(0, 0);
		speed = 0;
		direction = Direction.UP;
	}
	
	/*
	 * Sets a new position of the {@code GameObject}.
	 * 
	 * @param x The new x-coordinate.
	 * @param y The new y-coordinate.
	 */
	public void setPos(int x, int y) {		
		pos.setPos(new Vector2D(x, y));
	}
	
	/*
	 * Returns the position of the {@code GameObject} as {@code Vector2D}.
	 * 
	 * @return Returns the position of the {@code GameObject} as {@code Vector2D}.
	 */
	public Vector2D getPos() {
		return pos;
	}
	
	/*
	 * Returns the x-coordinate of the {@code GameObject}.
	 * 
	 * @return Returns the x-coordinate of the {@code GameObject}.
	 */
	public float getPosX() {
		return pos.getX();
	}
	
	/*
	 * Returns the y coordinate of the {@code GameObject}.
	 * 
	 * @return Returns the y coordinate of the {@code GameObject}.
	 */
	public float getPosY() {
		return pos.getY();
	}
	
	/*
	 * Returns {@code direction}.
	 * 
	 * @return Returns {@code direction}.
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/*
	 * Changes {@code direction}.
	 * 
	 * @param direction The new direction.
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/*
	 * The actual movement of the {@code GameObject} happens here. Depending on {@code Direction} and {@code speed} the position 
	 * update will be calculated. 
	 * 
	 * @param deltaTime The time between two frames.
	 */
	public void update(float deltaTime) {
		//TODO Ghosts are moving tile based, VacMan doesn't have to. Think about how GameObjects move and how we can make them move the same way instead of using different ideas for the ghosts and VacMan.
		//TODO Consider rearranging the GameObject class.
		
		switch(direction) {
			case UP:
				pos.setY(pos.getY() - speed * deltaTime);
				break;
			case DOWN:
				pos.setY(pos.getY() + speed * deltaTime);
				break;
			case LEFT:
				pos.setX(pos.getX() - speed * deltaTime);
				break;
			case RIGHT:
				pos.setX(pos.getX() + speed * deltaTime);
				break;
		}
	}
	
	/*
	 * Changes {@code direction} to UP.
	 */
	public void moveUp() {
		direction = Direction.UP;
	}
	
	/*
	 * Changes {@code direction} to DOWN.
	 */
	public void moveDown() {
		direction = Direction.DOWN;
	}
	
	/*
	 * Changes {@code direction} to LEFT.
	 */
	public void moveLeft() {
		direction = Direction.LEFT;
	}
	
	/*
	 * Changes {@code direction} to RIGHT.
	 */
	public void moveRight() {
		direction = Direction.RIGHT;
	}
	
	/*
	 * Set speed of the {@code GameObject}.
	 * 
	 * @param speed Speed of the {@code GameObject}.
	 */
	public void setVelocity(float speed) {
		this.speed = speed;
	}
	
	/*
	 * Called in case of a collision.
	 * 
	 * @param obj The {@code GameObject} with which this one collided.
	 * @param edge The edge on which both objects collided from the perspective of this {@code GameObject}.
	 */
	public abstract void onCollision(GameObject obj, CollisionEdge edge);
	
	/*
	 * Checks if a collision between this and another {@code GameObject} happened.
	 * 
	 * @param obj The {@code GameObject} of interest for collision detection. 
	 * @return Returns true if a collision happened otherwise false. 
	 */
	public boolean isCollided(GameObject obj) {
		//TODO Consider using gameStaticObjects[i][j] in World for collision detection
		
		double grad;
		boolean isSteep;
		boolean isLeft;
		boolean isTop;
		
		//Collision? --> Are the objects overlapping?
		if(obj.getPosX() + DIMENSION_X > pos.getX() && obj.getPosX() < pos.getX() + DIMENSION_X) {
			if(obj.getPosY() + DIMENSION_Y > pos.getY() && obj.getPosY() < pos.getY() + DIMENSION_Y) {
				//Which edge was colliding? Therefore:
				//1. Calculate gradient of the imaginary line connecting the two positions (avoid zero division)
				if(pos.getX() - obj.getPosX() != 0) {
					grad = Math.abs(pos.getY() - obj.getPosY()) / Math.abs(pos.getX() - obj.getPosX());
				}else {
					//If the positions are right above each other grad = -1 indicating this special case
					grad = -1;
				}
				
				//2. Is the gradient steep meaning greater 1 (greater 45°)? Then the collision happened at the bottom or top
				//   This includes -1 since it is infinite steep
				if(grad > 1 || grad == -1) {
					isSteep = true;
				}else {
					isSteep = false;
				}
				
				//3. Is this object left in relation to the other object?
				if(pos.getX() < obj.getPosX()) {
					isLeft = true;
				}else {
					isLeft = false;
				}
				
				//4. Is this object on top in relation to the other object?
				if(pos.getY() < obj.getPosY()) {
					isTop = true;
				}else {
					isTop = false;
				}
				
				//5. isSteep == true and isTop == true --> Collision at the bottom
				//   isSteep == true and isTop == false --> Collision at the top
				//   isSteep == false and isLeft == true --> Collision at the right
				//   isSteep == false and isLeft == false --> Collision at the left
				if(isSteep == true && isTop == true) {
					//Bottom
					onCollision(obj, CollisionEdge.BOTTOM);
					return true;
				}else if(isSteep == true && isTop == false) {
					//Top
					onCollision(obj, CollisionEdge.TOP);
					return true;
				}else if(isSteep == false && isLeft == true) {
					//Right
					onCollision(obj, CollisionEdge.RIGHT);
					return true;
				}else {
					//Left
					onCollision(obj, CollisionEdge.LEFT);
					return true;
				}
			}
		}
		return false;
	}
}
