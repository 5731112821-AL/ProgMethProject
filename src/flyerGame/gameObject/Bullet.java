package flyerGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;

import engine.utilities.HitableBox2D;
import engine.utilities.Moving2D;
import engine.utilities.Range;
import flyerGame.engineExtension.Resources;

public class Bullet extends Target implements HitableBox2D, Moving2D {
	
	private float speedX, speedY;
	private Range hbX,hbY;
	private Class<? extends Target> parent;

	private static float DEFAULT_BULLET_SIZE = 0.1f;

	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY) {
		this(parent, hitPoint, x, y, speedX, speedY, DEFAULT_BULLET_SIZE);
	}
	
	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY, float hitBoxSize) {
		this(parent, hitPoint, x, y, speedX, speedY, new Range(-hitBoxSize/2, hitBoxSize/2));
	}
	
	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY, Range hitBoxRange) {
		this(parent, hitPoint, x, y, speedX, speedY, hitBoxRange, hitBoxRange);
	}

	public Bullet(Class<? extends Target> parent, int hitPoint, float x, float y, float speedX, float speedY, Range hbX, Range hbY) {
		super(hitPoint, x, y, Resources.screenFieldExX, Resources.screenFieldExY);
		this.parent = parent;
		this.speedX = speedX;
		this.speedY = speedY;
		this.hbX = hbX;
		this.hbY = hbY;
	}
	
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
		renderHitBox(g, Color.YELLOW);
	}

	public int getHitPoint() {
		return getHealthPoint();
	}
}
