package programming.project.vacman.gameobjects;

/* File: Cookie.java
 * -----------------------
 * This class represents a cookie {@code GameObject}. The cookies can be collected by {@code VacMan}.
 * 
 * Therefore it has two states: collected or not collected.
 * Since only {@code VacMan} eats cookies collisions are only checked between cookies and {@code VacMan}.
 */
public class Cookie extends GameObject{
	private boolean isCollected;
	private boolean isInjection;
	
	/**
	 * Initializes a newly created {@code Cookie} object. Sets {@code collected} to {@code false} as default. 
	 * 
	 */
	public Cookie() {
		isCollected = false;
		isInjection = false;
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
	 * Returns true if this {@code Cookie} is an injection.
	 * 
	 * @return Returns true if this {@code Cookie} is an injection.
	 */
	public boolean isInjection() {
		return isInjection;
	}
	
	/*
	 * Turns the cookie into an injection or vice versa.
	 * 
	 * @param injected True if the cookie should turn into an injection.
	 */
	public void setInjection(boolean injected) {
		this.isInjection = injected;
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
