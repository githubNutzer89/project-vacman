package programming.project.vacman;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;

public class VacManRenderer extends JFrame{
	private VacManGame game;
	
	private Frame app;
	private BufferStrategy buffer;
	private GraphicsEnvironment ge;
	private GraphicsDevice dev;
	private GraphicsConfiguration gc;
	private BufferedImage bi;
    
	private Graphics graphics = null;
	private Graphics2D g2d = null;
	private Color background;
	private Color textColor;
	private Font font;
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
	public VacManRenderer(VacManGame game) {
		this.game = game;
		
		// Create game window...
        app = new Frame();
        app.setIgnoreRepaint(true);
        app.setUndecorated(true);

        dev = app.getGraphicsConfiguration().getDevice();
        // Change to full screen
        dev.setFullScreenWindow(app);

        // Create BackBuffer...
        app.createBufferStrategy(2);
        buffer = app.getBufferStrategy();

        // Objects needed for rendering...
        background = Color.BLACK;
        textColor = Color.GREEN;
        font = new Font("Courier New", Font.PLAIN, 12);
	    
	 // Hookup keyboard polling
	    app.addKeyListener(game.getInput());
	    //canvas.addKeyListener(game.getInput());
	}
	
	public void render(float deltaTime) {
		Graphics gfx = buffer.getDrawGraphics();
        gfx.setColor(background);
        gfx.fillRect(0, 0, getWidth(), getHeight());

        game.getCurrentScreen().render(gfx, deltaTime);

        // display frames per second...
        gfx.setFont(font);
        gfx.setColor(textColor);
        gfx.drawString("FPS: ", 20, 20);

        gfx.dispose();
        if(!buffer.contentsLost()) buffer.show();
	}
	
	public void dispose() {
		dev.setFullScreenWindow(null);
	}
	
	public int getWidth() {
		return app.getWidth();
	}
	
	public int getHeight() {
		return app.getHeight();
	}
}
