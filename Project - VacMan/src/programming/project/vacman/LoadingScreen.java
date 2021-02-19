package programming.project.vacman;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LoadingScreen extends Screen{

	public LoadingScreen(VacManGame game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		try {
			Assets.wallPart = ImageIO.read(new File("img/wallPart.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		game.setScreen(new GameScreen(game));
	}

	@Override
	public void render(Graphics gfx, float deltaTime) {
				
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
