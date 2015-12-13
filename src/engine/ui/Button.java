package engine.ui;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import engine.game.InputManager.ScreenMouseListener;
import engine.render.SpriteMap;
import engine.utilities.Range;

/**
 * Button is a {@link VisibleObject} with a mouseListener.
 * It uses a {@link MiniMouseListener} and returns a {@link ScreenMouseListener}
 * @author BobbyL2k
 */
public class Button extends VisibleObject {
	
	private ScreenMouseListener screenMouseListener;
	
	protected boolean hover = false, enable = true, clicked = false;

	private void updateState(){
		if(enable)
			if(hover)
				if(clicked)
					setState(2);
				else
					setState(1);
			else
				setState(0);
		else
			setState(3);
	}
	
	public void setHover(boolean hover) {
		this.hover = hover;
		updateState();
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
		updateState();
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
		updateState();
	}

	public ScreenMouseListener getScreenMouseListener() {
		return screenMouseListener;
	}

	public void setZIndex(double zIndex) {
		getScreenMouseListener().setzIndex(zIndex);
	}
	
	public double getZIndex() {
		return getScreenMouseListener().getzIndex();
	}

	/**
	 * @param img - Image of the Button
	 * @param screenX - Top Left x-axis screen coordinate of the Button
	 * @param screenY - Top Left y-axis screen coordinate of the Button
	 * @param mouseListener - {@link MiniMouseListener} for capturing mouse events
	 * zIndex is default to 0
	 */
	public Button(BufferedImage bufferedImage, int screenX, int screenY, MouseListener mouseListener) {
		this(new SpriteMap(bufferedImage, 1, 1), screenX, screenY, mouseListener, 0);
	}
	/**
	 * @param img - Image of the Button
	 * @param screenX - Top Left x-axis screen coordinate of the Button
	 * @param screenY - Top Left y-axis screen coordinate of the Button
	 * @param mouseListener - {@link MiniMouseListener} for capturing mouse events
	 * @param zIndex - zIndex value for the {@link ScreenMouseListener}.
	 */
	public Button(BufferedImage bufferedImage, int screenX, int screenY, MouseListener mouseListener, double zIndex) {
		this(new SpriteMap(bufferedImage, 1, 1), screenX, screenY, mouseListener, zIndex);
	}

	/**
	 * @param spriteMap - SpriteMap of the Button
	 * @param screenX - Top Left x-axis screen coordinate of the Button
	 * @param screenY - Top Left y-axis screen coordinate of the Button
	 * @param mouseListener - {@link MiniMouseListener} for capturing mouse events
	 * zIndex is default to 0
	 */
	public Button(SpriteMap spriteMap, int screenX, int screenY, MouseListener mouseListener) {
		this(spriteMap, screenX, screenY, mouseListener, 0);
	}
	/**
	 * @param spriteMap - SpriteMap of the Button
	 * @param screenX - Top Left x-axis screen coordinate of the Button
	 * @param screenY - Top Left y-axis screen coordinate of the Button
	 * @param mouseListener - {@link MiniMouseListener} for capturing mouse events
	 * @param zIndex - zIndex value for the {@link ScreenMouseListener}.
	 */
	public Button(SpriteMap spriteMap, int screenX, int screenY, MouseListener mouseListener, double zIndex) {
		super(spriteMap, screenX, screenY, spriteMap.getWidth(), spriteMap.getHeight());
		screenMouseListener = new ScreenMouseListener(
				mouseListener,
				new Range(getScreenX(), getScreenX()+getWidth() ), 
				new Range(getScreenY(), getScreenY()+getHeight()),
				zIndex);
	}
}
