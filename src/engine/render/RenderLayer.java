package engine.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import flyerGame.engineExtension.Resources;

/**
 * Is a Thread-safe collection of {@link Renderable}, layer, with some
 * extra features, Enable/Disable, and simple Fade-black.
 * @author L2k-nForce
 */
public class RenderLayer{
	public interface Renderable {
		public void render(Graphics g);
	}
	
	private static int counterResetVale = 15;
	
	private boolean fade = false;
	private int counter = 0;
	
	/**
	 * @return whether or not the layer will fade when Enable and Disable.
	 */
	public boolean isFade() {
		return fade;
	}
	/**
	 * @param fade set true to make the layer fade-black
	 * when Enable and Disable.
	 */
	public void setFade(boolean fade) {
		this.fade = fade;
	}
	
	public RenderLayer() {
		this(new ArrayList<>());
	}
	public RenderLayer(List<Renderable> renderables) {
		super();
		this.renderables = renderables;
	}

	private boolean isVisible = false;

	/**
	 * @return true if the layer is Enable and false if the layer
	 * is disable.
	 */
	public boolean isVisible() {
		return isVisible;
	}
	/**
	 * @param isVisible use to enable and disable the layer
	 */
	public void setVisible(boolean isVisible) {
		if(this.isVisible != isVisible){
			this.counter = counterResetVale;
			this.isVisible = isVisible;
		}
	}
	
	private List<Renderable> renderables;

	/**
	 * Adds a {@link Renderable} to the layer.
	 * This method is Thread-safe
	 * @param renderable to add
	 */
	public void addRenderable(Renderable renderable) {
		synchronized (renderables) {
			this.renderables.add(renderable);
		}
	}
	/**
	 * Removes a {@link Renderable} to the layer.
	 * This method is Thread-safe
	 * @param renderable to remove
	 */
	public void removeRenderable(Renderable renderable) {
		synchronized (renderables) {
			this.renderables.remove(renderable);
		}
	}

	/**
	 * Render all of the {@link Renderable} object in the layer<br>
	 * This method is intended to be call by {@link GamePanel}'s paintComponent
	 * @param g is {@link Graphics} object from paintComponent's parameter
	 */
	void renderAll(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(counter > 0){
			synchronized (renderables){
				for(Renderable renderable : renderables)
					renderable.render(g);
			}
			if(this.isVisible){
				g2.setColor(new Color(0, 0, 0, (float)counter/counterResetVale));
			}else{
				g2.setColor(new Color(0, 0, 0, 1f-(float)counter/counterResetVale));
			}
//			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
//			g2.setComposite(composite);
			g2.fillRect(0, 0, Resources.virtualScreenWidth, Resources.virtualScreenHeight);
			counter--;
		}
		else if(this.isVisible)
			synchronized (renderables){
				for(Renderable renderable : renderables)
					renderable.render(g);
			}
	}
}
