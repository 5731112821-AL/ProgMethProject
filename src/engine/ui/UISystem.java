package engine.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import engine.game.InputManager.ScreenMouseListener;
import engine.render.RenderLayer;
import engine.render.RenderLayer.Renderable;

/**
 * @deprecated <br>
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
		 * This can be achieved by using "Collections.unmodifiableList( List <Button> )"
		 */
		public abstract List<Button> getButtons();
		/**
		 * @return Should return an Immutable List
		 * This can be achieved by using "Collections.unmodifiableList( List <VisibleObject> )"
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
		
		/**
		 * <b>Do NOT</b> add {@link Button} after adding this UIElement in to the {@link UISystem}
		 * @param button
		 */
		public void addButton(Button button){
			buttons.add(button);
			this.addVisibleObject(button);
		}

		/**
		 * <b>Do NOT</b> add {@link VisibleObject} after adding this UIElement in to the {@link UISystem}
		 * @param button
		 */
		public void addVisibleObject(VisibleObject object) {
			visibleObjects.add(object);
		}
		
	}
	
	private double zIndexOffset;
	private List<UIElement> uiElements;
	
	public UISystem(double zIndexOffset) {
		this(
				zIndexOffset,
				new ArrayList<UIElement>()
				);
	}

	public UISystem(double zIndexOffset, List<UIElement> uiElements) {
		super();
		this.zIndexOffset = zIndexOffset;
		this.uiElements = uiElements;
		processAllUIElements();
	}
	
	public RenderLayer getRenderLayer() {
		RenderLayer layer = new RenderLayer();
		for (UIElement uiElement : uiElements)
			for (Renderable renderable : uiElement.getVisibleObject())
				layer.addRenderable(renderable);
		return layer;
	}
	
	public List<ScreenMouseListener> getScreenMouseListeners() {
		List<ScreenMouseListener> screenMouseListeners = new ArrayList<ScreenMouseListener>();
		for(UIElement uiElement : uiElements){
			for(Button button : uiElement.getButtons()){
				screenMouseListeners.add(button.getScreenMouseListener());
			}
		}
		return screenMouseListeners;
	}

	public void addUIElement(UIElement uiElement) {
		processUIElement(uiElement, zIndexOffset + uiElements.size());
		uiElements.add(uiElement);
	}

	private static void processUIElement(UIElement element, double zIndexoffset){
		List<Button> buttons = element.getButtons();
		buttons.sort(new Comparator<Button>() {
			@Override
			public int compare(Button o1, Button o2) {
				return Double.compare(o1.getZIndex(), o2.getZIndex());
			}
		});
		double counter = 0, total = buttons.size();
		for(Button button : buttons){
			button.setZIndex(zIndexoffset + counter/total);
			counter++;
		}
	}
	
	private void processAllUIElements(){
		double offset = zIndexOffset;
		for(UIElement element : uiElements){
			processUIElement(element, offset);
			offset++;
		}
	}
}
