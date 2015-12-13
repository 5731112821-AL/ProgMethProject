package flyerGame.engineExtension;

import java.applet.AudioClip;
import java.util.ArrayList;
import java.util.List;

import osuUtilities.OsuBeatmap.HitCircle;
import engine.game.GameObject2D;
import engine.game.InputManager;
import engine.render.GamePanel;
import engine.render.RenderLayer;
import engine.utilities.Range;
import flyerGame.gameObject.Bullet;
import flyerGame.gameObject.EnemyTarget;
import flyerGame.gameObject.Player;
import flyerGame.gameObject.Target;

public class GameLogic extends engine.game.Logic {

	private static final int TARGET_FPS = 60;

	Player player = new Player(this);
	
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	private ArrayList<Target> targetList = new ArrayList<Target>();
	private RenderLayer gameLayer = new RenderLayer(getRenderList());;

	// Need Constructor initialization
	
	private GamePanel gamePanel;
	private List<HitCircle> hitCircles = null;
	private AudioClip song = null;
	private long songStartTime;
	
	public GameLogic(GamePanel gamePanel, AudioClip song, List<HitCircle> hitCircles) {
		super(TARGET_FPS);

		this.gamePanel = gamePanel;
		this.hitCircles = hitCircles;
		this.song = song;
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		
		gamePanel.getRenderLayers().add(gameLayer);
		
		addObjectNextTick(new MovingBackground(Resources.gameBackground, 0, Range.map(-0.001f, Resources.screenFieldY, Resources.virtualScreenFieldY)));
		addTarget(player);

		// Do Last
		songStartTime = System.currentTimeMillis();
		if(this.song != null){
			this.song.play();
			System.out.println("Play Music!");
		}else{
			System.out.println("Song not found");
		}
	}
	
	int hitCircleCounter = 0;
	
	@Override
	protected void logicLoop(long frameTime) {
		if(InputManager.isKeyActive(InputManager.KEY_ESC)){
			InputManager.forceFlushKeys();
			stopLogic();
			return;
		}
		
		long currentSongTime = System.currentTimeMillis() - songStartTime;
		if(hitCircles != null){
			while(hitCircleCounter < hitCircles.size()){
				HitCircle currentHitCircle = hitCircles.get(hitCircleCounter);
				if(currentHitCircle.time < currentSongTime){
					Resources.soundFxDrum.play();
					addTarget(new EnemyTarget(
							1, 
							Range.normalize(currentHitCircle.x, new Range(0, 512), Resources.screenFieldX), 
							Range.normalize(currentHitCircle.y, new Range(0, 512), Resources.screenFieldY)));
					hitCircleCounter++;
				}
				else break;
			}
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

	@Override
	protected void objectDestroyReport(GameObject2D gameObject2D) {
		removeObjectFromLocal(gameObject2D);
	}
	
	private void removeObjectFromLocal(Object obj) {
		if (obj instanceof Bullet) {
			Bullet bullet = (Bullet) obj;
			bulletList.remove(bullet);
		}
		if (obj instanceof Target) {
			Target target = (Target) obj;
			targetList.remove(target);
		}
	}
	
	public void removeObject(Object obj) {
		super.removeObjectNextTick(obj);
		removeObjectFromLocal(obj);
	}

	public void addBullet(Bullet bullet){
		addObjectNextTick(bullet);
		bulletList.add(bullet);
	}
	
	public void addTarget(Target target){
		addObjectNextTick(target);
		targetList.add(target);
	}

	@Override
	protected void onExitLogic() {
		this.song.stop();
		gamePanel.getRenderLayers().remove(this.gameLayer);
	}
	
}
