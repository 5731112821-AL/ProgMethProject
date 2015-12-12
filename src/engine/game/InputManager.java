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
import java.util.Iterator;

import engine.utilities.Range;

public class InputManager {
	private InputManager() {}

	public static void addComponent(Component comp){
		comp.addKeyListener(keyListener);
		comp.addMouseListener(mouseListener);
		comp.addMouseMotionListener(mouseMotionListener);
	}

	// Keyboard
	public static final int 
		KEY_UP_ARROW    = 38,
		KEY_DOWN_ARROW  = 40,
		KEY_LEFT_ARROW  = 37,
		KEY_RIGHT_ARROW = 39,
		KEY_ESC         = 27;
	
	private static boolean[] keyActive = new boolean[256];

	/**
	 * Resets all the keys to be inactive
	 */
	public static void forceFlushKeys() {
		for(int c=0; c<keyActive.length; c++){
			keyActive[c] = false;
		}
	}
	
	public static boolean isKeyActive(int key) {
		synchronized (InputManager.keyActive) {
			return InputManager.keyActive[key];	
		}
	}
	
	public static final KeyListener keyListener = new KeyListener() {

		private void setKeyActive(int key, boolean active) {
			if(active != InputManager.keyActive[key])
				System.out.println("Key "+key+" is "+(active?"active":"inactive"));
			synchronized (InputManager.keyActive) {
				InputManager.keyActive[key] = active;	
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 
			// Do nothing 
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			setKeyActive(e.getKeyCode(), false);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			setKeyActive(e.getKeyCode(), true);
		}
	};
	
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

	private static ArrayList<ScreenMouseListener> screenMouseListeners = new ArrayList<ScreenMouseListener>();
	public static void addScreenMouseListener(ScreenMouseListener screenMouseListener){
		screenMouseListeners.add(screenMouseListener);
	}
	

	/**
	 * {@link MiniMouseListener} is a reduced-complexity of {@link MouseListener} in java.awt
	 * @author BobbyL2k
	 */
	public static interface MiniMouseListener{
		public void mouseClicked(MouseEvent e);
		public void mousePressed(MouseEvent e);
		public void mouseReleased(MouseEvent e);
	}

	private static boolean mouseOnScreen = false;
	
	public static boolean isMouseOnScreen() {
		return mouseOnScreen;
	}

	// Mouse
	public static final MouseListener mouseListener = new MouseListener() {

		private void updateCustomMouseListeners(){
			screenMouseListeners.sort(new Comparator<ScreenMouseListener>() {
				@Override
				public int compare(ScreenMouseListener o1, ScreenMouseListener o2) {
					return Double.compare(o1.zIndex, o2.zIndex);
				}
			});
		}
		
		private MiniMouseListener findMouseListenerAt(int x, int y) {
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
			MiniMouseListener mouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY());
			if(mouseListener!=null){
				pendingMouseEvents.add(
						new PendingMouseEvent(
							mouseListener,
							e,
							PendingMouseEvent.EventType.click));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point location = e.getPoint();
			MiniMouseListener mouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY());
			if(mouseListener!=null){
				pendingMouseEvents.add(
						new PendingMouseEvent(
							mouseListener,
							e,
							PendingMouseEvent.EventType.press));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point location = e.getPoint();
			MiniMouseListener mouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY());
			if(mouseListener!=null){
				pendingMouseEvents.add(
						new PendingMouseEvent(
							mouseListener,
							e,
							PendingMouseEvent.EventType.release));
			}
		}

		private void setMouseOnScreen(boolean mouseOnScreen) {
			InputManager.mouseOnScreen = mouseOnScreen;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			setMouseOnScreen(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setMouseOnScreen(false);
		}
		
	};

	private static Point mouseLocation;
	
	public static Point getMouseLocation(){
		return mouseLocation;
	}
	
	public static final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseLocation = e.getPoint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseLocation = e.getPoint();
		}
	};

	private static class PendingMouseEvent{

		public static enum EventType{ click, press, release }
		
		MiniMouseListener listener;
		MouseEvent event;
		EventType type;
		
		public PendingMouseEvent(MiniMouseListener listener, MouseEvent event, EventType type) {
			super();
			this.listener = listener;
			this.event    = event;
			this.type     = type;
		}
		
		public void execute(){
			switch (type) {
				case click:
					listener.mouseClicked(event);
					break;
				case press:
					listener.mousePressed(event);
					break;
				case release:
					listener.mouseReleased(event);
					break;
					
			}
		}
	}
	
	private static ArrayList<PendingMouseEvent> pendingMouseEvents = new ArrayList<>();
	public static void executeAllMouseEvent(){
		for (Iterator<PendingMouseEvent> iterator = pendingMouseEvents.iterator(); iterator.hasNext();){
			PendingMouseEvent pendingMouseEvent = iterator.next();
			iterator.remove();
			pendingMouseEvent.execute();
		}
	}

}
