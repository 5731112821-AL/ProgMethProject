package engine.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.render.RenderLayer;
import engine.render.RenderLayer.Renderable;

/**
 * For handling multiple UI Elements on multiple Layers.
 * The UISystem handling both Rendering and Inputs for each UIElements.
 * @author BobbyL2k
 */
public class UISystem {

	/**
	 * An UI Element is used to pack a group of
	 * VisibleObjects and Buttons in order to create
	 * Can be added to UISystem to be automatically 
	 * managed by the UISystem (Rendering and Input handling)
	 * @author BobbyL2k
	 */
	public static abstract class UIElement {
		
		/**
		 * @return Should return an Immutable List
		 * This can be achieved by using "Collections.unmodifiableList( List <> )"
		 */
		public abstract List<Button> getButtons();
		/**
		 * @return Should return an Immutable List
		 * This can be achieved by using "Collections.unmodifiableList( List <> )"
		 */
		public abstract List<VisibleObject> getVisibleObject();
		
	}
	
	public static class CustomUIElement extends UIElement{
		
		private ArrayList<Button> buttons;
		private ArrayList<VisibleObject> visibleObjects;
		
		@Override
		public List<Button> getButtons() {
			return Collections.unmodifiableList(buttons);
		}
		
		@Override
		public List<VisibleObject> getVisibleObject() {
			return Collections.unmodifiableList(visibleObjects);
		}
		
		public void addButton(Button button){
			buttons.add(button);
			this.addVisibleObject(button);
		}
		
		public void addVisibleObject(VisibleObject object) {
			visibleObjects.add(object);
		}
		
	}
	
	private int zIndexOffset;
	private List<UIElement> uiElements;
	
	public UISystem(int zIndexOffset) {
		this(
				zIndexOffset,
				new ArrayList<UIElement>()
				);
	}

	public UISystem(int zIndexOffset, List<UIElement> uiElements) {
		super();
		this.zIndexOffset = zIndexOffset;
		this.uiElements = uiElements;
	}
	
	public RenderLayer getRenderLayer() {
		RenderLayer layer = new RenderLayer();
		for (UIElement uiElement : uiElements)
			for (Renderable renderable : uiElement.getVisibleObject())
				layer.addRenderable(renderable);
		return layer;
	}
	
}
