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

/**
 * Handles inputs from {@link java.awt.Component}.
 * Use To {@code addComponent()} to registers a {@link Component}.
 * Do note that the {@link MouseListener} and {@link MouseMotionListener} event
 * is <b>NOT</b> dispatched on AWT's and must be called with {@code executeAllMouseEvent()}.
 * @author L2k-nForce
 */
public class InputManager {
	private InputManager() {}
	
	private static final int STD_SLEEP_TIME = 20;

	private static Component latestComponent = null;
	
	/**
	 * Registers a {@link Component} into the {@link InputManager}.
	 * {@link InputManager} can register multiples {@link Component}
	 * to centralize the input data. But the {@link MouseListener} and
	 * {@link MouseMotionListener} will not have all the correct data.
	 * @param comp a {@link Component} to register
	 */
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
		KEY_SPACEBAR	= 32,
		KEY_ENTER		= 10;
	
	private static boolean[] keyActive = new boolean[256];

	/**
	 * Resets all the keys to be inactive.
	 * This can be use to simulate a trigger-style
	 * input event.
	 */
	public static void forceFlushKeys() {
		for(int c=0; c<keyActive.length; c++){
			keyActive[c] = false;
		}
	}
	
	/**
	 * Checks if the key is pressed. At the given time.
	 * @param key is the same {@code int key} from {@code KeyListener setKeyActive()}
	 * @return true if the key is pressed.
	 */
	public static boolean isKeyActive(int key) {
//		synchronized (InputManager.keyActive) {
			return InputManager.keyActive[key];	
//		}
	}
	
	private static final KeyListener keyListener = new KeyListener() {

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
	 * {@link ScreenMouseListener} is a {@link MouseListener}
	 * with screen coordinates (Ranges) and Layer Depth (zIndex).
	 * Do note that {@link MouseEvent} from {@link MouseListener}'s
	 * Enter and Exit should not be used since it's inaccurate.
	 * @author BobbyL2k
	 */
	public static class ScreenMouseListener{
		
		private MouseListener mouseListener;
		private Range boundX,boundY;

		private double zIndex;
	
		private boolean isActive;
	
		/**
		 * @param mouseListener
		 * @param boundX x-axis bounds in which the {@link ScreenMouseListener} is listening to.
		 * @param boundY y-axis bounds in which the {@link ScreenMouseListener} is listening to.
		 * @param zIndex Higher zIndex means that it is on top of the other
		 * object with a lower zIndex value. Having a higher zIndex guarantees that a {@link MouseEvent}
		 * will be dispatched to that {@link ScreenMouseListener}.
		 */
		public ScreenMouseListener(MouseListener mouseListener, Range boundX, Range boundY, double zIndex) {
			this.mouseListener = mouseListener;
			this.boundX = boundX;
			this.boundY = boundY;
			this.zIndex = zIndex;
			this.isActive = true;
		}

		public Range getBoundX() {
			return boundX;
		}


		public void setBoundX(Range boundX) {
			this.boundX = boundX;
		}


		public Range getBoundY() {
			return boundY;
		}


		public void setBoundY(Range boundY) {
			this.boundY = boundY;
		}
	
		public boolean isActive() {
			return isActive;
		}
	
		/**
		 * Set {@link ScreenMouseListener} on and off.
		 * If the {@link ScreenMouseListener} is inactive the mouse clicks
		 * through to the other {@link ScreenMouseListener} with an equal or lower zIndex.
		 * A {@link ScreenMouseListener} is on (active) by default.
		 * @param isActive
		 */
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
	
	/**
	 * @return true if the mouse is inside one of the registered Components.
	 */
	public static boolean isMouseOnScreen() {
		return mouseOnScreen.get();
	}
	
	/**
	 * @return true if the mouse is hold down. (Between a click)
	 */
	public static boolean isMouseHoldDown() {
		return mouseHoldDown.get();
	}

	/**
	 * To check if the mouse has entered a {@link ScreenMouseListener}.
	 * {@link InputManager} creates and runs the mouseEnterChecker Thread
	 * to do checking. If the mouse is not on screen, the Thread is suspended.
	 * The Thread checks the mouse position and sleeps for {@link STD_SLEEP_TIME}.
	 */
	private static Thread mouseEnterChecker;
	
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
								synchronized (pendingMouseEvents) {
									pendingMouseEvents.add(
											new PendingMouseEvent(
												lastMouseListener.mouseListener,
												e,
												PendingMouseEvent.EventType.exited));
								}
							}
							lastMouseListener = findMouseListenerAt(mousePoint.x, mousePoint.y);
							if(lastMouseListener != null){
								synchronized (pendingMouseEvents) {
									pendingMouseEvents.add(
											new PendingMouseEvent(
												lastMouseListener.mouseListener,
												e,
												PendingMouseEvent.EventType.entered));
								}
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
	
	private static ScreenMouseListener findMouseListenerAt(int screenX, int screenY) {
		updateCustomMouseListeners();
		int screenVirtualX = (int) (screenX/Resources.scale);
		int screenVirtualY = (int) (screenY/Resources.scale);
		for(int c=screenMouseListeners.size()-1; c>=0; c--){
			ScreenMouseListener screenMouseListener = screenMouseListeners.get(c);
			if(screenMouseListener.isActive==true && screenMouseListener.boundX.inRange(screenVirtualX) && screenMouseListener.boundY.inRange(screenVirtualY)){
				return screenMouseListener;
			}
		}
		return null;
	}
	
	private static final MouseListener mouseListener = new MouseListener() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			Point location = e.getPoint();
			ScreenMouseListener screenMouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY()); 
			if(screenMouseListener != null && screenMouseListener.mouseListener != null){
				synchronized (pendingMouseEvents) {
					pendingMouseEvents.add(
							new PendingMouseEvent(
								screenMouseListener.mouseListener,
								e,
								PendingMouseEvent.EventType.click));
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			setMouseHoldDown(true);
			Point location = e.getPoint();
			System.out.println("Mouse Clicked At ("+location.getX()+","+location.getY()+")");
			ScreenMouseListener screenMouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY()); 
			if(screenMouseListener != null && screenMouseListener.mouseListener != null){
				synchronized (pendingMouseEvents) {
					pendingMouseEvents.add(
							new PendingMouseEvent(
								screenMouseListener.mouseListener,
								e,
								PendingMouseEvent.EventType.press));
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setMouseHoldDown(false);
			Point location = e.getPoint();
			ScreenMouseListener screenMouseListener = findMouseListenerAt((int)location.getX(), (int)location.getY()); 
			if(screenMouseListener != null && screenMouseListener.mouseListener != null){
				synchronized (pendingMouseEvents) {
					pendingMouseEvents.add(
							new PendingMouseEvent(
								screenMouseListener.mouseListener,
								e,
								PendingMouseEvent.EventType.release));
				}
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
	
	/**
	 * @return The mouse's location on screen. This method is Thread safe.
	 * Thus need not be used on {@link java.awt}'s Thread.
	 */
	public static Point getMouseLocation(){
		synchronized (mouseLocation) {
			return new Point(mouseLocation);
		}
	}
	
	private static final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
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
	/**
	 * Executes all the pending mouse events from AWT's 
	 * Event Thread and {@link InputManager}'s {@code mouseEnterChecker} Thread.
	 */
	public static void executeAllMouseEvent(){
		while(true){
			PendingMouseEvent event;
			synchronized (pendingMouseEvents) {
				if(pendingMouseEvents.isEmpty())
					 break;
				event = pendingMouseEvents.remove(0);
			}
			event.execute();
		}
	}

}
