package programming.project.vacman;

import java.util.ArrayList;
import java.util.Random;

public class Ghost extends GameObject{
	private static final float VELOCITY = 4;
	
	//private ArrayList<Integer> lastRandomDirection;
	private Random rnd;
	
	public Ghost() {
		rnd = new Random();
		setVelocity(VELOCITY);
		//lastRandomDirection = new ArrayList<Integer>();
	}
	
	public void advance(float deltaTime) {
		//lastRandomDirection.clear();
		
		switch(direction) {
			case LEFT:
				moveLeft(deltaTime);
				break;
			case UP:
				moveUp(deltaTime);
				break;
			case RIGHT:
				moveRight(deltaTime);
				break;
			case DOWN:
				moveDown(deltaTime);
				break;
		}
	}
	
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		if(obj instanceof WallPart) {
			switch(edge) {
				case LEFT:
					posX = obj.posX + DIMENSION_X;
					break;
				case TOP:
					posY = obj.posY + DIMENSION_Y;
					break;
				case RIGHT:
					posX = obj.posX - DIMENSION_X;
					break;
				case BOTTOM:
					posY = obj.posY - DIMENSION_Y;
					break;
			}
			
			//int randomDir = rnd.nextInt(4);
			
//			switch(rnd.nextInt(4)) {
//				case 0:
//					direction = Direction.LEFT;
//					break;
//				case 1:
//					direction = Direction.UP;
//					break;
//				case 2:
//					direction = Direction.RIGHT;
//					break;
//				case 3:
//					direction = Direction.DOWN;
//					break;
//			}
		}
	}
}
