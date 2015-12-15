package flyerGame.ui;

import java.util.ArrayList;

import engine.game.InputManager;
import engine.render.RenderLayer;
import engine.render.RenderLayer.Renderable;
import engine.ui.Button;

public abstract class Gui {
	
	private RenderLayer renderLayer = new RenderLayer();
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	protected ArrayList<Renderable> renderablesToAdd = new ArrayList<Renderable>();
	
	public Gui() {
	}
	
	protected void postConstrutorConfig() {
		for(Renderable readable: renderablesToAdd){
			renderLayer.addRenderable(readable);
		}
		for(Button button : buttons){
			InputManager.addScreenMouseListener(button.getScreenMouseListener());
			renderLayer.addRenderable(button);
		}
		setEnable(false);
		renderLayer.setFade(true);
	}

	public void setEnable(boolean set) {
		renderLayer.setVisible(set);
		for(Button button : buttons)
			button.getScreenMouseListener().setActive(set);
	}
	public boolean isEnable() {
		return renderLayer.isVisible();
	}

	public RenderLayer getRenderLayer() {
		return renderLayer;
	}

	public abstract void updateRenderableStates();
}
