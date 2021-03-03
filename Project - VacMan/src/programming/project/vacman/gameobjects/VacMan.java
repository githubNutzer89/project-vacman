package programming.project.vacman.gameobjects;

/* File: VacMan.java
 * -----------------------
 * A singleton class which represents VacMan. Takes care of collision handling between {@code VacMan} and {@code WallPart}s.
 */
public class VacMan extends GameObject{
	//Instance of VacMan
	private static VacMan instance;
		
	//Default speed
	private static final float VELOCITY = 5;
	
	// Initialize lives of VacMan
	private int lives = 3;
	
	//Don't allow to create objects
	private VacMan () {}
	
	/*
	 * Provide an instance of {@code VacMan}. If null create one.
	 * 
	 * @return An instance of {@code VacMan}
	 */
	public static VacMan getInstance () {
		if (VacMan.instance == null) {
			VacMan.instance = new VacMan ();
			instance.setDirection(Direction.LEFT);
			instance.setVelocity(VELOCITY);
	    }
		
		
	    return VacMan.instance;
	}

	/*
	 * Repos {@code VacMan} if a collision with a {@code WallPart} or {@code Ghost} happened.
	 * 
	 * @param obj The {@code GameObject} with which {@code VacMan} collided.
	 * @param edge The edge on which both objects collided from the perspective of {@code VacMan}.
	 */
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		if(obj instanceof WallPart) {
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
		} else if(obj instanceof Ghost) {		// Loses a live if collision with ghost.
			this.lives --;
		}
	}
	
	
	/**
	 * Returns the lives of {@code VacMan}
	 * @return Returns the lives.
	 */
	public int getLives() {
		return lives;
	}
	
	public void resetLives() {
		lives = 3;
	}
}
