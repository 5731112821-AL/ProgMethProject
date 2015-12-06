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
	
	public static final KeyListener keyListener = new InputKeyListener();
	public static final MouseListener mouseListener = new ManagerMouseListener();
	public static final MouseMotionListener mouseMotionListener = new InputMouseMotionListener();
	
	public static final int 
		UpArrowKey    = 38,
		DownArrowKey  = 40,
		LeftArrowKey  = 37,
		RightArrowKey = 39;

	/**
	 * {@link MiniMouseListener} is a reduced-complexity of {@link MouseListener} in java.awt
	 * @author BobbyL2k
	 */
	public static interface MiniMouseListener{
		public void mouseClicked(MouseEvent e);
		public void mousePressed(MouseEvent e);
		public void mouseReleased(MouseEvent e);
	}
	
	/**
	 * {@link ScreenMouseListener} is a {@link MiniMouseListener}
	 * with screen coordinates (Bounds) and Layer Depth (zIndex).
	 * @author BobbyL2k
	 */
	public static class ScreenMouseListener{
		
		private MiniMouseListener mouseListener;
		private Range boundX,boundY;
		private double zIndex;

		private boolean isActive;

		public ScreenMouseListener(MiniMouseListener mouseListener, Range boundX, Range boundY, double zIndex) {
			this.mouseListener = mouseListener;
			this.boundX = boundX;
			this.boundY = boundY;
			this.zIndex = zIndex;
			this.isActive = true;
		}
		

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
		

		public double getzIndex() {
			return zIndex;
		}

		public void setzIndex(double zIndex) {
			this.zIndex = zIndex;
		}
	}

	public static boolean isKeyActive(int key) {
		synchronized (InputManager.keyActive) {
			return InputManager.keyActive[key];	
		}
	}

	public static boolean isMouseOnScreen() {
		return mouseOnScreen;
	}
	
	public static void addComponent(Component comp){
		comp.addKeyListener(keyListener);
		comp.addMouseListener(mouseListener);
		comp.addMouseMotionListener(mouseMotionListener);
	}
	
	private static ArrayList<ScreenMouseListener> screenMouseListeners = new ArrayList<ScreenMouseListener>();
	
	public static void addScreenMouseListener(ScreenMouseListener screenMouseListener){
		screenMouseListeners.add(screenMouseListener);
	}
	
	private static void setKeyActive(int key, boolean active) {
		if(active != InputManager.keyActive[key])
			System.out.println("Key "+key+" is "+(active?"active":"inactive"));
		synchronized (InputManager.keyActive) {
			InputManager.keyActive[key] = active;	
		}
	}

	private static boolean[] keyActive = new boolean[256];
	private static boolean mouseOnScreen = false;

	private static void setMouseOnScreen(boolean mouseOnScreen) {
		InputManager.mouseOnScreen = mouseOnScreen;
	}
	
	private static class ManagerMouseListener implements MouseListener {

		private static void updateCustomMouseListeners(){
			screenMouseListeners.sort(new Comparator<ScreenMouseListener>() {
				@Override
				public int compare(ScreenMouseListener o1, ScreenMouseListener o2) {
					return Double.compare(o1.zIndex, o2.zIndex);
				}
			});
		}
		
		private static MiniMouseListener findMouseListenerAt(int x, int y) {
			updateCustomMouseListeners();
			for(int c=screenMouseListeners.size()-1; c>=0; c--){
				ScreenMouseListener screenMouseListener = screenMouseListeners.get(c);
				if(screenMouseListener.isActive==true && screenMouseListener.boundX.inRange(x) && screenMouseListener.boundY.inRange(y)){
					return screenMouseListener.mouseListener;
				}
			}
			return null;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Point location = e.getPoint();
			System.out.println("Mouse Clicked At ("+location.getX()+","+location.getY()+")");
			MiniMouseListener ml = findMouseListenerAt((int)location.getX(), (int)location.getY());
			if(ml!=null){
				ml.mouseClicked(e);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point location = e.getPoint();
			MiniMouseListener ml = findMouseListenerAt((int)location.getX(), (int)location.getY());
			if(ml!=null){
				ml.mousePressed(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point location = e.getPoint();
			MiniMouseListener ml = findMouseListenerAt((int)location.getX(), (int)location.getY());
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
