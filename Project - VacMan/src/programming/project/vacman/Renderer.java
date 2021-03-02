package programming.project.vacman;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

import programming.project.vacman.lighthouse.LighthouseDisplay;

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
	
	/*
	 * Represents the uni high riser display.
	 */
	private LighthouseDisplay display;
	private Color[][] highRiserScreen;

	private Color background;

	/**
	 * Initializes a newly created Renderer object meaning it creates a window, sets it to full screen, creates a back buffer
	 * for double buffering, sets the background to black and registers the {@code KeyInputManager} for keyboard inputs
	 * to the window. 
	 * 
	 */
	public Renderer(VacManGame game, LighthouseDisplay display) {
		this.game = game;
		this.display = display;
		
		//Initialize the high riser screen
		highRiserScreen = new Color[28][14];

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

		//Set the high riser all black
		highRiserScreen = new Color[28][14];
		
		//Perform drawing
		game.getCurrentScreen().render(gfx, highRiserScreen, deltaTime);
		
		//Send image to actual high riser screen
		try {
			display.sendImage(convertToByte(highRiserScreen));
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}

		//Release system resources
		gfx.dispose();
		
		//Show back buffer
		if (!buffer.contentsLost())
			buffer.show();
	}

	/*
	 * Converts from an 2D array of {@code Color} to an array 1D array of {@code byte}. Each {@code Color} consists of 3 bytes, i.e.
	 * each item in the 2D array corresponds to 3 items in the 1D {@code byte} array.
	 * 
	 * @param highRiserScreen A 2D array of {@code Color}.
	 * @return A 1D array of {@code byte}.
	 */
	private byte[] convertToByte(Color[][] highRiserScreen) {
		Color color;
		byte[] highRiserScreenByte = new byte[14 * 28 * 3];
		
		for(int i=0; i<28; i++) {
			for(int j=0; j<14; j++) {
				color = highRiserScreen[i][j];
				
				for(int k=0; k<3; k++) {
					if(color != null) {						
						switch(k) {
							case 0:
								highRiserScreenByte[(28 * j + i) * 3 + k] = (byte) color.getRed();
								break;
							case 1:
								highRiserScreenByte[(28 * j + i) * 3 + k] = (byte) color.getGreen();
								break;
							case 2:
								highRiserScreenByte[(28 * j + i) * 3 + k] = (byte) color.getBlue();
								break;
						}
					}else {
						highRiserScreenByte[(28 * j + i) * 3 + k] = 0;
					}
				}
			}
		}
		
		return highRiserScreenByte;
	}

	/*
	 * Deselects full screen mode whenever the program closes and terminates connection to Lighthouse.
	 * 
	 */
	public void dispose() {
		display.close();
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
