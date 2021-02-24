package programming.project.vacman.util;

/* File: Vector2D.java
 * -----------------------
 * {@code Vector2D} is an helper class to provide vector operations. It saves a pair of coordinates as a vector. The user can set and 
 * get the x and y value of a vector as well as conduct add operations, get the length, multiplication with a scalar, a certain 
 * type of subtraction and check if vectors are equal.    
 */
public class Vector2D {
	private float x;
	private float y;
	
	/*
	 * Initializes the newly created {@code Vector2D} object by saving x and y value.
	 * 
	 * @param x The x-value of the vector.
	 * @param y The y-value of the vector. 
	 */
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Set the x-value.
	 * 
	 * @param x The x-value of the vector. 
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/*
	 * Set the y-value.
	 * 
	 * @param y The y-value of the vector. 
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/*
	 * Sets the vector to a new position by using another {@code Vector2D}.
	 * 
	 * @param vec The other {@code Vector2D} which position should be copied. 
	 */
	public void setPos(Vector2D vec) {
		this.x = vec.getX();
		this.y = vec.getY();
	}
		
	/*
	 * Returns the x-value of the vector.
	 * 
	 * @return X-Value of the vector.
	 */
	public float getX() {
		return x;
	}
	
	/*
	 * Returns the y-value of the vector.
	 * 
	 * @return Y-Value of the vector.
	 */
	public float getY() {
		return y;
	}
	
	/*
	 * Adds another vector to this one.
	 * 
	 * @param x The x-value of the vector to be added.
	 * @param y The y-value of the vector to be added. 
	 */
	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	/*
	 * Adds another vector to the values of this one and returns a newly created {@code Vector2D}.
	 * 
	 * @param vec The {@code Vector2D} to be added.
	 * @return Returns a new {@code Vector2D} which represents the sum. 
	 */
	public Vector2D add(Vector2D vec) {
		return new Vector2D(x + vec.getX(), y + vec.getY());
	}
	
	/*
	 * Multiplies the vector with a scalar and returns the product as a new {@code Vector2D}.
	 * 
	 * @param val The scalar to be added.
	 * @return Returns a new {@code Vector2D} which represents the result of this operation. 
	 */
	public Vector2D multiplyScalar(float val) {
		return new Vector2D(x * val, y * val);
	}
	
	/*
	 * Returns {@code true} if this vector and another {@code Vector2D} are equal.
	 * 
	 * @param vec The {@code Vector2D} to be checked if equal.
	 * @return Returns {@code true} if this vector and {@code vec} are equal. 
	 */
	public boolean isEqual(Vector2D vec) {
		return x == vec.getX() && y == vec.getY();
	}
	
	/*
	 * Returns the length of the vector.
	 * 
	 * @return Returns the length of the vector. 
	 */
	public float getLength() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	/*
	 * Returns a vector from the position the current vector is pointing to towards
	 * the position specified by the parameter {@code vec}.
	 * 
	 *  @param vec Position where the resulting vector is pointing to
	 *  @return Vector from position of the current vector towards the position of {@code vec} 
	 */
	public Vector2D getVectorTo(Vector2D vec) {
		return new Vector2D(vec.getX() - x, vec.getY() - y);
	}
}
