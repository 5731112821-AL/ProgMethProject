package engine.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import engine.render.RenderLayer.Renderable;
import engine.render.SpriteMap;

public class VisibleObject implements Renderable {
	
	private int screenX,screenY,width,height,state = 0;
	private SpriteMap spriteMap;
	private boolean isVisible;
	
	public void setState(int state) {
		System.out.println(state); //TODO
		this.state = state;
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public int getWidth() {
		if(spriteMap != null)
			return spriteMap.getWidth();
		return width;
	}

	public int getHeight() {
		if(spriteMap != null)
			return spriteMap.getHeight();
		return height;
	}

	public VisibleObject(BufferedImage bufferedImage, int screenX, int screenY, int width, int height) {
		this(new SpriteMap(bufferedImage, 1, 1), screenX, screenY, width, height);
	}
	/**
	 * @param spriteMap
	 * @param screenX
	 * @param screenY
	 * @param width - is ignored if img is NOT null
	 * @param height - is ignored if img is NOT null
	 */
	public VisibleObject(SpriteMap spriteMap, int screenX, int screenY, int width, int height) {
		super();
		this.spriteMap = spriteMap;
		this.width = width;
		this.height = height;
		this.screenX = screenX;
		this.screenY = screenY;
		this.isVisible = true;
	}

	@Override
	public void render(Graphics g) {
		if(this.isVisible){
			spriteMap.render(g, screenX, screenY, null, state);
		}
	}

}
