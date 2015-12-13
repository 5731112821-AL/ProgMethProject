package engine.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

import engine.render.RenderLayer.Renderable;

public class SpriteMap {

	private BufferedImage image;
	private int colums, rows;
	
	public SpriteMap(BufferedImage image, int colums, int rows) {
		super();
		this.image = image;
		this.colums = colums;
		this.rows = rows;
	}

	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int index) {
		render(g, x, y, affineTransformOp, index, index / colums);
	}

	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int indexX, int indexY) {
		int 
			height = image.getHeight(),
			width = image.getWidth(),
			spriteHeight = height/rows,
			spriteWidth = width/colums;
		indexX = indexX % rows;
		indexY = indexY % colums;
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image.getSubimage(indexX*spriteWidth, indexY*spriteWidth, spriteWidth, spriteHeight), affineTransformOp, x, y);
	}

}
