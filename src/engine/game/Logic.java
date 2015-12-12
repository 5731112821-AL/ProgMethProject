package engine.game;

import java.util.ArrayList;

import engine.render.RenderLayer.Renderable;

public abstract class Logic {
	
	protected abstract void logicLoop(long frameTime);
	protected abstract void onExitLogic();
	protected abstract void objectDestroyReport(GameObject2D gameObject2D);
	
	private long oldTime;
	private int sleepTime;
	private boolean logicIsRunning = false;

	private ArrayList<Renderable> renderList;
	private ArrayList<Updatable> updatePreList;
	
	public ArrayList<Updatable> updatePostList;

	private ArrayList<GameObject2D> gameObjects;
	private ArrayList<GameObject2D> gameObjectsToAdd;
	private ArrayList<GameObject2D> gameObjectsToRemove;
	
	public interface Updatable{
		public abstract void update(long frameTime);
	}
	
	
	public ArrayList<Renderable> getRenderList() {
		return renderList;
	}

	public void addGameObjectNextTick(GameObject2D obj) {
		gameObjectsToAdd.add(obj);
	}
	public void removeGameObjectNextTick(GameObject2D obj) {
		gameObjectsToRemove.add(obj);
	}
	
	private void addGameObject(GameObject2D obj){
//		System.out.println("Add Game Object " + obj);
		if(obj instanceof Renderable)
			renderList.add((Renderable) obj);
		if(obj instanceof Updatable)
			updatePreList.add((Updatable) obj);
		gameObjects.add(obj);
	}
	private void removeGameObject(GameObject2D obj){
//		System.out.println("Remove Game Object " + obj);
		if(obj instanceof Renderable)
			renderList.remove((Renderable) obj);
		if(obj instanceof Updatable)
			updatePreList.remove((Updatable) obj);
		gameObjects.remove(obj);
	}
	
	private void updateGameObjectList(){
		/// Remove destroyed object
		for(int c=gameObjects.size()-1; c>=0; c--){
			GameObject2D obj = gameObjects.get(c);
			if(obj.isDestroy()){
				objectDestroyReport(obj);
				removeGameObject(obj);
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
		gameObjectsToAdd 	= new ArrayList<>();
		gameObjectsToRemove = new ArrayList<>();
		sleepTime = 1000/maxFPS;
	}


//	private int counter = 0;
	
	public void update() {
//		System.out.println("RUNNING");
		
		/// Time Keeping
		long 
			newTime = System.currentTimeMillis(),
			frameTime = newTime - oldTime;
		
		oldTime = newTime;
		
//		if(++counter == 10){
//			counter = 0;
////			System.out.println("Frame Time : "+frameTime);
//			System.out.println("FPS :"+1000/frameTime);
//		}

		InputManager.executeAllMouseEvent();
		// Check list for destroyed objects
		updateGameObjectList();
		// Add and Remove pending Objects
		for(GameObject2D obj: gameObjectsToAdd)
			addGameObject(obj);
		gameObjectsToAdd.clear();
		for(GameObject2D obj: gameObjectsToRemove)
			removeGameObject(obj);
		gameObjectsToRemove.clear();
		
		
		for(Updatable updatable : updatePreList)
			updatable.update(frameTime);
		
		logicLoop(frameTime);
		
		for(Updatable updatable : updatePostList)
			updatable.update(frameTime);
		
//		System.out.println("gameObjects"+gameObjects.size());
//		System.out.println("renderList"+renderList.size());
		
	}
	
	public void runLogic() {
		logicIsRunning = true;
		oldTime = System.currentTimeMillis();
		while(logicIsRunning){
			update();
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
		return sleepTime;
	}
	protected void setSleepTime(int sleepTime) {
		if(sleepTime < 0)
			sleepTime = 0;
		this.sleepTime = sleepTime;
	}
}
