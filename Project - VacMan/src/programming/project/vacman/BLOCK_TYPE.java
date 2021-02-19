package programming.project.vacman;

public enum BLOCK_TYPE {
	WALL(0, 0, 0), // black
	WALL_RED(100, 100, 100), // grey
	COIN(255, 255, 0), // yellow
	VACMAN(255, 0, 0), // red
	GHOST_SPAWNPOINT(0, 255, 0), // green
	ITEM_GOLD_COINGHOST_GATE(0, 0, 255); // blue

	private int color;

	private BLOCK_TYPE (int r, int g, int b) {
		color = 0xff << 24 | r << 16 | g << 8 | b << 0;
	}

	public boolean sameColor (int color) {
		return this.color == color;
	}

	public int getColor () {
		return color;
	}
}
