package engine.game;

import engine.utilities.Cordinate2D;
import engine.utilities.Range;

public abstract class GameObject2D implements Cordinate2D{
	
	private float x,y;
	private boolean isDestory;

	private Range xRange, yRange;

	/**
	 * @param x - x-axis value of the Game Coordinate
	 * @param y - y-axis value of the Game Coordinate
	 * @param xRange - Bound of the x-axis value of the Game Coordinate
	 * @param yRange - Bound of the y-axis value of the Game Coordinate
	 */
	public GameObject2D(float x, float y, Range xRange, Range yRange) {
		super();
		this.xRange = xRange;
		this.yRange = yRange;
		this.isDestory = false;
		setX(x);
		setY(y);
	}
	
	
	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	
	public Range getXRange() {
		return xRange;
	}
	public Range getYRange() {
		return yRange;
	}
	public boolean isDestroy() {
		return isDestory;
	}

	
	public void setX(float x) {
		if(xRange != null)
			this.x = xRange.bound(x);
		else
			this.x = x;
	}
	public void setY(float y) {
		if(yRange != null)
			this.y = yRange.bound(y);
		else
			this.y = y;
	}
	
	/**
	 * When the {@link GameObject2D} is destroyed, it is 
	 * automatically removed from the {@link Logic}
	 */
	protected void destory() {
//		System.out.println(this+" is destroyed");
		this.isDestory = true;
	}
}
