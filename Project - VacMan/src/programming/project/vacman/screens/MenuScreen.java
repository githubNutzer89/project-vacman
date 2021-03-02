package programming.project.vacman.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import programming.project.vacman.Assets;
import programming.project.vacman.BlockType;
import programming.project.vacman.VacManGame;
import programming.project.vacman.World;
import programming.project.vacman.gameobjects.WallPart;
import programming.project.vacman.gameobjects.WallPart.Status;

/* File: MenuScreen.java
 * -----------------------
 * {@code MenuScreen} works the same way as {@code GameScreen}. In this case only a menu consisting of two entries is drawn 
 * so the data model is rather simple. Due to its simplicity it is not encapsulated in its own class. It's only represented by one 
 * {@code boolean} variable {@code isStartSelected} which is toggled {@code true}/{@code false} to save the state. Also it draws the 
 * menu on the display in its render method by using {@code WallParts} and handles different states of the program (paused,
 * resumed, disposed).  
 */

//TODO Consider to create a new class as a data model for MenuScreen to fulfill the MVC pattern requirement.

public class MenuScreen extends Screen{
	
	private ArrayList<WallPart> wallParts;
	
	private boolean isStartSelected;

	/*
	 * Initializes the newly created {@code MenuScreen} object.
	 * 
	 * @param game A reference to VacManGame to be saved. This enables access to the {@code Renderer} and the 
	 * {@code KeyboardInputManager}.
	 */
	public MenuScreen(VacManGame game) {
		super(game);
		
		wallParts = new ArrayList<WallPart>();
		isStartSelected = true;
		init();
	}

	/*
	 * Positions the {@code WallParts} to be drawn according to the main_menu.png. They will represent 'Start' and 'Quit'
	 * and saves them in an {@code ArrayList wallParts}. 
	 * 
	 */
	private void init() {
		//The RGB value
		int rgb = 0;
		
		//Go through each pixel
		for(int i=0; i<World.WORLD_WIDTH; i++) {
			for(int j=0; j<World.WORLD_HEIGHT; j++) {
				//The RGB values of the pixel
				rgb = Assets.mainMenu.getRGB(i, j);
				
				//Wall NORMAL
				if(BlockType.WALL.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallParts.add(wallPart);
				}
				
				//Wall RED
				if(BlockType.WALL_RED.sameColor(rgb)) {
					WallPart wallPart = new WallPart();
					wallPart.setPos(i, j);
					wallPart.setStatus(Status.RED);;
					wallParts.add(wallPart);
				}
			}
		}
	}

	/*
     * Updates the underlying data model in accordance with pressed keys, i.e. if an applicable key is pressed all {@code WallPart}s
     * and {@code isStartSelected} will be toggled.
     * 
     * @param deltaTime The time between two frames.
     */
	@Override
	public void update(float deltaTime) {
		game.getInput().poll();
		
		if(game.getInput().keyPressedOnce(KeyEvent.VK_UP) == true || game.getInput().keyPressedOnce(KeyEvent.VK_DOWN) == true) {
			isStartSelected = !isStartSelected;			
			
			for(WallPart wallPart : wallParts) {
				wallPart.toogleRedNormal();
			}
		}
		
		if(game.getInput().keyPressedOnce(KeyEvent.VK_ENTER) == true) {
			if(isStartSelected) {
				game.setScreen(new GameScreen(game));
			}else {
				game.close();
			}
		}
		
		if(game.getInput().keyPressed(KeyEvent.VK_ESCAPE) == true) {
			game.close();
		}
	}

	/*
     * Draws the {@code WallPart}s according their {@code Status} on the display. 
     * 
     * @param gfx A {@code Graphics} object necessary to draw on the screen.
     * @param highRiserScreen An array of {@code Color}s where each item corresponds to a window of the high riser. The saved {@code Color} will be displayed.
     * @param deltaTime The time between two frames.
     */
	@Override
	public void render(Graphics g, Color[][] highRiserScreen, float deltaTime) {
		for(WallPart wallPart : wallParts) {
			if(wallPart.getStatus() == Status.RED) {
				//Draw on display
				g.drawImage(Assets.wallPartRed, 
						convertX(wallPart.getPosX()), convertY(wallPart.getPosY()), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
				
				//Draw on high riser
				highRiserScreen[(int) wallPart.getPosX()][(int) wallPart.getPosY()] = new Color(255, 0, 0);
			}else {
				//Draw on display
				g.drawImage(Assets.wallPart, 
						convertX(wallPart.getPosX()), convertY(wallPart.getPosY()), convertX(wallPart.getPosX() + WallPart.DIMENSION_X), convertY(wallPart.getPosY() + WallPart.DIMENSION_Y), 
						0, 0, Assets.wallPart.getWidth(), Assets.wallPart.getHeight(), null);
				
				//Draw on high riser
				highRiserScreen[(int) wallPart.getPosX()][(int) wallPart.getPosY()] = new Color(100, 100, 100);
			}
		}
	}

	/*
     * Called when the game pauses.
     */
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	/*
     * Called when the game resumes.
     */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	/*
     * Releases resources when they are not needed anymore.
     */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
