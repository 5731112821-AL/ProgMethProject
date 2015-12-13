package flyerGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.game.GameObject2D;
import engine.game.Logic.Updatable;
import engine.render.RenderLayer.Renderable;
import engine.utilities.HitableBox2D;
import engine.utilities.Range;
import flyerGame.engineExtension.Resources;

public abstract class Target extends GameObject2D implements HitableBox2D, Updatable, Renderable {
	
	private int healthPoint;

	public Target(int health, float x, float y) {
		this(health, x, y, Resources.screenFieldX, Resources.screenFieldY);
	}

	public Target(int health, float x, float y, Range xRange, Range yRange) {
		super(x, y, xRange, yRange);
		this.healthPoint = health;
	}
	
	public int getHealthPoint(){
		return healthPoint;
	}

	public void damage(int hitPoint) {
		System.out.println(this + " is being hit");
		healthPoint -= hitPoint;
		if(healthPoint <= 0)
			this.destory();
	}
	
	public void renderHitBox(Graphics g, Color color) {
		int 
			fromX = (int) Range.map(getX() + getHitBoxXRange().min, Resources.screenFieldX, Resources.virtualScreenFieldX),
			fromY = (int) Range.map(getY() + getHitBoxYRange().min, Resources.screenFieldY, Resources.virtualScreenFieldY);
		
		Graphics2D g2d = (Graphics2D)g;
		
		if(color != null)
			g2d.setColor(color);

		g2d.drawRect(
				fromX, 
				fromY, 
				(int) Range.map(getHitBoxXRange().size(), Resources.screenFieldX, Resources.virtualScreenFieldX),
				(int) Range.map(getHitBoxYRange().size(), Resources.screenFieldY, Resources.virtualScreenFieldY));
	}
	
}
