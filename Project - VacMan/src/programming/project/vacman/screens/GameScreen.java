package programming.project.vacman.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import programming.project.vacman.Assets;
import programming.project.vacman.VacManGame;
import programming.project.vacman.World;
import programming.project.vacman.gameobjects.Collectable;
import programming.project.vacman.gameobjects.Ghost;
import programming.project.vacman.gameobjects.VacMan;
import programming.project.vacman.gameobjects.WallPart;
import programming.project.vacman.gameobjects.Ghost.Mode;
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
	
	//Colors for the high riser screen (HRS)
	private final static Color VACMAN_HRS = new Color(255, 0, 0);
	private final static Color COOKIE_HRS = new Color(0, 255, 255);
	private final static Color INJECTION_HRS = new Color(0, 100, 255);
	private final static Color WALL_HRS = new Color(255, 100, 0);
	private final static Color GHOST_HRS = new Color(255, 0, 255);
	private final static Color GATE_HRS = new Color(100, 100, 100);
	
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
	public void render(Graphics g, Color[][] highRiserScreen, float deltaTime) {
		g.setColor(Color.RED);
		//g.drawRect(convertX(world.vacMan.getPosX()), convertY(world.vacMan.getPosY()), 68, 68);
		
		//Draw VacMan on display------------------------------------------------------------------------------------------
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
		
		//Draw VacMan on high riser
		highRiserScreen[(int) world.vacMan.getPosX()][(int) world.vacMan.getPosY()] = VACMAN_HRS;
		
		//Draw Cookies on display
		for(Collectable cookie : world.cookies) {
			if(!cookie.isCollected()) {
				//Draw cookie on display
				g.drawImage(Assets.cookie, 
						convertX(cookie.getPosX()), convertY(cookie.getPosY()), convertX(cookie.getPosX() + Collectable.DIMENSION_X), convertY(cookie.getPosY() + Collectable.DIMENSION_Y), 
						0, 0, Assets.cookie.getWidth(), Assets.cookie.getHeight(), null);
				
				
				//Draw cookie on high riser
				highRiserScreen[(int) cookie.getPosX()][(int) cookie.getPosY()] = COOKIE_HRS;
			}
		}
		
		//Draw Injections on display
		for(Collectable injection : world.injections) {
			if(!injection.isCollected()) {
				//Draw injection on display
				g.drawImage(Assets.injection, 
						convertX(injection.getPosX()), convertY(injection.getPosY()), convertX(injection.getPosX() + Collectable.DIMENSION_X), convertY(injection.getPosY() + Collectable.DIMENSION_Y), 
						0, 0, Assets.injection.getWidth(), Assets.injection.getHeight(), null);
				
				
				//Draw injection on high riser
				highRiserScreen[(int) injection.getPosX()][(int) injection.getPosY()] = INJECTION_HRS;
			}
		}
		
		//g.drawRect(convertX(world.ghost.getPosX()), convertY(world.ghost.getPosY()), 68, 68);
		
		for(WallPart wallPart : world.wallParts) {
			if(wallPart.getStatus() == Status.NORMAL) {
				//Draw wall on display
				g.drawImage(Assets.wallPart, 
						convertX(wallPart.getPosX()), convertY(wallPart.getPosY()), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
				
				//Draw wall on high riser
				highRiserScreen[(int) wallPart.getPosX()][(int) wallPart.getPosY()] = WALL_HRS;
			}
			
			if(wallPart.getStatus() == Status.GATE) {
				//Draw gate on display
				g.drawImage(Assets.wallPartGate, 
						convertX(wallPart.getPosX()), convertY(wallPart.getPosY()), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
				
				//Draw gate on high riser
				highRiserScreen[(int) wallPart.getPosX()][(int) wallPart.getPosY()] = GATE_HRS;
			}
		}

		for(Ghost ghost : world.ghosts) {
			//Draw ghost on display, chose asset according type and direction
			if(ghost.getMode() == Mode.FRIGHTEND) {
				switch(ghost.getDirection()) {
					case UP:
						g.drawImage(Assets.frightened_up, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
					case DOWN:
						g.drawImage(Assets.frightened_down, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
					case LEFT:
						g.drawImage(Assets.frightened_left, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
					case RIGHT:
						g.drawImage(Assets.frightened_right, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
				}
			}else if(ghost.getMode() == Mode.EATEN){
				switch(ghost.getDirection()) {
					case UP:
						g.drawImage(Assets.eaten_up, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
					case DOWN:
						g.drawImage(Assets.eaten_down, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
					case LEFT:
						g.drawImage(Assets.eaten_left, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
					case RIGHT:
						g.drawImage(Assets.eaten_right, 
								convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
								0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
						break;
				}
			}else {
				switch(ghost.getType()) {
					case BLINKY:
						switch(ghost.getDirection()) {
							case UP:
								g.drawImage(Assets.blinky_up, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case DOWN:
								g.drawImage(Assets.blinky_down, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case LEFT:
								g.drawImage(Assets.blinky_left, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case RIGHT:
								g.drawImage(Assets.blinky_right, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
						}
						break;
					case PINKY:
						switch(ghost.getDirection()) {
							case UP:
								g.drawImage(Assets.pinky_up, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case DOWN:
								g.drawImage(Assets.pinky_down, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case LEFT:
								g.drawImage(Assets.pinky_left, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case RIGHT:
								g.drawImage(Assets.pinky_right, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
						}
						break;
					case INKY:
						switch(ghost.getDirection()) {
							case UP:
								g.drawImage(Assets.inky_up, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case DOWN:
								g.drawImage(Assets.inky_down, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case LEFT:
								g.drawImage(Assets.inky_left, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case RIGHT:
								g.drawImage(Assets.inky_right, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
						}
						break;
					case CLYDE:
						switch(ghost.getDirection()) {
							case UP:
								g.drawImage(Assets.clyde_up, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case DOWN:
								g.drawImage(Assets.clyde_down, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case LEFT:
								g.drawImage(Assets.clyde_left, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
							case RIGHT:
								g.drawImage(Assets.clyde_right, 
										convertX(ghost.getPosX()), convertY(ghost.getPosY()), convertX(ghost.getPosX() + Ghost.DIMENSION_X), convertY(ghost.getPosY() + Ghost.DIMENSION_Y), 
										0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
								break;
						}
						break;
				}
			}
			
			//Draw ghost on high riser
			highRiserScreen[(int) ghost.getPosX()][(int) ghost.getPosY()] = GHOST_HRS;
		}
		
		g.drawRect(convertX(0), convertY(0), 1904, 952);		
		
		
		g.drawString(world.vacMan.getPosX() + ", " + world.vacMan.getPosY(), worldStartRenderX, worldStartRenderY);

		
		// Set current score below the {@code GameScreen}
		// TODO Don't use fixed values for score placement. Rather use relative ones like 0.1 * worldRenderWidth. Otherwise this will lead to different appearance on different screens.
		g.setFont(new Font("TimesRoman", Font.PLAIN, 64));
		g.setColor(Color.YELLOW);
		g.drawString("Score:" + world.score, worldStartRenderX + worldRenderWidth - 256, worldStartRenderY + worldRenderHeight + 64);

		// Draw VacMans according to lives left
		// TODO Don't use fixed values for distance between two VacMan life indicators due to different appearance on different screens. 
		for(int i = 0; i< world.vacMan.getLives(); i++) {
			g.drawImage(Assets.vacman_left,
					worldStartRenderX + i* convertX(VacMan.DIMENSION_X) +10, worldStartRenderY + worldRenderHeight +10, worldStartRenderX + (i+1)*convertX(VacMan.DIMENSION_X), worldStartRenderY + worldRenderHeight + convertX(VacMan.DIMENSION_Y),
					0, 0, Assets.vacman_right.getWidth(), Assets.vacman_right.getHeight(), null);
		}
		
		if(world.isGameOver) {			
			// Game Over
			g.setFont(new Font("TimesRoman", Font.PLAIN, 128));
			g.setColor(Color.RED);
			String text = "Game Over! Press Enter!";
			String text1 = "Your score is: " + world.score;
			
			// Draw Text on GameScreen
			int text_width  = g.getFontMetrics().stringWidth(text);
			int text_height = g.getFontMetrics().getHeight();
			int text1_width  = g.getFontMetrics().stringWidth(text1);

			g.drawString(text, worldStartRenderX + (worldRenderWidth / 2) - text_width/2, worldStartRenderY + (worldRenderHeight / 2) - text_height/2);
			g.drawString(text1, worldStartRenderX + (worldRenderWidth / 2) - text1_width/2, worldStartRenderY + (worldRenderHeight) - text_height);

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
