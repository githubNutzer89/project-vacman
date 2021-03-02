package programming.project.vacman.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import programming.project.vacman.Assets;
import programming.project.vacman.VacManGame;
import programming.project.vacman.World;
import programming.project.vacman.gameobjects.Cookie;
import programming.project.vacman.gameobjects.Ghost;
import programming.project.vacman.gameobjects.VacMan;
import programming.project.vacman.gameobjects.WallPart;
import programming.project.vacman.gameobjects.WallPart.Status;

/* File: GameScreen.java
 * -----------------------
 * {@code GameScreen} initializes the data model {@code World} for the game and updates it permanently dependent on keyboard
 * inputs. Also it draws the game on the display in its render method and handles different states of the program (paused,
 * resumed, disposed).  
 */
public class GameScreen extends Screen{
	enum GameState {
        READY,
        RUNNING,
        PAUSED,
        GAMEOVER
    }
	
	private World world;
	private GameState state = GameState.RUNNING;
	
	/*
	 * Initializes the newly created {@code GameScreen} object by creating a data model {@code World}.
	 * 
	 * @param game A reference to VacManGame to be saved. This enables access to the {@code Renderer} and the 
	 * {@code KeyboardInputManager}.
	 */
	public GameScreen(VacManGame game) {
		super(game);
		world = new World();
	}

	/*
     * Updates the underlying data model {@code World} in accordance with pressed keys.
     * 
     * @param deltaTime The time between two frames.
     */
	@Override
	public void update(float deltaTime) {
		game.getInput().poll();
		
		switch(state) {
			case RUNNING:
				updateRunning(deltaTime);
				break;
			case GAMEOVER:
				updateGameover(deltaTime);
				break;
		}			
	}

	 private void updateGameover(float deltaTime) {
		 if(game.getInput().keyPressed(KeyEvent.VK_ENTER)) {
			 world.resetWorld();
			 state = GameState.RUNNING;
		 }
	}

	private void updateRunning(float deltaTime) {
		//LEFT --> Move VacMan left
		if(game.getInput().keyPressed(KeyEvent.VK_LEFT) == true) {
			world.vacMan.moveLeft();
			world.vacMan.update(deltaTime);
		}
		
		//UP --> Move VacMan up
		if(game.getInput().keyPressed(KeyEvent.VK_UP) == true) {
			world.vacMan.moveUp();
			world.vacMan.update(deltaTime);
		}
		
		//RIGHT --> Move VacMan right
		if(game.getInput().keyPressed(KeyEvent.VK_RIGHT) == true) {
			world.vacMan.moveRight();
			world.vacMan.update(deltaTime);
		}
		
		//DOWN --> Move VacMan down
		if(game.getInput().keyPressed(KeyEvent.VK_DOWN) == true) {
			world.vacMan.moveDown();
			world.vacMan.update(deltaTime);
		}
	
		//ESC --> Exit game
		if(game.getInput().keyPressed(KeyEvent.VK_ESCAPE) == true) {
			game.close();
		}
	
		//Update world
		world.update(deltaTime);
		
		if(world.isGameOver) {
			state = GameState.GAMEOVER;
		}
	}

