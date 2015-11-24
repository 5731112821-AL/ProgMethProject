package engine.ui;

import java.awt.image.BufferedImage;

import engine.game.InputManager.ScreenMouseListener;
import engine.game.InputManager.MiniMouseListener;
import engine.utilities.Range;

public class Button extends VisibleObject {
	
	private ScreenMouseListener screenMouseListener;

	public ScreenMouseListener getCustomMouseListener() {
		return screenMouseListener;
	}

	public Button(BufferedImage img, int screenX, int screenY, MiniMouseListener mouseListener) {
		this(img, screenX, screenY, mouseListener, 0);
	}
	
	public Button(BufferedImage img, int screenX, int screenY, MiniMouseListener mouseListener, int zIndex) {
		super(img, screenX, screenY, img.getWidth(), img.getHeight());
		screenMouseListener = new ScreenMouseListener(
				mouseListener,
				new Range(getScreenX(), getScreenX()+getWidth() ), 
				new Range(getScreenY(), getScreenY()+getHeight()),
				zIndex);
	}
}
