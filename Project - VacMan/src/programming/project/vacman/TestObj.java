package programming.project.vacman;

public class TestObj {
	public float x = 0;
	public float y = 300;
	
	public void update(float deltaTime) {
		x += 40.0 * deltaTime;
	}
}
