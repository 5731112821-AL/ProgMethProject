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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import engine.utilities.Range;
import flyerGame.engineExtension.Resources;

public class InputManager {
	private InputManager() {}
	
	private static final int STD_SLEEP_TIME = 20;

	private static Component latestComponent = null;
	
	public static void addComponent(Component comp){
		comp.addKeyListener(keyListener);
		comp.addMouseListener(mouseListener);
		comp.addMouseMotionListener(mouseMotionListener);
		latestComponent = comp;
	}

	// Keyboard
	public static final int 
		KEY_UP_ARROW    = 38,
		KEY_DOWN_ARROW  = 40,
		KEY_LEFT_ARROW  = 37,
		KEY_RIGHT_ARROW = 39,
		KEY_ESC         = 27,
		KEY_SPACEBAR	= 32;
	
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
//		synchronized (InputManager.keyActive) {
			return InputManager.keyActive[key];	
//		}
	}
	
	public static final KeyListener keyListener = new KeyListener() {

		private void setKeyActive(int key, boolean active) {
			if(active != InputManager.keyActive[key])
				System.out.println("Key "+key+" is "+(active?"active":"inactive"));
//			synchronized (InputManager.keyActive) {
				InputManager.keyActive[key] = active;	
//			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
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
		
		private MouseListener mouseListener;
		private Range boundX,boundY;
		private double zIndex;
	
		private boolean isActive;
	
		public ScreenMouseListener(MouseListener mouseListener, Range boundX, Range boundY, double zIndex) {
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

	// Mouse
	private static AtomicBoolean mouseOnScreen = new AtomicBoolean(false);
	private static AtomicBoolean mouseHoldDown = new AtomicBoolean(false);
	
	public static boolean isMouseOnScreen() {
		return mouseOnScreen.get();
	}
	
	public static boolean isMouseHoldDown() {
		return mouseHoldDown.get();
	}

	static Thread mouseEnterChecker;
	
	static{
		mouseEnterChecker = new Thread(new Runnable() {
			
			private ScreenMouseListener lastMouseListener = null;
			
			@Override
			public void run() {
				while(true){
					synchronized (mouseOnScreen) {
						if(mouseOnScreen.get() == false){
							try {
								System.out.println(Thread.currentThread().getName() + " Suspended");
								mouseOnScreen.wait();
								System.out.println(Thread.currentThread().getName() + " Resumed");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					Point mousePoint = getMouseLocation();
					if(lastMouseListener != null && lastMouseListener == findMouseListenerAt(mousePoint.x, mousePoint.y)){
						// Case the same obj is being hovered on
					}else{
						if(latestComponent != null){
							MouseEvent e = new MouseEvent(latestComponent, 0, 0, 0, mousePoint.x, mousePoint.y, 0, 0, 0, false, 0);
//							System.out.println("lastMouseListener " + lastMouseListener);
							if(lastMouseListener != null){
								pendingMouseEvents.add(
										new PendingMouseEvent(
											lastMouseListener.mouseListener,
											e,
											PendingMouseEvent.EventType.exited));
							}
							lastMouseListener = findMouseListenerAt(mousePoint.x, mousePoint.y);
							if(lastMouseListener != null){
								pendingMouseEvents.add(
										new PendingMouseEvent(
											lastMouseListener.mouseListener,
											e,
											PendingMouseEvent.EventType.entered));
							}
						} else { System.out.println( "lastComponent is null" ); }
					}
					try {
						Thread.sleep(STD_SLEEP_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		},"Mouse Enter Checker Thread");
		mouseEnterChecker.start();
	}
	
	private static void updateCustomMouseListeners(){
		screenMouseListeners.sort(new Comparator<ScreenMouseListener>() {
			@Override
			public int compare(ScreenMouseListener o1, ScreenMouseListener o2) {
				return Double.compare(o1.zIndex, o2.zIndex);
			}
		});
	}
	
	private static ScreenMouseListener findMouseListenerAt(int x, int y) {
		updateCustomMouseListeners();
		x = (int) Range.map(x, Resources.trueScreenFieldX, Resources.virtualScreenFieldX);
		y = (int) Range.map(y, Resources.trueScreenFieldY, Resources.virtualScreenFieldY);
		for(int c=screenMouseListeners.size()-1; c>=0; c--){
			ScreenMouseListener screenMouseListener = screenMouseListeners.get(c);
			if(screenMouseListener.isActive==true && screenMouseListener.boundX.inRange(x) && screenMouseListener.boundY.inRange(y)){
				return screenMouseListener;
			}
		}
		return null;
	}
	
	public static final MouseListener mouseListener = new MouseListener() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Point location = e.getPoint();
			System.out.println("Mouse Clicked At ("+location.getX()+","+location.getY()+")");
			ScreenMouseListener screenMouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY()); 
			if(screenMouseListener != null && screenMouseListener.mouseListener != null){
				pendingMouseEvents.add(
						new PendingMouseEvent(
							screenMouseListener.mouseListener,
							e,
							PendingMouseEvent.EventType.click));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			setMouseHoldDown(true);
			Point location = e.getPoint();
			ScreenMouseListener screenMouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY()); 
			if(screenMouseListener != null && screenMouseListener.mouseListener != null){
				pendingMouseEvents.add(
						new PendingMouseEvent(
							screenMouseListener.mouseListener,
							e,
							PendingMouseEvent.EventType.press));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setMouseHoldDown(false);
			Point location = e.getPoint();
			ScreenMouseListener screenMouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY()); 
			if(screenMouseListener != null && screenMouseListener.mouseListener != null){
				pendingMouseEvents.add(
						new PendingMouseEvent(
							screenMouseListener.mouseListener,
							e,
							PendingMouseEvent.EventType.release));
			}
		}
		
		private void setMouseHoldDown(boolean mouseHoldDown) {
			InputManager.mouseHoldDown.set(mouseHoldDown);
		}

		private void setMouseOnScreen(boolean mouseOnScreen) {
			synchronized (InputManager.mouseOnScreen) {	
				InputManager.mouseOnScreen.set(mouseOnScreen);
				if(mouseOnScreen == true)
					InputManager.mouseOnScreen.notifyAll();
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			System.out.println("mouseEntered screen");
			setMouseOnScreen(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			System.out.println("mouseExited screen");
			setMouseOnScreen(false);
		}
		
	};

	private static Point mouseLocation = new Point();
	
	public static Point getMouseLocation(){
		synchronized (mouseLocation) {
			return new Point(mouseLocation);
		}
	}
	
	public static final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {
			synchronized (mouseLocation) {
				Point newMouseLocation = e.getPoint();
				mouseLocation.x = newMouseLocation.x;
				mouseLocation.y = newMouseLocation.y;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			synchronized (mouseLocation) {
				Point newMouseLocation = e.getPoint();
				mouseLocation.x = newMouseLocation.x;
				mouseLocation.y = newMouseLocation.y;
			}
		}
	};
	
	private static class PendingMouseEvent{
		
		public static enum EventType{ click, press, release, entered, exited }
		
		MouseListener listener;
		MouseEvent event;
		EventType type;
		
		public PendingMouseEvent(MouseListener listener, MouseEvent event, EventType type) {
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
				case entered:
					listener.mouseEntered(event);
					break;
				case exited:
					listener.mouseExited(event);
					break;
			}
		}
	}
	
	private static List<PendingMouseEvent> pendingMouseEvents = new LinkedList<PendingMouseEvent>();
	public static void executeAllMouseEvent(){
		while(pendingMouseEvents.isEmpty() == false){
			PendingMouseEvent event = pendingMouseEvents.remove(0);
			event.execute();
		}
	}

}
