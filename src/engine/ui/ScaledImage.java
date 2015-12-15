package engine.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import engine.render.RenderLayer.Renderable;

public class ScaledImage implements Renderable {
	
	private BufferedImage image;
	private int x, y, /*width,*/ height;

	public ScaledImage(BufferedImage image, int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
//		this.width = width;
		this.height = height;
		setImage(image);
	}
	
	public void setImage(BufferedImage image) {
		if(image != null && (image.getWidth()-image.getHeight()*4/3) > 0){
			int x = (image.getWidth()-image.getHeight()*4/3)/2;
			this.image = image.getSubimage(x, 0, image.getHeight()*4/3, image.getHeight());
		}
		else
			this.image = image;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if(image != null){
			AffineTransform transform = new AffineTransform();
			float scale = (float)height/image.getHeight();
			transform.scale(scale, scale);
			BufferedImageOp bufferedImageOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
			g2d.drawImage(image, bufferedImageOp, x, y);
		}
//		else{
//			g2d.setColor(Color.GRAY);
//			g2d.fillRect(x, y, width, height);
//		}
	}

}
