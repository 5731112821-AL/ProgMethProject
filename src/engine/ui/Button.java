package engine.ui;

import java.awt.image.BufferedImage;

import engine.game.InputManager.ScreenMouseListener;
import engine.game.InputManager.MiniMouseListener;
import engine.utilities.Range;

/**
 * Button is a {@link VisibleObject} with a mouseListener.
 * It uses a {@link MiniMouseListener} and returns a {@link ScreenMouseListener}
 * @author BobbyL2k
 */
public class Button extends VisibleObject {
	
	private ScreenMouseListener screenMouseListener;

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
	public Button(BufferedImage img, int screenX, int screenY, MiniMouseListener mouseListener) {
		this(img, screenX, screenY, mouseListener, 0);
	}

	/**
	 * @param img - Image of the Button
	 * @param screenX - Top Left x-axis screen coordinate of the Button
	 * @param screenY - Top Left y-axis screen coordinate of the Button
	 * @param mouseListener - {@link MiniMouseListener} for capturing mouse events
	 * @param zIndex - zIndex value for the {@link ScreenMouseListener}.
	 */
	public Button(BufferedImage img, int screenX, int screenY, MiniMouseListener mouseListener, int zIndex) {
		super(img, screenX, screenY, img.getWidth(), img.getHeight());
		screenMouseListener = new ScreenMouseListener(
				mouseListener,
				new Range(getScreenX(), getScreenX()+getWidth() ), 
				new Range(getScreenY(), getScreenY()+getHeight()),
				zIndex);
	}
}
