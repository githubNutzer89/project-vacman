package programming.project.vacman;

public class VacManController {
	private VacManGame game;
	private VacManRenderer renderer;
	
	private volatile boolean running;
	
	public VacManController(VacManGame game) {
		this.game = game;
		renderer = new VacManRenderer(game);
	}

	public void start() {
		long startTime = System.nanoTime();
		running = true;
		
		while(running) {
			float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            game.getCurrentScreen().update(deltaTime);
            renderer.render(deltaTime);
		}
	}
}
