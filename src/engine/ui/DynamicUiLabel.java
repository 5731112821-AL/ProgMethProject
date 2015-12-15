package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.security.AllPermission;

import engine.render.RenderLayer.Renderable;
import flyerGame.engineExtension.Resources;

/**
 * Is a {@link UiLabel} where the string currently displayed,
 * and the {@link Font} currently can be swap on-the-fly as a
 * frame-per-frame render basis.
 * <p>
 * For dynamic string see {@link GetString}.<br>
 * Dynamic {@link Font} can be achieved by overriding getFont method.
 * @author L2k-nForce
 */
public class DynamicUiLabel implements Renderable {
	
	/**
	 * An anonymous function that returns a {@link String} when called.
	 * @author L2k-nForce
	 */
	public static interface GetString{
		String getString();
	}
	
	private GetString getString;
	private Font font;
	private int x, y;
	private Align align;
	private Color color;

	/**
	 * @param getString is an anonymous function that returns a {@link String}
	 * @param x is x-axis coordinate to render the string
	 * @param y is y-axis coordinate to render the string
	 * @param font is the font used to display the string,
	 * can be swap later if need to. Can be passed in as null
	 * swapping is used.
	 * @param color is the color used to render the string.
	 * @param align is the alignment of the string. Check {@link Align} for more info.
	 */
	public DynamicUiLabel(GetString getString, int x, int y, Font font, Color color, Align align) {
		super();
		this.getString = getString;
		this.font = font;
		this.x = x;
		this.y = y;
		this.align = align;
		this.color = color;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Is the method that can be override to provide
	 * the render method a new {@link Font} as a Frame-per-frame
	 * basis. The getFont is called after the stringReport method.
	 * @return a Font to be used by render method 
	 */
	protected Font getFont() {
		return this.font;
	}
	
	/**
	 * @param str is the string that is about to be drawn.
	 */
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
		
		FontMetrics metrics = g.getFontMetrics(font);
		int adv = metrics.stringWidth(str);
		
		if(align == Align.left){
			g2d.drawString(str, x, y);
		}
		else if(align == Align.right){
			g2d.drawString(str, x-adv, y);
		}
		else {
			g2d.drawString(str, x-adv/2, y);
		}
	}

}
