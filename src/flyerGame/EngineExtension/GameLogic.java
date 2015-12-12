package flyerGame.EngineExtension;

import java.util.ArrayList;

import engine.game.GameObject2D;
import engine.game.InputManager;
import engine.render.GamePanel;
import engine.render.RenderLayer;
import engine.utilities.Range;
import flyerGame.GameObject.Bullet;
import flyerGame.GameObject.EnemyTarget;
import flyerGame.GameObject.Player;
import flyerGame.GameObject.Target;

public class GameLogic extends engine.game.Logic {

	private static final int TARGET_FPS = 60;

	Player player;
	
	private ArrayList<Bullet> bulletList;
	private ArrayList<Target> targetList; 
	private GamePanel gamePanel;
	private RenderLayer gameLayer;

	private int enemyTargetSpawnTimeCounter;
	private static final Range ENEMY_TARGET_SPAWN_TIME = new Range(1000, 3000);
	
	public GameLogic(GamePanel gamePanel) {
		super(TARGET_FPS);
		this.gamePanel = gamePanel;
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		
		gameLayer = new RenderLayer(getRenderList());
		this.gamePanel.getRenderLayers().add(gameLayer);
		
		bulletList = new ArrayList<>();
		targetList = new ArrayList<>();
		
		player = new Player(this);
		addTarget(player);
		
		enemyTargetSpawnTimeCounter = (int)ENEMY_TARGET_SPAWN_TIME.random();
	}

//	private int counter = 3000;
	
	@Override
	protected void logicLoop(long frameTime) {
		if(InputManager.isKeyActive(InputManager.KEY_ESC)){
			InputManager.forceFlushKeys();
			stopLogic();
			return;
		}
		enemyTargetSpawnTimeCounter -= frameTime;
		if(enemyTargetSpawnTimeCounter < 0){
			enemyTargetSpawnTimeCounter = (int)ENEMY_TARGET_SPAWN_TIME.random();
			addTarget(new EnemyTarget(5));
		}
		for(Bullet bullet : bulletList){
			for(Target target : targetList){
				if(bullet.isHitting(target)){
					bullet.hit(target);
					break;
				}
				
			}
		}
//		System.out.println("bulletList"+bulletList.size());
//		System.out.println("targetList"+targetList.size());
	}

	public void addGameObject(GameObject2D obj) {
		super.addGameObjectNextTick(obj);
	}

	@Override
	protected void objectDestroyReport(GameObject2D gameObject2D) {
		removeGameObjectFromLocal(gameObject2D);
	}
	
	private void removeGameObjectFromLocal(GameObject2D obj) {
		if (obj instanceof Bullet) {
			Bullet bullet = (Bullet) obj;
			bulletList.remove(bullet);
		}
		if (obj instanceof Target) {
			Target target = (Target) obj;
			targetList.remove(target);
		}
	}
	
	public void removeGameObject(GameObject2D obj) {
		super.removeGameObjectNextTick(obj);
		removeGameObjectFromLocal(obj);
	}

	public void addBullet(Bullet bullet){
		addGameObjectNextTick(bullet);
		bulletList.add(bullet);
	}
	
	public void addTarget(Target target){
		addGameObjectNextTick(target);
		targetList.add(target);
	}

	@Override
	protected void onExitLogic() {
		gamePanel.getRenderLayers().remove(this.gameLayer);
	}
	
}
