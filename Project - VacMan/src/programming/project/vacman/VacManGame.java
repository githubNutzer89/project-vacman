package programming.project.vacman;

public class VacManGame {
	private KeyboardInput input;
	private Screen screen;
	//private VacManController controller;
	private VacManRenderer renderer;
	//private Audio audio;
	//private FileIO fileIO;
	
	private volatile boolean running;

	  public VacManGame() {
		  input = new KeyboardInput();
		  renderer = new VacManRenderer(this);
		  screen = new LoadingScreen(this);
		  
		  running = true;
		  
		  startGameLoop();
		  //controller = new VacManController(this);
		  
		  //controller.start();
	  }
	  
	  public void startGameLoop() {
			long startTime = System.nanoTime();
			
			while(running) {
				float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
	            startTime = System.nanoTime();

	            getCurrentScreen().update(deltaTime);
	            renderer.render(deltaTime);
			}
		}
	        
	  public Screen getCurrentScreen() { return screen; }
	  
	  public void setScreen(Screen screen) { 
		  if (screen == null) throw new
		  	IllegalArgumentException("Screen must not be null");
		  
		  this.screen.pause(); 
		  this.screen.dispose(); 
		  screen.resume();
		  
		  screen.update(0); 
		  this.screen = screen; 
	  }
	  
	  public KeyboardInput getInput() { return input; }
	  
	  public VacManRenderer getRenderer() { return renderer; }
	        
	  //public FileIO getFileIO() { return fileIO; }
	  
	  //public Audio getAudio() { return audio; }
	  
	  public void close() {
		  running = false;
		  screen.pause();
		  screen.dispose();
		  
		  System.exit( 0 );
	  }
	  
	  public static void main( String[] args ) {
	    VacManGame app = new VacManGame();

	    System.exit( 0 );
	  }
	}


	/*
	 * public class VacManGame extends GraphicsProgram { private
	 * AndroidFastRenderView renderView; private Graphics graphics; private Audio
	 * audio; private Input input; private FileIO fileIO; private Screen screen;
	 * 
	 * @Override public void run() { renderView = new AndroidFastRenderView(this,
	 * frameBuffer); graphics = new AndroidGraphics(getAssets(), frameBuffer);
	 * fileIO = new AndroidFileIO(this); audio = new AndroidAudio(this); input = new
	 * AndroidInput(this, renderView, scaleX, scaleY); screen = getStartScreen();
	 * setContentView(renderView); }
	 * 
	 * public Input getInput() { return input; }
	 * 
	 * public FileIO getFileIO() { return fileIO; }
	 * 
	 * public Graphics getGraphics() { return graphics; }
	 * 
	 * public Audio getAudio() { return audio; }
	 * 
	 * 
	 * public Screen getCurrentScreen() { return screen; }
	 * 
	 * public static void main(String[] args) { new VacManGame().start(); }
	 * 
	 * }
	 */
 