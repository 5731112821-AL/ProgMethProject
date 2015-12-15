package engine.game;

import engine.utilities.Cordinate2D;
import engine.utilities.Range;

/**
 * Is a foundation class for any game object. GameObject2D implements
 * {@link Cordinate2D}, this is meant to keep "Game Coordinates", which is
 * the coordinate value of the object in "Game Space" as oppose to "Screen Space".
 * It also has {@link destroy()}, this is used to keep track of the game objects
 * inside of {@link Logic}
 * @author L2k-nForce
 */
public abstract class GameObject2D implements Cordinate2D{
	
	private float x,y;
	private boolean isDestory;

	private Range xRange, yRange;

	/**
	 * @param x is x-axis value of the Game Coordinate
	 * @param y is y-axis value of the Game Coordinate
	 * @param xRange Bound of the x-axis value of the Game 
	 * Coordinate in which the {@link x} can be set. If left {@code null}
	 * the range is unbound.
	 * @param yRange Same as xRange but for the y coordinate.
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
	 * Destroys the {@link GameObject2D}.
	 * When the object is destroyed, it is 
	 * automatically removed from the {@link Logic}
	 */
	protected void destory() {
//		System.out.println(this+" is destroyed");
		this.isDestory = true;
	}
}
