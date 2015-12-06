package engine.game;

import java.util.ArrayList;

import engine.render.RenderLayer.Renderable;

public abstract class GameLogic {
	protected abstract void gameInit();
	protected abstract void gameLoop(long frameTime);
	
	private long oldTime;
	private int sleepTime;

	private ArrayList<Renderable> renderList;
	private ArrayList<Updatable> updatePreList;
	
	public ArrayList<Updatable> updatePostList;

	private ArrayList<GameObject2D> gameObjects;
	private ArrayList<GameObject2D> gameToAddObjects;
	private ArrayList<GameObject2D> gameToRemoveObjects;
	
	public interface Updatable{
		public abstract void update(long frameTime);
	}
	
	
	public ArrayList<Renderable> getRenderList() {
		return renderList;
	}

	public void addGameObjectNextTick(GameObject2D obj) {
		gameToAddObjects.add(obj);
	}
	public void removeGameObjectNextTick(GameObject2D obj) {
		gameToRemoveObjects.add(obj);
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
			if(obj.isDestroy())
				removeGameObject(obj);
		}
	}
	
	/**
	 * GameLogic() calls gameInit()
	 * Therefore must NOT be override
	 * Please do variable initialization in gameInit()
	 */
	public GameLogic() {
		renderList			= new ArrayList<>();
		updatePreList		= new ArrayList<>();
		updatePostList		= new ArrayList<>(); 
		gameObjects			= new ArrayList<>();
		gameToAddObjects 	= new ArrayList<>();
		gameToRemoveObjects = new ArrayList<>();
		sleepTime = 10;
		
		gameInit();
		
		oldTime = System.currentTimeMillis();
	}

//	private int counter = 0;
	
	public void update() {
//		System.out.println("RUNNING");
		/// Time Keeping
		long 
			newTime = System.currentTimeMillis(),
			frameTime = newTime - oldTime;
//		if(++counter == 10){
//			counter = 0;
////			System.out.println("Frame Time : "+frameTime);
//			System.out.println("FPS :"+1000/frameTime);
//		}

		// Check list for destroyed objects
		updateGameObjectList();
		// Add and Remove pending Objects
		for(GameObject2D obj: gameToAddObjects)
			addGameObject(obj);
		gameToAddObjects.clear();
		for(GameObject2D obj: gameToRemoveObjects)
			removeGameObject(obj);
		gameToRemoveObjects.clear();
		
		
		for(Updatable updatable : updatePreList)
			updatable.update(frameTime);
		gameLoop(frameTime);
		for(Updatable updatable : updatePostList)
			updatable.update(frameTime);
		
//		System.out.println("renderList"+renderList.size());
		
		oldTime = newTime;
	}
	
	public void runGame() {
		while(true){
			update();
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){};
		}
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
