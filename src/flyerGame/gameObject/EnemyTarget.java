package flyerGame.gameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.render.SpriteMap;
import engine.ui.Align;
import engine.utilities.Range;
import flyerGame.engineExtension.Resources;

/**
 * Is the class of the Enemy on screen.
 * @author L2k-nForce
 */
public class EnemyTarget extends Target{
	
	private static float speedY = Resources.gameSpeed;
	
	private static SpriteMap spriteMap = Resources.gameEnemySpriteMap;
	
//	private static AffineTransformOp transformOp;
	
	private static Range timing = new Range(466.6666666666667f, 0);
	private static Range frameCount = new Range(0, 19);
	
	static{
//		AffineTransform transform = new AffineTransform();
//		transform.scale(0.2, 0.2);
//		transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	}

	private static Range enemyField = new Range(Resources.gameFieldExY.min-3, Resources.gameFieldY.max-1);
	
	private long diffTime;
	
	/**
	 * @param healthPoint is the amount of health the {@link EnemyTarget} has.
	 * @param x X-axis position
	 * @param y X-axis position
	 * @param diffTime is the amount of time before the EnemyTarget should activate.
	 */
	public EnemyTarget(int healthPoint, float x, float y, long diffTime) {
		super(healthPoint, x, y, Resources.gameFieldExX, enemyField);
		this.diffTime = diffTime;
	}
	
	@Override
	public Range getHitBoxXRange() {
		return new Range(-0.4f, 0.4f);
	}

	@Override
	public Range getHitBoxYRange() {
		return new Range(-0.4f, 0.8f);
	}
	
	private boolean opened = false;

	public boolean isOpened() {
		return opened;
	}
	
	private int destoryTime = 8;
	private boolean isDestoryed = false;
	
	private boolean destoryedByLeavingTheScreen = false;
	
	/**
	 * @return true if the {@link EnemyTarget} isDestoryedByLeavingTheScreen
	 * (not from being shot down).
	 */
	public boolean isDestoryedByLeavingTheScreen() {
		return destoryedByLeavingTheScreen;
	}

	@Override
	protected void leftTheScreen() {
		super.leftTheScreen();
		destoryedByLeavingTheScreen = true;
	}
	
	@Override
	protected void destory() {
		isDestoryed = true;
	}

	@Override
	public void update(long frameTime) {
		setY( getY() + speedY*frameTime );
		if(!opened){
			synchronized (this) {
				this.diffTime -= frameTime;
				if(this.diffTime < 0){
					this.diffTime = 0;
					opened = true;
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		int x = (int) Range.normalize(getX(), Resources.gameFieldX, Resources.virtualScreenGameFieldX);
		int y = (int) Range.normalize(getY(), Resources.gameFieldY, Resources.virtualScreenGameFieldY);
		if(isDestoryed){
			spriteMap.render(g, x, y, null, 23-destoryTime/2, Align.center);
			destoryTime--;
			if(destoryTime == -1)
				super.destory();
		} else {
			synchronized (this) {
				int currentframe = (int) Range.map(Math.min(diffTime, timing.min), timing, frameCount);
				spriteMap.render(g, x, y, null, currentframe, Align.center);
			}
		}
		if(Resources.debugMode){
			Graphics2D g2d = ((Graphics2D)g);
			g2d.setColor(Color.red);
			g2d.drawOval(x, y, 1, 1);
			renderHitBox(g, Color.red);
		}
	}

}
