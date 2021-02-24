package programming.project.vacman.screens;

import java.awt.Graphics;
import programming.project.vacman.VacManGame;
import programming.project.vacman.World;

/* File: Screen.java
 * -----------------------
 * An abstract class which is extended by the actual screens. It provides methods to handle the different states the
 * program can be in, to update the data model and to draw on the display. 
 */
public abstract class Screen {
    protected final VacManGame game;
    
    protected int worldRenderHeight;
	protected int worldRenderWidth;
	protected int worldStartRenderX;
	protected int worldStartRenderY;

	/*
	 * Initializes the {@code Screen}.
	 * 
	 * Determines the area needed to draw the {@code World} on the display and calculates the upper left coordinates of this area
	 * to center it on the display. Additionally it saves a reference to {@code VacManGame}.
	 * 
	 *  @param game A reference to VacManGame. So every {@code Screen} is able to render and to process keyboard inputs.
	 */
    public Screen(VacManGame game) {
        this.game = game;
        
        worldRenderWidth = game.getRenderer().getWidth() - game.getRenderer().getWidth() % World.WORLD_WIDTH;
        worldRenderHeight = (int) (worldRenderWidth * (double) World.WORLD_HEIGHT / World.WORLD_WIDTH);
        worldStartRenderX = (game.getRenderer().getWidth() - worldRenderWidth) / 2;
        worldStartRenderY = (game.getRenderer().getHeight() - worldRenderHeight) / 2;
    }

    /*
     * Updates the underlying data model.
     * 
     * @param deltaTime The time between two frames.
     */
    public abstract void update(float deltaTime);

    /*
     * Draws the data model on the display.
     * 
     * @param gfx A {@code Graphics} object necessary to draw on the screen.
     * @param deltaTime The time between two frames.
     */
    public abstract void render(Graphics gfx, float deltaTime);
    
    /*
     * Called when the game pauses.
     */
    public abstract void pause();

    /*
     * Called when the game resumes.
     */
    public abstract void resume();
    
    /*
     * Releases resources when they are not needed anymore.
     */
    public abstract void dispose();
    
    /*
     * Converts the x-coordinate from {@code World} values to actual pixel values.
     * 
     * @param posX The x-coordinate as {@code World} value.
     * @return Returns the corresponding pixel value on the x-axis.
     */
    protected int convertX(float posX) {
    	return (int) (worldStartRenderX + worldRenderWidth * posX / World.WORLD_WIDTH);
    }
    
    /*
     * Converts the y-coordinate from {@code World} values to actual pixel values.
     * 
     * @param posY The y-coordinate as {@code World} value.
     * @return Returns the corresponding pixel value on the y-axis.
     */
    protected int convertY(float posY) {
    	return (int) (worldStartRenderY + worldRenderHeight * posY / World.WORLD_HEIGHT);
    }
}
