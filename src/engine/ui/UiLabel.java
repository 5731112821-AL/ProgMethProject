package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.render.RenderLayer.Renderable;
import flyerGame.engineExtension.Resources;

public class UiLabel implements Renderable {

	private String str;
	private int x, y;
	private Font font;
	private Color color;

	public UiLabel(String str, int x, int y, Font font) {
		this(str, x, y, font, Resources.fontColor);
	}
	public UiLabel(String str, int x, int y, Font font, Color color) {
		super();
		this.str = str;
		this.font = font;
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public void setString(String str) {
		this.str = str;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(this.color);
		g2d.setFont(this.font);
		g2d.setTransform(Resources.scaledTransform);
		g2d.drawString(str, x, y);
	}

}
