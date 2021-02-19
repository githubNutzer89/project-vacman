package programming.project.vacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GameScreen extends Screen{
	private TestObj obj;
	private World world;
	
	public GameScreen(VacManGame game) {
		super(game);
		
		obj = new TestObj();
		world = new World();
	}

	@Override
	public void update(float deltaTime) {
		game.getInput().poll();
				
		if(game.getInput().keyDown(KeyEvent.VK_LEFT) == true) {
			world.vacMan.moveLeft(deltaTime);
		}
		
		if(game.getInput().keyDown(KeyEvent.VK_UP) == true) {
			world.vacMan.moveUp(deltaTime);
		}
		
		if(game.getInput().keyDown(KeyEvent.VK_RIGHT) == true) {
			world.vacMan.moveRight(deltaTime);
		}
		
		if(game.getInput().keyDown(KeyEvent.VK_DOWN) == true) {
			world.vacMan.moveDown(deltaTime);
		}
		
		if(game.getInput().keyDown(KeyEvent.VK_ESCAPE) == true) {
			game.close();
		}
		
		world.update(deltaTime);
	}

	@Override
	public void render(Graphics g, float deltaTime) {
		// TODO Auto-generated method stub
		g.setColor(Color.RED);
		g.drawRect(convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), 68, 68);
		g.drawRect(convertX(world.ghost.getPosX()), convertY(world.ghost.getPosY()), 68, 68);
		if(!world.coin.isCollected()) {
			g.drawRect(convertX(world.coin.getPosX()), convertY(world.coin.getPosY()), 68, 68);
		}
		for(WallPart wallPart : world.wallParts) {
			//g.drawRect(Math.round(convertX(wallPart.getPosX())), convertY(wallPart.getPosY()), 68, 68);
			g.drawImage(Assets.wallPart, 
					convertX(wallPart.posX), convertY(wallPart.posY), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
					0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
		}
		g.drawRect(convertX(0), convertY(0), 1904, 952);
		
		
		
		g.drawString(world.vacMan.posX + ", " + world.vacMan.posY, worldStartRenderX + 100, worldStartRenderY + 10);
	}
	
	@Override
    public void pause() {
//        if(state == GameState.Running)
//            state = GameState.Paused;
//        
//        if(world.gameOver) {
//            Settings.addScore(world.score);
//            Settings.save(game.getFileIO());
//        }
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
}
