package programming.project.vacman;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MenuScreen extends Screen{
	
	private ArrayList<WallPart> wallParts;
	
	private boolean isStartSelected;

	public MenuScreen(VacManGame game) {
		super(game);
		
		wallParts = new ArrayList<WallPart>();
		isStartSelected = true;
		init();
	}

	private void init() {
		int rgb = 0;
		
		for(int i=0; i<World.WORLD_WIDTH; i++) {
			for(int j=0; j<World.WORLD_HEIGHT; j++) {
				rgb = Assets.mainMenu.getRGB(i, j);
				
				//Wall
				if(BLOCK_TYPE.WALL.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallParts.add(wallPart);
				}
				
				if(BLOCK_TYPE.WALL_RED.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallPart.toogleColor();
					wallParts.add(wallPart);
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		game.getInput().poll();
		
		if(game.getInput().keyDownOnce(KeyEvent.VK_UP) == true || game.getInput().keyDownOnce(KeyEvent.VK_DOWN) == true) {
			isStartSelected = !isStartSelected;
			
			for(WallPart wallPart : wallParts) {
				wallPart.toogleColor();
			}
		}
		
		if(game.getInput().keyDownOnce(KeyEvent.VK_ENTER) == true) {
			if(isStartSelected) {
				game.setScreen(new GameScreen(game));
			}else {
				game.close();
			}
		}
		
		if(game.getInput().keyDown(KeyEvent.VK_ESCAPE) == true) {
			game.close();
		}
	}

	@Override
	public void render(Graphics g, float deltaTime) {
		for(WallPart wallPart : wallParts) {
			if(wallPart.isRed()) {
				g.drawImage(Assets.wallPartRed, 
						convertX(wallPart.posX), convertY(wallPart.posY), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
			}else {
				g.drawImage(Assets.wallPart, 
						convertX(wallPart.posX), convertY(wallPart.posY), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
			}
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
