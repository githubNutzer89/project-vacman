package programming.project.vacman;

import java.awt.event.KeyEvent;

import programming.project.vacman.screens.LoadingScreen;
import programming.project.vacman.screens.Screen;

/* File: VacManGame.java
 * -----------------------
 * This class represents the entry point of the program. It contains the main-method and the game loop. The game loop is
 * an infinite running loop where the data model is permanently updated and drawn. Furthermore this class holds references
 * to {@code KeyboardInput}, the {@code Screen} and the {@code Renderer}. A reference of {@code VacManGame} is passed
 * to each new screen created so every screen is able to draw, react on keyboard inputs and change the current screen to
 * another one. 
 */
public class VacManGame {
	/*
	 * A reference to check for keyboard inputs.
	 */
	private KeyboardInputManager input;
	
	/*
	 * Holds the {@code Screen} currently displayed.
	 */
	private Screen screen;
	
	/*
	 * The {@code Renderer} which draws on the display.
	 */
	private Renderer renderer;
	//private Audio audio;
	
	/*
	 * Determines if the game is running or not.
	 */
	public volatile boolean isRunning;

	/**
	 * Initializes a newly created VacManGame object. The necessary references to handle keyboard input,
	 * rendering and screen management are created. Furthermore the game loop is started. 
	 * 
	 */
	public VacManGame() {
		input = new KeyboardInputManager();			//to handle keyboard input
		renderer = new Renderer(this);			//to draw on the display
		screen = new LoadingScreen(this);		//first screen shown is the LoadingScreen
		  
		isRunning = true;						//set game loop running
		  
		startGameLoop();						//start the game loop
		
		// TODO Actually, I don't like this procedure yet.
		while (!isRunning) {					// VacMan died and GameLoop is stopped
			getInput().poll();
			if(getInput().keyPressed(KeyEvent.VK_ENTER)) {	// Reload Screen with enter		
				screen = new LoadingScreen(this);		
				  
				isRunning = true;						
				  
				startGameLoop();															
			}
		}

	}
	  
	/**
	 * Starts the game loop and calculates the {@code deltaTime}. The {@code deltaTime} is the time between two frames and used
	 * to ensure motion of game objects independently from the refresh rate of the computer system.
	 */
	private void startGameLoop() {
		long startTime = System.nanoTime();											//current time

		while (isRunning) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;		//deltaTime = current time - time of the last frame
			startTime = System.nanoTime();											//reset startTime

			getCurrentScreen().update(deltaTime);									//update the data model of the current screen
			renderer.render(deltaTime);												//draw the data model of the current screen
		}
	}

	/*
	 * Sets a new {@code Screen}.
	 * 
	 * @param screen The new screen to be displayed.
	 * 
	 * @throws IllegalArgumentException if {@code screen == null}
	 */
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		//TODO Check if all the different states a screen can be in are really necessary (asses at the the end of the project).
		this.screen.pause();
		this.screen.dispose();
		screen.resume();

		screen.update(0);		//Do a first update to initialize the screen
		this.screen = screen;	//Set screen as the current one
	}

	/*
	 * Returns the reference to the current {@Screen}.
	 * 
	 * @return reference to the current {@Screen}.
	 */
	public Screen getCurrentScreen() {
		return screen;
	}
	
	/*
	 * Returns the reference to {@KeyboardInput}.
	 * 
	 * @return reference to {@KeyboardInput}.
	 */
	public KeyboardInputManager getInput() {
		return input;
	}

	/*
	 * Returns the reference to {@Renderer}.
	 * 
	 * @return reference to {@Renderer}.
	 */
	public Renderer getRenderer() {
		return renderer;
	}

	// public Audio getAudio() { return audio; }

	/*
	 * Closes the program and handles all the necessary operations associated with it.
	 */
	public void close() {
		//Stop the game loop
		isRunning = false;
		
		//TODO Check if all the different states a screen can be in are really necessary (asses at the the end of the project).
		screen.pause();
		screen.dispose();

		//Terminates the program
		System.exit(0);
	}
	
	/*
	 * Creates a new instance of VacManGame and calls its start method.
	 */
	public static void main(String[] args) {
		new VacManGame();
	}
}
 