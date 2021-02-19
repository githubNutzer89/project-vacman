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
		//g.drawRect(convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), 68, 68);
		
		switch(world.vacMan.getDirection()) {
			case UP:
				g.drawImage(Assets.vacman_up, 
						convertX(world.vacMan.posX), convertY(world.vacMan.posY), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
			case RIGHT:
				g.drawImage(Assets.vacman_right, 
						convertX(world.vacMan.posX), convertY(world.vacMan.posY), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
			case DOWN:
				g.drawImage(Assets.vacman_down, 
						convertX(world.vacMan.posX), convertY(world.vacMan.posY), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
			case LEFT:
				g.drawImage(Assets.vacman_left, 
						convertX(world.vacMan.posX), convertY(world.vacMan.posY), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
		}
				
		//g.drawRect(convertX(world.ghost.getPosX()), convertY(world.ghost.getPosY()), 68, 68);
		
		for(Coin coin : world.coins) {
			if(!coin.isCollected()) {
				g.drawImage(Assets.coin, 
						convertX(coin.posX), convertY(coin.posY), convertX(coin.getPosX() + Coin.DIMENSION_X), convertY(coin.getPosY() + Coin.DIMENSION_Y), 
						0, 0, Assets.coin.getWidth(), Assets.coin.getHeight(), null);
			}
		}
		
		for(WallPart wallPart : world.wallParts) {
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
