package flyerGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.utilities.HitableBox2D;
import engine.utilities.Moving2D;
import engine.utilities.Range;
import flyerGame.engineExtension.Resources;

/**
 * Is a {@link Moving2D} object that has a {@link HitableBox2D}
 * @author L2k-nForce
 */
public class Bullet extends Target implements HitableBox2D, Moving2D {
	
	private float speedX, speedY;
	private Range hbX,hbY;
	private Class<? extends Target> parent;

	private static float DEFAULT_BULLET_SIZE = 0.1f;


	/**
	 * A {@link Bullet} will not Hit its parent<br>
	 * The hitBox size in default to DEFAULT_BULLET_SIZE = 0.1f
	 * @param parent
	 * @param hitPoint the damage inflicted when a {@link Target} is hit
	 * @param x X-axis position
	 * @param y X-axis position
	 * @param speedX rate of x changing per millisecond
	 * @param speedY rate of y changing per millisecond
	 */
	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY) {
		this(parent, hitPoint, x, y, speedX, speedY, DEFAULT_BULLET_SIZE);
	}

	/**
	 * A {@link Bullet} will not Hit its parent
	 * @param parent
	 * @param hitPoint the damage inflicted when a {@link Target} is hit
	 * @param x X-axis position
	 * @param y X-axis position
	 * @param speedX rate of x changing per millisecond
	 * @param speedY rate of y changing per millisecond
	 * @param hitBoxSize size of the hitBox
	 */
	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY, float hitBoxSize) {
		this(parent, hitPoint, x, y, speedX, speedY, new Range(-hitBoxSize/2, hitBoxSize/2));
	}

	/**
	 * A {@link Bullet} will not Hit its parent
	 * @param parent
	 * @param hitPoint the damage inflicted when a {@link Target} is hit
	 * @param x X-axis position
	 * @param y X-axis position
	 * @param speedX rate of x changing per millisecond
	 * @param speedY rate of y changing per millisecond
	 * @param hitBoxRange X & Y -axis Range of the bullet's HitBox
	 */
	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY, Range hitBoxRange) {
		this(parent, hitPoint, x, y, speedX, speedY, hitBoxRange, hitBoxRange);
	}

	/**
	 * A {@link Bullet} will not Hit its parent
	 * @param parent
	 * @param hitPoint the damage inflicted when a {@link Target} is hit
	 * @param x X-axis position
	 * @param y X-axis position
	 * @param speedX rate of x changing per millisecond
	 * @param speedY rate of y changing per millisecond
	 * @param hbX X-axis Range of the bullet's HitBox
	 * @param hbY Y-axis Range of the bullet's HitBox
	 */
	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY, Range hbX, Range hbY) {
		super(hitPoint, x, y, Resources.gameFieldExX, Resources.gameFieldExY);
		this.parent = parent;
		this.speedX = speedX;
		this.speedY = speedY;
		this.hbX = hbX;
		this.hbY = hbY;
	}
	
	/**
	 * Reduces the Target health with its hitPoint
	 * and destroys itself.
	 * @param obj
	 */
	public void hit(Target obj){
		System.out.println("Bullet hitting "+obj);
		obj.damage(this.getHitPoint());
		this.destory();
	}

	@Override
	public boolean isHitting(HitableBox2D obj) {
		return 
				obj.getClass() != this.parent &&
				HitableBox2D.super.isHitting(obj);
	}
	
	@Override
	public void update(long frameTime) {
		Moving2D.super.update(frameTime);
	}

	@Override
	public Range getHitBoxXRange() {
		return hbX;
	}

	@Override
	public Range getHitBoxYRange() {
		return hbY;
	}

	@Override
	public float getSpeedX() {
		return speedX;
	}

	@Override
	public float getSpeedY() {
		return speedY;
	}
	
	@Override
	public void setX(float x) {
		if(getXRange().inRange(x))
			super.setX(x);
		else
			destory();
	}
	@Override
	public void setY(float y) {
		if(getYRange().inRange(y))
			super.setY(y);
		else
			destory();
	}
	
	@Override
	public void render(Graphics g) {
		int x = (int) Range.normalize(getX(), Resources.gameFieldX, Resources.virtualScreenGameFieldX);
		int y = (int) Range.normalize(getY(), Resources.gameFieldY, Resources.virtualScreenGameFieldY);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(Resources.raySprite, null, x-Resources.raySprite.getWidth()/2, y);
		if(Resources.debugMode)
			renderHitBox(g, Color.YELLOW);
	}

	/**
	 * @return the HitPoint of the {@link Bullet}
	 * (or How much damage will the Bullet inflict)
	 * <p>
	 * the HitPoint System is implemented with the
	 * HealthPoint from its super class, {@link Target}.
	 */
	public int getHitPoint() {
		return getHealthPoint();
	}
}
