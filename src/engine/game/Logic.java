package engine.game;

import java.util.ArrayList;
import java.util.List;

import engine.render.GamePanel;
import engine.render.RenderLayer.Renderable;

/**
 * Creates the "Game Loops", handles {@link GameObject2D} removal,
 * executes/updates {@link Updatable} objects in the event loop, and packs all {@link Renderable} objects
 * into the renderList. 
 * @author L2k-nForce
 */
public abstract class Logic {
	
	/**
	 * Execute during the inside {@link Logic}'s event loop.
	 * @param frameTime Time difference from the 
	 * last time this method is called.
	 */
	protected abstract void logicLoop(long frameTime);
	/**
	 * Is called once before the {@link Logic}'s event loop ends.
	 */
	protected abstract void onExitLogic();
	/**
	 * @param gameObject2D the object that is destroyed and is now
	 * removed from {@link Logic}. If the object implements {@link Renderable}
	 * it is now removed from renderList, therefore it is safe to be modified.
	 */
	protected abstract void objectDestroyReport(GameObject2D gameObject2D);
	
	private long oldTime;
	private int targetFrameTime;
	private boolean logicIsRunning = false;

	private ArrayList<Renderable> renderList;
	private ArrayList<Updatable> updatePreList;
	
	/**
	 * Is an List of {@link Updatable} that is called after the
	 * {@link Logic}'e event loop. Is meant to be used with calling
	 * {@link GamePanel} and {@link java.awt.Component} redraw.
	 */
	public ArrayList<Updatable> updatePostList;

	private ArrayList<GameObject2D> gameObjects;
	private ArrayList<Object> objectsToAdd;
	private ArrayList<Object> objectsToRemove;
	
	/**
	 * The update method is called inside the event loop,
	 * <b>before</b> the Logic's logicLoop method. The use of this 
	 * is to configure the object to be ready for the logicLoop.
	 * @author L2k-nForce
	 */
	public interface Updatable{
		public abstract void update(long frameTime);
	}
	
	
	/**
	 * @return The renderList which keeps all the {@link Renderable}
	 * that {@link Logic} is currently handling. Do note that accessing,
	 * modifying, and removing objects directly from this List is <b>unsafe</b>.
	 * As the renderList is access by java.awt event Thread.<br>
	 * This method should only be use for giving the {@link GamePanel}
	 * access the renderList   
	 */
	public List<Renderable> getRenderList() {
		return renderList;
	}

	
	/**
	 * This method is Thread safe.
	 * @param obj Is the object to be added in the next tick
	 * (event loop cycle).
	 */
	public void addObjectNextTick(Object obj) {
		objectsToAdd.add(obj);
	}
	/**
	 * This method is Thread safe.
	 * @param obj Is the object to be removed in the next tick
	 * (event loop cycle).
	 */
	public void removeObjectNextTick(Object obj) {
		objectsToRemove.add(obj);
	}
	
	private void addObject(Object obj){
//		System.out.println("Add Game Object " + obj);
		if(obj instanceof Renderable){
			synchronized (renderList) {
				renderList.add((Renderable) obj);
			}
		}
		if(obj instanceof Updatable)
			updatePreList.add((Updatable) obj);
		if(obj instanceof GameObject2D)
			gameObjects.add((GameObject2D) obj);
	}
	private void removeObject(Object obj){
//		System.out.println("Remove Game Object " + obj);
		if(obj instanceof Renderable){
			synchronized (renderList) {
				renderList.remove((Renderable) obj);
			}
		}
		if(obj instanceof Updatable)
			updatePreList.remove((Updatable) obj);
		if(obj instanceof GameObject2D)
			gameObjects.remove((GameObject2D)obj);
	}
	
	private void updateGameObjectList(){
		/// Remove destroyed object
		for(int c=gameObjects.size()-1; c>=0; c--){
			GameObject2D obj = gameObjects.get(c);
			if(obj.isDestroy()){
				objectDestroyReport(obj);
				removeObject(obj);
			}
		}
	}
	
	/**
	 * @param maxRate Is the maximum rate in which the event
	 * loop will run at.
	 */
	public Logic(int maxRate) {
		renderList			= new ArrayList<>();
		updatePreList		= new ArrayList<>();
		updatePostList		= new ArrayList<>(); 
		gameObjects			= new ArrayList<>();
		objectsToAdd 	= new ArrayList<>();
		objectsToRemove = new ArrayList<>();
		targetFrameTime = 1000/maxRate;
	}

	/// For FPS
	private int counter = 0;
	private static boolean showFPS = false;
	
	private void update() {
//		System.out.println("RUNNING");
		
		/// Time Keeping
		long 
			newTime = System.currentTimeMillis(),
			frameTime = newTime - oldTime;
		
		oldTime = newTime;
		
		/// For FPS
		if(showFPS)
			if(++counter == 10){
				counter = 0;
				System.out.println("Frame Time : "+frameTime);
				System.out.println("FPS :"+1000/frameTime);
			}

		InputManager.executeAllMouseEvent();
		// Check list for destroyed objects
		updateGameObjectList();
		// Add and Remove pending Objects
		for(Object obj: objectsToAdd)
			addObject(obj);
		objectsToAdd.clear();
		for(Object obj: objectsToRemove)
			removeObject(obj);
		objectsToRemove.clear();
		
		
		for(Updatable updatable : updatePreList)
			updatable.update(frameTime);
		
		logicLoop(frameTime);
		
		for(Updatable updatable : updatePostList)
			updatable.update(frameTime);
		
//		System.out.println("gameObjects"+gameObjects.size());
//		System.out.println("renderList"+renderList.size());
		
	}
	
	private long minSleepTime = 0;
	
	/**
	 * Start running {@link Logic}'s event loop.
	 * Do note that this method blocks the Thread until
	 * the event loop stops.
	 */
	public void runLogic() {
		logicIsRunning = true;
		oldTime = System.currentTimeMillis();
		while(logicIsRunning){
			update();
			long timeTaken = System.currentTimeMillis() - oldTime;
			long sleepTime = (targetFrameTime - timeTaken < 0) ? 0 : targetFrameTime - timeTaken;
			/// For FPS
			if(showFPS){
				minSleepTime = Math.min(minSleepTime, sleepTime);
				if(counter == 0){
					System.out.println("mSleep Time : "+sleepTime+"/"+targetFrameTime);
					minSleepTime = sleepTime;
				}
			}
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){
				break;
			};
		}
		onExitLogic();
	}
	
	/**
	 * Stops {@link Logic}'s Event loop
	 */
	protected void stopLogic(){
		logicIsRunning = false;
	}
	
	/**
	 * @return the amount of that the Thread sleeps if
	 * the event loops is done in <1ms. Theoretical maximum
	 * sleep time.
	 */
	protected int getSleepTime() {
		return targetFrameTime;
	}
	
	/**
	 * @param sleepTime sets the theoretical maximum
	 * sleep time of the Thread running the {@link Logic} event loop.
	 */
	protected void setSleepTime(int sleepTime) {
		if(sleepTime < 0)
			sleepTime = 0;
		this.targetFrameTime = sleepTime;
	}
}
