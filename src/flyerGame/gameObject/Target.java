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
		this(health, x, y, null, null);
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
	
	@Override
	public void setX(float x) {
		Range xRange = getXRange();
		if(xRange != null && xRange.inRange(x) == false){
			leftTheScreen();
			destory();
		}
		super.setX(x);
	}
	
	@Override
	public void setY(float y) {
		Range yRange = getYRange();
		if(yRange != null && yRange.inRange(y) == false){
			leftTheScreen();
			destory();
		}
		super.setY(y);
	}
	
	protected void leftTheScreen() {}
	
	public void renderHitBox(Graphics g, Color color) {
		int 
			fromX = (int) Range.map(getX() + getHitBoxXRange().min, Resources.gameFieldX, Resources.virtualScreenGameFieldX),
			fromY = (int) Range.map(getY() + getHitBoxYRange().min, Resources.gameFieldY, Resources.virtualScreenGameFieldY);
		
		Graphics2D g2d = (Graphics2D)g;
		
		if(color != null)
			g2d.setColor(color);

		g2d.drawRect(
				fromX, 
				fromY, 
				(int) Range.scale(getHitBoxXRange().size(), Resources.gameFieldX, Resources.virtualScreenGameFieldX),
				(int) Range.scale(getHitBoxYRange().size(), Resources.gameFieldY, Resources.virtualScreenGameFieldY));
	}
	
}
