package flyerGame.EngineExtension;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import engine.game.Logic.Updatable;
import engine.render.InfiniteTile;
import engine.render.RenderLayer.Renderable;

public class MovingBackground implements Renderable, Updatable {

	private InfiniteTile infiniteTile;
	private float speedXPxPerMilliSec, speedYPxPerMilliSec, x = 0, y = 0;

	public MovingBackground(InfiniteTile infiniteTile, float speedXPxPerMilliSec,
			float speedYPxPerMilliSec) {
		this(infiniteTile, speedXPxPerMilliSec, speedYPxPerMilliSec, 0, 0);
	}

	public MovingBackground(InfiniteTile infiniteTile, float speedXPxPerMilliSec,
			float speedYPxPerMilliSec, float x, float y) {
		super();
		this.infiniteTile = infiniteTile;
		this.speedXPxPerMilliSec = speedXPxPerMilliSec;
		this.speedYPxPerMilliSec = speedYPxPerMilliSec;
		this.x = x;
		this.y = y;
	}

	@Override
	public void update(long frameTime) {
		x = (x + speedXPxPerMilliSec*frameTime) % infiniteTile.getBufferedImage().getWidth();
		y = (y + speedYPxPerMilliSec*frameTime) % infiniteTile.getBufferedImage().getHeight();
	}

	@Override
	public void render(Graphics g) {
		infiniteTile.draw(g, (int)x, (int)y, Resources.virtualScreenWidth, Resources.virtualScreenHeight);
	}

}
