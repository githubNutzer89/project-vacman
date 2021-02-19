package programming.project.vacman;

public class VacMan extends GameObject{
	private static VacMan instance;
		
	private static final float VELOCITY = 5;
	
	private VacMan () {}
	
	public static VacMan getInstance () {
	    if (VacMan.instance == null) {
	      VacMan.instance = new VacMan ();
	      instance.setDirection(Direction.LEFT);
	      instance.setVelocity(VELOCITY);
	    }
	    return VacMan.instance;
	}

	@Override
	public void onCollision(GameObject obj, CollisionEdge edge) {
		if(obj instanceof WallPart) {
			switch(edge) {
				case LEFT:
					posX = obj.posX + DIMENSION_X;
					break;
				case TOP:
					posY = obj.posY + DIMENSION_Y;
					break;
				case RIGHT:
					posX = obj.posX - DIMENSION_X;
					break;
				case BOTTOM:
					posY = obj.posY - DIMENSION_Y;
					break;
			}
		}		
	}
}
