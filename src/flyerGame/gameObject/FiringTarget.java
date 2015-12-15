package flyerGame.gameObject;

import engine.utilities.Range;
import flyerGame.engineExtension.GameLogic;
import flyerGame.factory.BulletFactory;

/**
 * Is an {@link Target} that can fire {@link Bullet}
 * @author L2k-nForce
 */
public abstract class FiringTarget extends Target {
	
	private GameLogic gameLogic;
	
	private long fireCoolDownValue;
	
	protected abstract long getFireCoolDownTime();
	
	/**
	 * health, x, y, xRange, yRange is from {@link Target}
	 * @param health 
	 * @param x
	 * @param y
	 * @param xRange
	 * @param yRange
	 * @param gameLogic is the {@link GameLogic} object that will be 
	 * receiving and handling the bullet created.
	 */
	public FiringTarget(int health, float x, float y, Range xRange, Range yRange, GameLogic gameLogic) {
		super(health, x, y, xRange, yRange);
		this.gameLogic = gameLogic;
	}

	/**
	 * health, x, y is from {@link Target}
	 * @param health 
	 * @param x
	 * @param y
	 * @param gameLogic is the {@link GameLogic} object that will be 
	 * receiving and handling the bullet created.
	 */
	public FiringTarget(int health, float x, float y, GameLogic gameLogic) {
		super(health, x, y);
		this.gameLogic = gameLogic;
	}

	protected void fire(long frameTime, BulletFactory bulletFactory){
		fireCoolDownValue -= frameTime;
		if(fireCoolDownValue < 0){
			fireCoolDownValue = getFireCoolDownTime();
			System.out.println("FIRE");
			gameLogic.addBullet(bulletFactory.createBullet());
		}
	}

}