package programming.project.vacman;

public class WallPart extends GameObject{
	private boolean isRed;
	
	public WallPart() {
		super.posX = 3;
		super.posY = 3;
		
		isRed = false;
	}
	
	public boolean isRed() {
		return isRed;
	}
	
	public void toogleColor() {
		isRed = !isRed;
	}
	
	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		// TODO Auto-generated method stub
		
	}

}
