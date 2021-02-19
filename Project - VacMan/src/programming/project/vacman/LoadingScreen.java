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
			Assets.wallPartRed = ImageIO.read(new File("img/wallPartRed.png"));
			Assets.mainMenu = ImageIO.read(new File("img/main_menu.png"));
			Assets.lvl1 = ImageIO.read(new File("img/lvl1.png"));
			Assets.coin = ImageIO.read(new File("img/coin.png"));
			Assets.vacman_right = ImageIO.read(new File("img/vacman_right.png"));
			Assets.vacman_left = ImageIO.read(new File("img/vacman_left.png"));
			Assets.vacman_up = ImageIO.read(new File("img/vacman_up.png"));
			Assets.vacman_down = ImageIO.read(new File("img/vacman_down.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		game.setScreen(new MenuScreen(game));
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
