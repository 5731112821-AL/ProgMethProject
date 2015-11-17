package flyerGame.GameObject;

import engine.utilities.Range;
import flyerGame.EngineExtension.GameLogic;
import flyerGame.Factory.BulletFactory;

public abstract class FiringTarget extends Target {
	
	private GameLogic gameLogic;
	
	private long fireCoolDownValue;
	
	protected abstract long getFireCoolDownTime();
	
	public FiringTarget(int health, float x, float y, Range xRange, Range yRange, GameLogic gameLogic) {
		super(health, x, y, xRange, yRange);
		this.gameLogic = gameLogic;
	}

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