package engine.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import engine.render.RenderLayer.Renderable;
import engine.render.SpriteMap;

public class VisibleObject implements Renderable {
	
	private int screenX,screenY,width,height,state = 0;
	private SpriteMap spriteMap;
	private boolean isVisible;
	private Align align;
	
	public SpriteMap getSpriteMap() {
		return spriteMap;
	}

	public void setSpriteMap(SpriteMap spriteMap) {
		this.spriteMap = spriteMap;
	}

	public void setSpriteMap(BufferedImage bufferedImage) {
		setSpriteMap(new SpriteMap(bufferedImage, 1, 1));
	}

	public void setState(int state) {
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
		this(new SpriteMap(bufferedImage, 1, 1), screenX, screenY, width, height, Align.left);
	}
	public VisibleObject(BufferedImage bufferedImage, int screenX, int screenY, int width, int height, Align align) {
		this(new SpriteMap(bufferedImage, 1, 1), screenX, screenY, width, height, align);
	}

	public VisibleObject(SpriteMap spriteMap, int screenX, int screenY, int width, int height){
		this(spriteMap, screenX, screenY, width, height, Align.left);
	}
	/**
	 * @param spriteMap
	 * @param screenX
	 * @param screenY
	 * @param width - is ignored if img is NOT null
	 * @param height - is ignored if img is NOT null
	 */
	public VisibleObject(SpriteMap spriteMap, int screenX, int screenY, int width, int height, Align align) {
		super();
		this.spriteMap = spriteMap;
		this.width = width;
		this.height = height;
		this.screenX = screenX;
		this.screenY = screenY;
		this.isVisible = true;
		this.align = align;
	}

	@Override
	public void render(Graphics g) {
		if(this.isVisible){
			spriteMap.render(g, screenX, screenY, null, state, align);
		}
	}

}
