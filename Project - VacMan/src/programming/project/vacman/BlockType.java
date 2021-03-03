package programming.project.vacman;

/*
 * File: BlockType.java
 * -----------------------
 * This enumeration is used to simplify the conversion process from the lvl.png to the actual level. Therefore it is necessary
 * to read each RGB of each pixel of lvl.png and compare it to preset values. If they match a actual game object is created
 * later on. In this enumeration only the comparison is done. 
 */
public enum BlockType {
	WALL(0, 0, 0),					// black
	WALL_RED(100, 100, 100), 		// grey
	COIN(255, 255, 0), 				// yellow
	VACMAN(255, 0, 0), 				// red
	GHOST_SPAWNPOINT(0, 255, 0), 	// green
	GHOST_GATE(0, 0, 255); 			// blue

	/*
	 * The color of the BlockType as RGB value.
	 */
	private int color;

	/*
	 * Initializes the enumeration constants with the preset colors.
	 * 
	 * @param r The red value of a color.
	 * @param g The green value of a color.
	 * @param b The blue value of a color.
	 */
	private BlockType (int r, int g, int b) {
		//Uses bit shift to convert from single r, g and b values to a RGB value
		color = 0xff << 24 | r << 16 | g << 8 | b << 0;
	}

	/*
	 * Checks if a given color is the same as the one of the {@code BlockType}.
	 * 
	 * @param color A color as a RGB value.
	 * @return True if the color is the same otherwise false.
	 */
	public boolean sameColor (int color) {
		return this.color == color;
	}

	/*
	 * Returns the color of the {@code BlockType}.
	 * 
	 * @return Returns the color of the {@code BlockType}.
	 */
	public int getColor () {
		return color;
	}
}
