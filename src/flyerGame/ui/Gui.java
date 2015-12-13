package flyerGame.ui;

import java.util.ArrayList;

import engine.game.InputManager;
import engine.render.RenderLayer;
import engine.ui.Button;
import engine.ui.VisibleObject;

public abstract class Gui {
	
	private RenderLayer renderLayer = new RenderLayer();
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	protected ArrayList<VisibleObject> visibleObjects = new ArrayList<VisibleObject>();
	
	public Gui() {
		setEnable(false);
		renderLayer.setFade(true);
	}
	
	protected void initButtonsAndVisibleObjects() {
		for(VisibleObject visibleObject : visibleObjects){
			renderLayer.addRenderable(visibleObject);
		}
		for(Button button : buttons){
			InputManager.addScreenMouseListener(button.getScreenMouseListener());
			renderLayer.addRenderable(button);
		}
	}

	public void setEnable(boolean set) {
		renderLayer.setVisible(set);
		for(Button button : buttons)
			button.getScreenMouseListener().setActive(set);
	}

	public RenderLayer getRenderLayer() {
		return renderLayer;
	}
}
