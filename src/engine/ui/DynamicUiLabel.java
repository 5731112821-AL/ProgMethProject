package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.render.RenderLayer.Renderable;
import flyerGame.engineExtension.Resources;

public class DynamicUiLabel implements Renderable {
	
	public enum Align{
		left, right
	}
	
	public static interface GetString{
		String getString();
	}
	
	private GetString getString;
	private Font font;
	private int x, y;
	private Align align;
	private Color color;

	public DynamicUiLabel(GetString getString, int x, int y, Font font, Color color, Align align) {
		super();
		this.getString = getString;
		this.font = font;
		this.x = x;
		this.y = y;
		this.align = align;
		this.color = color;
	}
	
	protected Font getFont() {
		return this.font;
	}
	
	protected void stringReport(String str) {
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		String str = getString.getString();
		stringReport(str);
		g2d.setColor(this.color);
		Font font = getFont();
		g2d.setFont(font);
		g2d.setTransform(Resources.scaledTransform);
		if(align == Align.left){
			g2d.drawString(str, x, y);
		}
		else{
			FontMetrics metrics = g.getFontMetrics(font);
			int adv = metrics.stringWidth(str);
			g2d.drawString(str, x-adv, y);
		}
	}

}
