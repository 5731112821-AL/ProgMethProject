package flyerGame.GameObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import engine.game.InputManager;
import engine.utilities.Range;
import flyerGame.EngineExtension.GameLogic;
import flyerGame.EngineExtension.Resources;
import flyerGame.Factory.BulletFactory;

public class Player extends FiringTarget{
	
	private float width,height;
	private static long FIRE_COOL_DOWN = 100;
	private BulletFactory bulletFactory;
	
	public Player(GameLogic gameLogic) {
		this(100, 3, 5, gameLogic);
	}

	public Player(int health, float x, float y, GameLogic gameLogic) {
		super(health, x, y, gameLogic);//, Resources.gameField, Resources.gameField);
		this.width = 1;
		this.height = 1;
		this.bulletFactory = new BulletFactory(this.getClass()) {
			@Override
			public Bullet createBullet() {
//				System.out.println("New Bullet created");
				Bullet bullet = new Bullet(this.getParentClass(), 1, getX(), getY(), 0, -0.1f);
				return bullet;
			}
		};
	}

	@Override
	public void render(Graphics g) {
//		System.out.println("Rendering player");
//		int 
//			x = 	  Resources.normalizeToScreen(getX()), 
//			y = 	  Resources.normalizeToScreen(getY());
//		int 
//			width  =  Resources.normalizeToScreen(this.width),
//			height =  Resources.normalizeToScreen(this.height);
//		
//		Graphics2D g2d = (Graphics2D)g;
//		g2d.setColor(Color.BLUE);
//		g2d.drawRect((int)x - width/2, (int)y - height/2, width, height);
		renderHitBox(g, Color.BLUE);
	}

	@Override
	public void update(long frameTime) {
		Point p = InputManager.getMouseLocation();
		if(p!=null){
			this.setX(Resources.normalizeToGame(p.x));
			this.setY(Resources.normalizeToGame(p.y));
		}
//		if(InputManager.isKeyActive(InputManager.LeftArrowKey)){
//			setX(getX() - frameTime/200f);
//		}
//		if(InputManager.isKeyActive(InputManager.RightArrowKey)){
//			setX(getX() + frameTime/200f);
//		}
//		if(InputManager.isKeyActive(InputManager.UpArrowKey)){
//			setY(getY() - frameTime/200f);
//		}
//		if(InputManager.isKeyActive(InputManager.DownArrowKey)){
//			setY(getY() + frameTime/200f);
//		}
//		System.out.println("X "+getX());
		
		if(InputManager.isKeyActive(32))
			fire(frameTime, bulletFactory);
	}

	@Override
	public Range getHitBoxXRange() {
		return new Range(-width/2, width/2);
	}

	@Override
	public Range getHitBoxYRange() {
		return new Range(-height/2, height/2);
	}

	@Override
	protected long getFireCoolDownTime() {
		return FIRE_COOL_DOWN;
	}

}
