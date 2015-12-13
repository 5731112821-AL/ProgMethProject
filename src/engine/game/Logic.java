package engine.game;

import java.util.ArrayList;

import engine.render.RenderLayer.Renderable;

public abstract class Logic {
	
	protected abstract void logicLoop(long frameTime);
	protected abstract void onExitLogic();
	protected abstract void objectDestroyReport(GameObject2D gameObject2D);
	
	private long oldTime;
	private int targetFrameTime;
	private boolean logicIsRunning = false;

	private ArrayList<Renderable> renderList;
	private ArrayList<Updatable> updatePreList;
	
	public ArrayList<Updatable> updatePostList;

	private ArrayList<GameObject2D> gameObjects;
	private ArrayList<Object> objectsToAdd;
	private ArrayList<Object> objectsToRemove;
	
	public interface Updatable{
		public abstract void update(long frameTime);
	}
	
	
	public ArrayList<Renderable> getRenderList() {
		return renderList;
	}

	public void addObjectNextTick(Object obj) {
		objectsToAdd.add(obj);
	}
	public void removeObjectNextTick(Object obj) {
		objectsToRemove.add(obj);
	}
	
	private void addObject(Object obj){
//		System.out.println("Add Game Object " + obj);
		if(obj instanceof Renderable)
			renderList.add((Renderable) obj);
		if(obj instanceof Updatable)
			updatePreList.add((Updatable) obj);
		if(obj instanceof GameObject2D)
			gameObjects.add((GameObject2D) obj);
	}
	private void removeObject(Object obj){
//		System.out.println("Remove Game Object " + obj);
		if(obj instanceof Renderable)
			renderList.remove((Renderable) obj);
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
	 * Use GameLogic() to initialize the game states.
	 */
	public Logic(int maxFPS) {
		renderList			= new ArrayList<>();
		updatePreList		= new ArrayList<>();
		updatePostList		= new ArrayList<>(); 
		gameObjects			= new ArrayList<>();
		objectsToAdd 	= new ArrayList<>();
		objectsToRemove = new ArrayList<>();
		targetFrameTime = 1000/maxFPS;
	}

	/// For FPS
	private int counter = 0;
	private static boolean showFPS = false;
	
	public void update() {
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
	
	long minSleepTime = 0;
	
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
	
	protected void stopLogic(){
		logicIsRunning = false;
	}
	
	protected int getSleepTime() {
		return targetFrameTime;
	}
	protected void setSleepTime(int sleepTime) {
		if(sleepTime < 0)
			sleepTime = 0;
		this.targetFrameTime = sleepTime;
	}
}
