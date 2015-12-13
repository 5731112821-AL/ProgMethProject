package engine.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.render.RenderLayer.Renderable;

public class VisibleObject implements Renderable {
	
	private int screenX,screenY,width,height;
	private BufferedImage img;
	private boolean isVisible;
	
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
		if(img != null)
			return img.getWidth();
		return width;
	}

	public int getHeight() {
		if(img != null)
			return img.getHeight();
		return height;
	}

	public VisibleObject(BufferedImage img, int screenX, int screenY, int width, int height) {
		super();
		this.img = img;
		this.width = width;
		this.height = height;
		this.screenX = screenX;
		this.screenY = screenY;
		this.isVisible = true;
	}

	@Override
	public void render(Graphics g) {
		if(this.isVisible){
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(img, null, screenX, screenY);
		}
	}

}
