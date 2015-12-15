package engine.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import engine.game.InputManager.ScreenMouseListener;
import engine.render.SpriteMap;
import engine.utilities.Range;

/**
 * Is a {@link VisibleObject} with a mouseListener.
 * It uses a {@link MouseListener} and returns a {@link ScreenMouseListener}
 * <p>
 * A {@link Button} has 4 states, normal, hover, clicked, and disabled<br>
 * By assigning a {@link SpriteMap} with the same sequence, this class
 * automatically loads the corresponding sprite according to the current state.
 * @author BobbyL2k
 */
public class Button extends VisibleObject {
	
	private ScreenMouseListener screenMouseListener;
	
	protected boolean hover = false, enable = true, clicked = false;

	@Override
	public void setScreenX(int screenX) {
		super.setScreenX(screenX);
		screenMouseListener.setBoundX( new Range(getScreenX(), getScreenX()+getWidth()) ); 
	}
	
	@Override
	public void setScreenY(int screenY) {
		super.setScreenY(screenY);
		screenMouseListener.setBoundY( new Range(getScreenY(), getScreenY()+getHeight()) ); 
	}
	
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
	
	/**
	 * Forces hover state On/Off.
	 * @param hover
	 */
	public void setHover(boolean hover) {
		this.hover = hover;
		updateState();
	}

	/**
	 * Forces Enable/Disable button.
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
		this.screenMouseListener.setActive(enable);
		updateState();
	}

	/**
	 * Forces clicked state On/Off.
	 * @param clicked
	 */
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
		updateState();
	}

	/**
	 * @return {@link ScreenMouseListener} which correspond to the {@link Button}.
	 */
	public ScreenMouseListener getScreenMouseListener() {
		return screenMouseListener;
	}

	/**
	 * Set the zIndex of {@link ScreenMouseListener} of the {@link Button}
	 * @param zIndex
	 */
	public void setZIndex(double zIndex) {
		getScreenMouseListener().setzIndex(zIndex);
	}

	/**
	 * @return get the zIndex of {@link ScreenMouseListener} of the {@link Button}
	 */
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
				new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						setClicked(false);
						mouseListener.mouseReleased(e);
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						setClicked(true);
						mouseListener.mousePressed(e);
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						setHover(false);
						mouseListener.mouseExited(e);
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						setHover(true);
						mouseListener.mouseEntered(e);
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						mouseListener.mouseClicked(e);
					}
				},
				new Range(getScreenX(), getScreenX()+getWidth() ), 
				new Range(getScreenY(), getScreenY()+getHeight()),
				zIndex);
	}
}
