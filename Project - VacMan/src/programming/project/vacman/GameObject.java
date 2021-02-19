package programming.project.vacman;

import java.awt.Rectangle;

import programming.project.vacman.GameObject.Direction;

public abstract class GameObject {
	protected enum Direction{LEFT, UP, RIGHT, DOWN};
	
	protected float posX;
	protected float posY;
	
	protected float velocity;
	
	protected static final int DIMENSION_X = 1;
	protected static final int DIMENSION_Y = 1;
	
	protected Direction direction;
	
	protected enum CollisionEdge{
		LEFT, TOP, RIGHT, BOTTOM
	};
	
	public GameObject() {
		posX = 0;
		posY = 0;
		velocity = 0;
		direction = Direction.UP;
	}
	
	public void setPos(int x, int y) {
		posX = x;
		posY = y;
	}
	
	public float getPosX() {
		return posX;
	}
	
	public float getPosY() {
		return posY;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void moveUp(float deltaTime) {
		direction = Direction.UP;
		posY -= velocity * deltaTime;
	}
	
	public void moveDown(float deltaTime) {
		direction = Direction.DOWN;
		posY += velocity * deltaTime;
	}
	
	public void moveLeft(float deltaTime) {
		direction = Direction.LEFT;
		posX -= velocity * deltaTime;
	}
	
	public void moveRight(float deltaTime) {
		direction = Direction.RIGHT;
		posX += velocity * deltaTime;
	}
	
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	public abstract void onCollision(GameObject obj, CollisionEdge edge);
	
	public boolean isCollidated(GameObject obj) {
		double grad;
		boolean isSteep;
		boolean isLeft;
		boolean isTop;
		
		//Collision? --> Are the objects overlapping?
		if(obj.posX + DIMENSION_X > posX && obj.posX < posX + DIMENSION_X) {
			if(obj.posY + DIMENSION_Y > posY && obj.posY < posY + DIMENSION_Y) {
				//Which edge was colliding? Therefore:
				//1. Calculate gradient of the imaginary line connecting the two positions (avoid zero division)
				if(posX - obj.posX != 0) {
					grad = Math.abs(posY - obj.posY) / Math.abs(posX - obj.posX);
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
				if(posX < obj.posX) {
					isLeft = true;
				}else {
					isLeft = false;
				}
				
				//4. Is this object on top in relation to the other object?
				if(posY < obj.posY) {
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
