package programming.project.vacman;

public class Coin extends GameObject{
	private boolean collected;
	
	public Coin() {
		collected = false;
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		if(obj instanceof VacMan) {
			collected = true;
		}
	}

}
