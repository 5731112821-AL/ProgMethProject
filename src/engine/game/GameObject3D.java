package engine.game;

import engine.utilities.Cordinate3D;
import engine.utilities.Range;

/**
 * GameObject3D is currently unused in the Project
 * @author BobbyL2k
 */
public abstract class GameObject3D extends GameObject2D implements Cordinate3D{
	
	private float z;
	private Range zRange;

	@Override
	public float getZ() { return z; }
	
	public GameObject3D(Range xRange, Range yRange, Range zRange) {
		this(0, 0, 0, xRange, yRange, zRange);
	}
	public GameObject3D(float x, float y, float z, Range xRange, Range yRange, Range zRange) {
		super(x, y, xRange, yRange);
		this.zRange = zRange;
		setZ(z);
	}
	
	public void setZ(float z) {
		this.z = zRange.bound(z);
	}
	
}
