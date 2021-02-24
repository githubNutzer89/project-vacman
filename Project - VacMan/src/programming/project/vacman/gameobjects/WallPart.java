package programming.project.vacman.gameobjects;

/* File: WallPart.java
 * -----------------------
 * This class represents a WallPart {@code GameObject}.
 * 
 * It has three states: {@code NOMRAL}, {@code RED} and {@code GATE}. These states have an impact on collision behavior
 * and appearance. 
 */
public class WallPart extends GameObject{
	/*
	 * Determines the {@code Status} of the {@code WallPart}.
	 */
	private Status status;
	
	/*
	 * The three states a {@code WallPart} can be in: {@code NOMRAL}, {@code RED} and {@code GATE}. 
	 * These states have an impact on collision behavior and appearance. 
	 */
	public enum Status{
		NORMAL,
		RED,
		GATE
	}
	
	/**
	 * Initializes a newly created {@code WallPart} object. It has the {@code Status} {@code NORMAL} by default. 
	 * 
	 */
	public WallPart() {		
		status = Status.NORMAL;
	}
	
	/*
	 * Toggles between {@code Status} {@code RED} and {@code NORMAL}. 
	 */
	public void toogleRedNormal() {
		// If NORMAL then RED
		// If RED then NORMAL
		if(status == Status.NORMAL) {
			status = Status.RED;
		}else {
			status = Status.NORMAL;
		}
	}
	
	/*
	 * Sets the {@code status} of the {@code WallPart}
	 * 
	 * @param status The {@code Status} to be set.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/*
	 * Returns the {@code status} of the {@code WallPart}.
	 * 
	 * @return Returns the {@code status} of the {@code WallPart}.
	 */
	public Status getStatus() {
		return status;
	}
	
	/*
	 * Checks collision with another {@code GameObject}. Not implemented.
	 * 
	 * @param obj The {@code GameObject} the {@code WallPart} has collided with.
	 * @param edge The edge on which the collision happened.
	 */
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		//Not required
		
	}
}
