package programming.project.vacman;

import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class Screen {
    protected final VacManGame game;
    
    protected int worldRenderHeight;
	protected int worldRenderWidth;
	protected int worldStartRenderX;
	protected int worldStartRenderY;

    public Screen(VacManGame game) {
        this.game = game;
        
        worldRenderWidth = game.getRenderer().getWidth() - game.getRenderer().getWidth() % World.WORLD_WIDTH;
        worldRenderHeight = (int) (worldRenderWidth * (double) World.WORLD_HEIGHT / World.WORLD_WIDTH);
        worldStartRenderX = (game.getRenderer().getWidth() - worldRenderWidth) / 2;
        worldStartRenderY = (game.getRenderer().getHeight() - worldRenderHeight) / 2;
    }

    public abstract void update(float deltaTime);

    public abstract void render(Graphics gfx, float deltaTime);
    
    public abstract void pause();

    public abstract void resume();
    
    public abstract void dispose();
    
    protected int convertX(float posX) {
    	return (int) (worldStartRenderX + worldRenderWidth * posX / World.WORLD_WIDTH);
    }
    
    protected int convertY(float posY) {
    	return (int) (worldStartRenderY + worldRenderHeight * posY / World.WORLD_HEIGHT);
    }
}
