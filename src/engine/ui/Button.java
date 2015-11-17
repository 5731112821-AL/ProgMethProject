package engine.ui;

import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import engine.game.InputManager.CustomMouseListener;
import engine.utilities.Range;

public class Button extends VisibleObject {
	
	private CustomMouseListener customMouseListener;

	public CustomMouseListener getCustomMouseListener() {
		return customMouseListener;
	}

	public Button(BufferedImage img, int screenX, int screenY, MouseListener mouseListener, int zIndex) {
		super(img, screenX, screenY);
		customMouseListener = new CustomMouseListener(
				mouseListener,
				new Range(getScreenX(), getScreenX()+getWidth() ), 
				new Range(getScreenY(), getScreenY()+getHeight()),
				zIndex);
	}
}
