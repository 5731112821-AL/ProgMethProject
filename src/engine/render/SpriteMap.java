package engine.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import flyerGame.engineExtension.Resources;

public class SpriteMap {

	private BufferedImage image;
	private int colums, rows, height, width;

	public SpriteMap(BufferedImage image, int colums, int rows) {
		super();
		this.image = image;
		this.colums = colums;
		this.rows = rows;
		int 
			height = image.getHeight(),
			width = image.getWidth();
		this.height = height/rows;
		this.width = width/colums;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int index) {
		render(g, x, y, affineTransformOp, index, false);
	}

	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int index, boolean center) {
		if(index < 0){
			index = 0;
			System.out.println("Alert negative index input for SpriteMap");
		}
		render(g, x, y, affineTransformOp, index, index / colums, center);
	}

	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int indexX, int indexY) {
		render(g, x, y, affineTransformOp, indexX, indexY, false);
	}
	
	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int indexX, int indexY, boolean center) {
		indexX = indexX % colums;
		indexY = indexY % rows;
		Graphics2D g2 = (Graphics2D)g;
		BufferedImage sprite = image.getSubimage(indexX*this.width, indexY*this.height, this.width, this.height);
		if(center)
			g2.drawImage(sprite, affineTransformOp, x-width/2, y-height/2);
		else
			g2.drawImage(sprite, affineTransformOp, x, y);
	}

}
