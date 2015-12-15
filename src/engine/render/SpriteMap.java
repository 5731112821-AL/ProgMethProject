package engine.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import engine.ui.Align;

/**
 * Handles rendering Sprite Map (An Image with multiple Sprite)
 * @author L2k-nForce
 */
public class SpriteMap {

	private BufferedImage image;
	private int columns, rows, height, width;

	/**
	 * @param image is a {@link BufferedImage} of the Sprite Map.
	 * @param columns is the number of columns the Sprite Map has.
	 * @param rows is the number of rows the Sprite Map has.
	 */
	public SpriteMap(BufferedImage image, int columns, int rows) {
		super();
		this.image = image;
		this.columns = columns;
		this.rows = rows;
		int 
			height = image.getHeight(),
			width = image.getWidth();
		this.height = height/rows;
		this.width = width/columns;
	}

	/**
	 * @return height of one sprite
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return width of one sprite
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Renders the nth sprite at x, y
	 * @param g is {@link Graphics} object passed from paintCompnent
	 * @param x is x-axis screen coordinate
	 * @param y is y-axis screen coordinate
	 * @param affineTransformOp is a transform operation to apply,
	 * passing in null if you do not wish to do a transformation.
	 * @param index the n-th index of the sprite. Starts from 0 to 
	 * Total_Number_Of_Sprite-1.
	 */
	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int index) {
		render(g, x, y, affineTransformOp, index, Align.left);
	}

	/**
	 * Renders the nth sprite at x, y
	 * @param g is {@link Graphics} object passed from paintCompnent
	 * @param x is x-axis screen coordinate
	 * @param y is y-axis screen coordinate
	 * @param affineTransformOp is a transform operation to apply,
	 * passing in null if you do not wish to do a transformation.
	 * @param index the n-th index of the sprite. Starts from 0 to 
	 * Total_Number_Of_Sprite-1.
	 * @param center if <b>true</b> the sprite is draw with x, y being at
	 * the <b>center</b> of the sprite instead of the top left.
	 */
	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int index, Align align) {
		if(index < 0){
			index = 0;
			System.out.println("Alert negative index input for SpriteMap");
		}
		render(g, x, y, affineTransformOp, index, index / columns, align);
	}

	/**
	 * Renders the indexX-th column, indexY-th row sprite at x, y
	 * @param g is {@link Graphics} object passed from paintCompnent
	 * @param x is x-axis screen coordinate
	 * @param y is y-axis screen coordinate
	 * @param affineTransformOp is a transform operation to apply,
	 * passing in null if you do not wish to do a transformation.
	 * @param indexX is the index of the column, starts at 0 to column-1
	 * @param indexY is the index of the row, starts at 0 to row-1
	 */
	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int indexX, int indexY) {
		render(g, x, y, affineTransformOp, indexX, indexY, Align.left);
	}

	/**
	 * Renders the indexX-th column, indexY-th row sprite at x, y
	 * @param g is {@link Graphics} object passed from paintCompnent
	 * @param x is x-axis screen coordinate
	 * @param y is y-axis screen coordinate
	 * @param affineTransformOp is a transform operation to apply,
	 * passing in null if you do not wish to do a transformation.
	 * @param indexX is the index of the column, starts at 0 to column-1
	 * @param indexY is the index of the row, starts at 0 to row-1
	 * @param center if <b>true</b> the sprite is draw with x, y being at
	 * the <b>center</b> of the sprite instead of the top left.
	 */
	public void render(Graphics g, int x, int y, AffineTransformOp affineTransformOp, int indexX, int indexY, Align align) {
		indexX = indexX % columns;
		indexY = indexY % rows;
		Graphics2D g2 = (Graphics2D)g;
		BufferedImage sprite = image.getSubimage(indexX*this.width, indexY*this.height, this.width, this.height);
		if(align == Align.left)
			g2.drawImage(sprite, affineTransformOp, x, y);
		else if(align == Align.center)
			g2.drawImage(sprite, affineTransformOp, x-this.width/2, y-this.height/2);
		else
			g2.drawImage(sprite, affineTransformOp, x-this.width, y-this.height);
	}

}
