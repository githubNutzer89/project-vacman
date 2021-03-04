package programming.project.vacman.gameobjects;

/* File: Cookie.java
 * -----------------------
 * This class represents a cookie {@code GameObject}. The cookies can be collected by {@code VacMan}.
 * 
 * Therefore it has two states: collected or not collected.
 * Since only {@code VacMan} eats cookies collisions are only checked between cookies and {@code VacMan}.
 */
public class Collectable extends GameObject{
	private boolean isCollected;
	
	/**
	 * Initializes a newly created {@code Cookie} object. Sets {@code collected} to {@code false} as default. 
	 * 
	 */
	public Collectable() {
		isCollected = false;
	}
	
	/*
	 * Returns the state of the {@code Cookie} (collected or not collected).
	 * 
	 * @return Returns the state of the {@code Cookie} (collected or not collected).
	 */
	public boolean isCollected() {
		return isCollected;
	}
	
	/*
	 * On collision with {@code VacMan} the {@code Cookie} changes the status from not collected to collected.
	 * 
	 * @param obj The {@code GameObject} the {@code Cookie} has collided with.
	 * @param edge The edge on which the collision happened.
	 */
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		if(obj instanceof VacMan) {
			isCollected = true;
		}
	}

}
