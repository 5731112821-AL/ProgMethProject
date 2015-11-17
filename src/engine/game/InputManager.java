package engine.game;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;

import engine.utilities.Range;

public class InputManager {
	private InputManager() {}
	
	public static KeyListener keyListener = new InputKeyListener();
	public static MouseListener mouseListener = new ScreenMouseListener();
	public static MouseMotionListener mouseMotionListener = new InputMouseMotionListener();
	
	public static final int 
		UpArrowKey    = 38,
		DownArrowKey  = 40,
		LeftArrowKey  = 37,
		RightArrowKey = 39;
	
	public static class CustomMouseListener{
		MouseListener mouseListener;
		Range boundX,boundY;
		int zIndex;
		boolean isActive;
		
		public CustomMouseListener(MouseListener mouseListener, Range boundX, Range boundY, int zIndex) {
			this.mouseListener = mouseListener;
			this.boundX = boundX;
			this.boundY = boundY;
			this.zIndex = zIndex;
		}
	}

	public static synchronized boolean isKeyActive(int key) {
		return keyActive[key];
	}

	public static boolean isMouseOnScreen() {
		return mouseOnScreen;
	}
	
	public static void addComponent(Component comp){
		comp.addKeyListener(keyListener);
		comp.addMouseListener(mouseListener);
		comp.addMouseMotionListener(mouseMotionListener);
	}
	
	private static ArrayList<CustomMouseListener> customMouseListeners = new ArrayList<CustomMouseListener>();
	
	public static void addCustomMouseListener(CustomMouseListener customMouseListener){
		customMouseListeners.add(customMouseListener);
	}
	
	private static synchronized void setKeyActive(int key, boolean active) {
		if(active != InputManager.keyActive[key])
			System.out.println("Key "+key+" is "+(active?"active":"inactive"));
		InputManager.keyActive[key] = active;
	}

	private static boolean[] keyActive = new boolean[256];
	private static boolean mouseOnScreen = false;

	private static void setMouseOnScreen(boolean mouseOnScreen) {
		InputManager.mouseOnScreen = mouseOnScreen;
	}
	
	private static class ScreenMouseListener implements MouseListener {

		private static void updateCustomMouseListeners(){
			customMouseListeners.sort(new Comparator<CustomMouseListener>() {
				@Override
				public int compare(CustomMouseListener o1, CustomMouseListener o2) {
					return o1.zIndex - o2.zIndex;
				}
			});
		}
		
		private static MouseListener findMouseListenerAt(int x, int y) {
			updateCustomMouseListeners();
			for(int c=customMouseListeners.size()-1; c>=0; c++){
				CustomMouseListener customMouseListener = customMouseListeners.get(c);
				if(customMouseListener.isActive==true && customMouseListener.boundX.inRange(x) && customMouseListener.boundY.inRange(y)){
					return customMouseListener.mouseListener;
				}
			}
			return null;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getXOnScreen(),
				y = e.getYOnScreen();
			System.out.println("Mouse Clicked At ("+x+","+y+")");
			MouseListener ml = findMouseListenerAt(x, y);
			if(ml!=null){
				ml.mouseClicked(e);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getXOnScreen(),
				y = e.getYOnScreen();
			MouseListener ml = findMouseListenerAt(x, y);
			if(ml!=null){
				ml.mousePressed(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int x = e.getXOnScreen(),
				y = e.getYOnScreen();
			MouseListener ml = findMouseListenerAt(x, y);
			if(ml!=null){
				ml.mouseReleased(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setMouseOnScreen(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setMouseOnScreen(false);
		}
		
	}
	
	private static Point mouseLocation;
	
	public static Point getMouseLocation(){
		return mouseLocation;
	}
	
	private static class InputMouseMotionListener implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseLocation = e.getPoint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseLocation = e.getPoint();
		}
	
	}
	
	private static class InputKeyListener implements KeyListener{
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 
			// Do nothing 
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			InputManager.setKeyActive(e.getKeyCode(), false);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			InputManager.setKeyActive(e.getKeyCode(), true);
		}
	}
}
