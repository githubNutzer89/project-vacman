package programming.project.vacman;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/* File: Renderer.java
 * -----------------------
 * This class takes care of rendering. It draws whatever is defined in the render-method of the current {@code Screen}
 * using double buffering.
 */
public class Renderer extends JFrame {

	private VacManGame game;

	private Frame app;
	private BufferStrategy buffer;
	private GraphicsDevice dev;

	private Color background;

	/**
	 * Initializes a newly created Renderer object meaning it creates a window, sets it to full screen, creates a back buffer
	 * for double buffering, sets the background to black and registers the {@code KeyInputManager} for keyboard inputs
	 * to the window. 
	 * 
	 */
	public Renderer(VacManGame game) {
		this.game = game;

		//Create a new 'naked' window and don't allow repaint events (we are taking care of it by ourselves)
		app = new Frame();
		app.setIgnoreRepaint(true);
		app.setUndecorated(true);

		//Set it to full screen
		dev = app.getGraphicsConfiguration().getDevice();
		dev.setFullScreenWindow(app);

		//Create a back buffer for double buffering
		app.createBufferStrategy(2);
		buffer = app.getBufferStrategy();

		//The background if nothing is drawn
		background = Color.BLACK;

		//Register our KeyInputManager for keyboard inputs
		app.addKeyListener(game.getInput());
	}

	/*
	 * It draws whatever is defined in the render-method of the current {@code Screen}.
	 */
	public void render(float deltaTime) {
		//Get the back buffer and fill it black
		Graphics gfx = buffer.getDrawGraphics();
		gfx.setColor(background);
		gfx.fillRect(0, 0, getWidth(), getHeight());

		//Perform drawing
		game.getCurrentScreen().render(gfx, deltaTime);

		//Release system resources
		gfx.dispose();
		
		//Show back buffer
		if (!buffer.contentsLost())
			buffer.show();
	}

	/*
	 * Deselects full screen mode whenever the program closes.
	 * 
	 */
	public void dispose() {
		dev.setFullScreenWindow(null);
	}

	/*
	 * Returns the width of the display in pixel.
	 * 
	 * @return Returns the width of the display in pixel.
	 */
	public int getWidth() {
		return app.getWidth();
	}

	/*
	 * Returns the height of the display in pixel.
	 * 
	 * @return Returns the height of the display in pixel.
	 */
	public int getHeight() {
		return app.getHeight();
	}
}
