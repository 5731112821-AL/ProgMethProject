package flyerGame.EngineExtension;

import java.util.ArrayList;

import engine.game.GameObject2D;
import engine.utilities.Range;
import flyerGame.GameObject.Bullet;
import flyerGame.GameObject.EnemyTarget;
import flyerGame.GameObject.Player;
import flyerGame.GameObject.Target;

public class GameLogic extends engine.game.GameLogic {

	Player player;
	
	private ArrayList<Bullet> bulletList;
	private ArrayList<Target> targetList; 

	private int enemyTargetSpawnTimeCounter;
	private static final Range ENEMY_TARGET_SPAWN_TIME = new Range(1000, 3000);
	
	@Override
	protected void gameInit() {
		bulletList = new ArrayList<>();
		targetList = new ArrayList<>();
		
		player = new Player(this);
		addTarget(player);
		
		enemyTargetSpawnTimeCounter = (int)ENEMY_TARGET_SPAWN_TIME.random();
	}

	
	@Override
	protected void gameLoop(long frameTime) {
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
	}

	public void addGameObject(GameObject2D obj) {
		super.addGameObjectNextTick(obj);
		
	}
	
	@Override
	protected void removeGameObject(GameObject2D obj) {
		super.removeGameObject(obj);
		if (obj instanceof Bullet) {
			Bullet bullet = (Bullet) obj;
			bulletList.remove(bullet);
		}
		if (obj instanceof Target) {
			Target target = (Target) obj;
			targetList.remove(target);
		}
	}

	public void addBullet(Bullet bullet){
		addGameObjectNextTick(bullet);
		bulletList.add(bullet);
	}
	
	public void addTarget(Target target){
		addGameObjectNextTick(target);
		targetList.add(target);
	}
	
}
