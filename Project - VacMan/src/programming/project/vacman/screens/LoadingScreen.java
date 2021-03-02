package programming.project.vacman.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import programming.project.vacman.Assets;
import programming.project.vacman.VacManGame;

/* File: LoadingScreen.java
 * -----------------------
 * This class loads all the {@code Assets} available. It doesn't draw anything on the display. After it is finished
 * the {@code MenuScreen} is shown.
 */
public class LoadingScreen extends Screen{

	/*
	 * Initializes a newly created LoadingScreen object and saves the game instance in the super class.
	 */
	public LoadingScreen(VacManGame game) {
		super(game);
	}

	/*
	 * Loads all the {@code Assets} and shows the {@code MenuScreen}.
	 * 
	 * @param deltaTime The time between two frames.
	 */
	@Override
	public void update(float deltaTime) {
		try {
			Assets.wallPart = ImageIO.read(new File("img/wallPart.png"));
			Assets.wallPartRed = ImageIO.read(new File("img/wallPartRed.png"));
			Assets.wallPartGate = ImageIO.read(new File("img/wallpart_gate.png"));
			Assets.mainMenu = ImageIO.read(new File("img/main_menu.png"));
			Assets.lvl1 = ImageIO.read(new File("img/lvl1.png"));
			Assets.coin = ImageIO.read(new File("img/coin.png"));
			Assets.vacman_right = ImageIO.read(new File("img/vacman_right.png"));
			Assets.vacman_left = ImageIO.read(new File("img/vacman_left.png"));
			Assets.vacman_up = ImageIO.read(new File("img/vacman_up.png"));
			Assets.vacman_down = ImageIO.read(new File("img/vacman_down.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Show the MenuScreen
		game.setScreen(new MenuScreen(game));
	}

	/*
     * Draws nothing.
     * 
     * @param gfx A {@code Graphics} object necessary to draw on the screen.
     * @param deltaTime The time between two frames.
     */
	@Override
	public void render(Graphics gfx, Color[][] highRiserScreen, float deltaTime) {
		//Not required
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
