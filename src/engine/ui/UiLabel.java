package engine.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.render.RenderLayer.Renderable;
import flyerGame.engineExtension.Resources;

public class UiLabel implements Renderable {

	private String str;
	private int x, y;

	public UiLabel(String str, int x, int y) {
		super();
		this.str = str;
		this.x = x;
		this.y = y;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setFont(Resources.standardFont);
		g2d.drawString(str, x, y);
	}

}
