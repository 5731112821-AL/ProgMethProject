package engine.render;

import java.awt.Graphics;
import java.util.ArrayList;

public class RenderLayer{
	public interface Renderable {
		public default void render(Graphics g){
			System.out.println(Thread.currentThread().getName());
		}
	}
	
	public RenderLayer() {
		this(new ArrayList<>());
	}
	public RenderLayer(ArrayList<Renderable> renderables) {
		super();
		this.renderables = renderables;
		this.isVisible = true;
	}

	private boolean isVisible;

	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
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
		if(this.isVisible)
			for(Renderable renderable : renderables)
				renderable.render(g);
	}
}
