package engine.game;

import engine.utilities.Cordinate2D;
import engine.utilities.Range;

public abstract class GameObject2D implements Cordinate2D{

	public static final Range DEFAULT_RANGE() { return new Range(0, 100); }
	
	private float x,y;
	private boolean isDestory;

	private Range xRange, yRange;

	
	public GameObject2D() {
		this(DEFAULT_RANGE(), DEFAULT_RANGE());
	}
	public GameObject2D(Range xRange, Range yRange) {
		this(0, 0, xRange, yRange);
	}
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
		this.x = xRange.bound(x);
	}
	public void setY(float y) {
		this.y = yRange.bound(y);
	}
	
	protected void destory() {
//		System.out.println(this+" is destoryed");
		this.isDestory = true;
	}
}
