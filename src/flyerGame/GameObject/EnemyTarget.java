package flyerGame.GameObject;

import java.awt.Color;
import java.awt.Graphics;

import engine.utilities.Range;
import flyerGame.EngineExtension.Resources;

public class EnemyTarget extends Target{
	
	static float speedY = 0.01f;

	
	public EnemyTarget(int healthPoint) {
		this(healthPoint, Resources.screenField.random(), -2f);
	}

	public EnemyTarget(int healthPoint, float x, float y) {
		super(healthPoint, x, y, Resources.screenFieldEx, Resources.screenFieldEx);
	}
	
	@Override
	public Range getHitBoxXRange() {
		return new Range(-0.5f, 0.5f);
	}

	@Override
	public Range getHitBoxYRange() {
		return new Range(-0.5f, 1.5f);
	}

	@Override
	public void update(long frameTime) {
		setY( getY() + speedY );
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		renderHitBox(g, Color.red);
	}

}
