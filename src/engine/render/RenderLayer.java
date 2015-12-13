package engine.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import flyerGame.engineExtension.Resources;

public class RenderLayer{
	public interface Renderable {
		public default void render(Graphics g){
			System.out.println(Thread.currentThread().getName());
		}
	}
	
	private static int counterResetVale = 15;
	
	private boolean fade = false;
	private int counter = 0;
	
	public boolean isFade() {
		return fade;
	}
	public void setFade(boolean fade) {
		this.fade = fade;
	}
	
	public RenderLayer() {
		this(new ArrayList<>());
	}
	public RenderLayer(ArrayList<Renderable> renderables) {
		super();
		this.renderables = renderables;
	}

	private boolean isVisible = false;

	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		if(this.isVisible != isVisible){
			this.counter = counterResetVale;
			this.isVisible = isVisible;
		}
	}
	
	private ArrayList<Renderable> renderables;
	
	public ArrayList<Renderable> getRenderables() {
		return renderables;
	}
	public void addRenderable(Renderable renderable) {
		this.renderables.add(renderable);
	}
	public void removeRenderable(Renderable renderable) {
		this.renderables.remove(renderable);
	}

	void renderAll(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(counter > 0){
			for(Renderable renderable : renderables)
				renderable.render(g);
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
			for(Renderable renderable : renderables)
				renderable.render(g);
	}
}
