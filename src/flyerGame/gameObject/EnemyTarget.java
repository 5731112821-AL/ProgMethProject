package flyerGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;

import engine.utilities.Range;
import flyerGame.engineExtension.Resources;

public class EnemyTarget extends Target{
	
	static float speedY = 0.001f;

	public EnemyTarget(int healthPoint, float x, float y) {
		super(healthPoint, x, y, Resources.screenFieldExX, Resources.screenFieldExY);
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
		setY( getY() + speedY*frameTime );
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		renderHitBox(g, Color.red);
	}

}