	/*
     * Draws the data model {@code World} on the display.
     * 
     * @param gfx A {@code Graphics} object necessary to draw on the screen.
     * @param deltaTime The time between two frames.
     */
	@Override
	public void render(Graphics g, float deltaTime) {
		// TODO Auto-generated method stub
		g.setColor(Color.RED);
		//g.drawRect(convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), 68, 68);
		
		switch(world.vacMan.getDirection()) {
			case UP:
				g.drawImage(Assets.vacman_up, 
						convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
			case RIGHT:
				g.drawImage(Assets.vacman_right, 
						convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
			case DOWN:
				g.drawImage(Assets.vacman_down, 
						convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
			case LEFT:
				g.drawImage(Assets.vacman_left, 
						convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), convertX(world.vacMan.getPosX() + VacMan.DIMENSION_X), convertY(world.vacMan.getPosY() + VacMan.DIMENSION_Y), 
						0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
				break;
		}
		
		for(Cookie coin : world.cookies) {
			if(!coin.isCollected()) {
				g.drawImage(Assets.coin, 
						convertX(coin.getPosX()), convertY(coin.getPosY()), convertX(coin.getPosX() + Cookie.DIMENSION_X), convertY(coin.getPosY() + Cookie.DIMENSION_Y), 
						0, 0, Assets.coin.getWidth(), Assets.coin.getHeight(), null);
			}
		}
		
		//g.drawRect(convertX(world.ghost.getPosX()), convertY(world.ghost.getPosY()), 68, 68);
		
		for(WallPart wallPart : world.wallParts) {
			if(wallPart.getStatus() == Status.NORMAL) {
				g.drawImage(Assets.wallPart, 
						convertX(wallPart.getPosX()), convertY(wallPart.getPosY()), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
			}
			
			if(wallPart.getStatus() == Status.GATE) {
				g.drawImage(Assets.wallPartGate, 
						convertX(wallPart.getPosX()), convertY(wallPart.getPosY()), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
			}
		}
		// TODO Add image for ghosts. Maybe different per category?
		for(Ghost ghost : world.ghosts) {
			g.drawImage(Assets.vacman_up, 
					convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
					0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
		}
		
		g.drawRect(convertX(0), convertY(0), 1904, 952);		
		
		
		g.drawString(world.vacMan.getPosX() + ", " + world.vacMan.getPosY(), worldStartRenderX, worldStartRenderY);

		
		// Set current score at the bottom right of the {@code GameScreen}
		String scoreText = "Score: " + world.score;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 64));
		g.setColor(Color.YELLOW);
		g.drawString(scoreText, worldStartRenderX + worldRenderWidth - g.getFontMetrics().stringWidth(scoreText), worldStartRenderY + worldRenderHeight);
		 

		// Draw VacMans according to lives below spawn point of {@code VacMan}
		for(int i = 0; i< world.vacMan.getLives(); i++) {
			g.drawImage(Assets.vacman_left,
					convertX(world.vacMan.getSpawnPos().getX() + i*VacMan.DIMENSION_X), convertY(world.vacMan.getSpawnPos().getY()+VacMan.DIMENSION_Y), convertX(world.vacMan.getSpawnPos().getX() + (i+1)*VacMan.DIMENSION_X), convertY(world.vacMan.getSpawnPos().getY() + VacMan.DIMENSION_Y + VacMan.DIMENSION_Y), 
					0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
		}
		
		// If game is over, display results.
		if(state == GameState.GAMEOVER) {			
			
			// Initialize optional text.
			String text = null;

			// Set optional text if the game is lost or won.
			if(world.vacMan.getLives() == 0) {
				text = "Game Over! Press Enter!";
				g.setColor(Color.RED);
			} else if(world.score == world.cookies.size()) {
				text = "You Won! Press Enter!";
				g.setColor(Color.GREEN);
			}
			
			// Set font size.
			g.setFont(new Font("TimesRoman", Font.PLAIN, 128));
			
			// Draw Text on GameScreen.
			int text_width  = g.getFontMetrics().stringWidth(text);
			int text_height = g.getFontMetrics().getHeight();
			int text1_width  = g.getFontMetrics().stringWidth(scoreText);
			g.drawString(text, worldStartRenderX + (worldRenderWidth / 2) - text_width/2, worldStartRenderY + (worldRenderHeight / 2) - text_height/2);
			g.drawString(scoreText, worldStartRenderX + (worldRenderWidth / 2) - text1_width/2, worldStartRenderY + (worldRenderHeight) - text_height);
		}
		

	}
	
	/*
     * Called when the game pauses.
     */
	@Override
    public void pause() {
//        if(state == GameState.Running)
//            state = GameState.Paused;
//        
//            Settings.addScore(world.score);
//            Settings.save(game.getFileIO());
    }

	/*
     * Called when the game resumes.
     */
    @Override
    public void resume() {
        
    }

    /*
     * Releases resources when they are not needed anymore.
     */
    @Override
    public void dispose() {
        
    }
}
