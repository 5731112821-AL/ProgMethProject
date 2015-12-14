package flyerGame.engineExtension;

import java.util.ArrayList;
import java.util.List;

import osuUtilities.OsuBeatmap;
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
import flyerGame.main.SongIndexer.Song;
import flyerGame.ui.GameHud;

public class GameLogic extends engine.game.Logic {

	private static final int TARGET_FPS = 60;

	Player player = new Player(this);
	private static final float healthRegenRatePerMiliSec = 0.002f;
	private static final float healthReduceRatePerHit = 5f;
	private static final int recoverDelayMiliSec = 100;
	private static final float comboDivider = 100;
	
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	private ArrayList<Target> targetList = new ArrayList<Target>();
	private RenderLayer gameLayer = new RenderLayer(getRenderList());;

	// Need Constructor initialization
	
	private GamePanel gamePanel;
	private List<HitCircle> hitCircles = null;
	private Song song = null;
	private long songStartTime;
	private GameHud gameHud;
	
	public GameLogic(GamePanel gamePanel, Song song, OsuBeatmap beatmap) {
		super(TARGET_FPS);

		this.gamePanel = gamePanel;
		this.hitCircles = beatmap.hitCircles;
		this.song = song;
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		gameLayer.setVisible(true);
		gamePanel.getRenderLayers().add(gameLayer);
		
		addObjectNextTick(new MovingBackground(Resources.gameBackground, 0, Range.map(Resources.gameSpeed*-1, Resources.gameFieldY, Resources.virtualScreenFieldY)));
		addTarget(player);
		
		gameHud = new GameHud(this);
		gameHud.setEnable(true);
		gamePanel.getRenderLayers().add(gameHud.getRenderLayer());

		// Do Last
		if(this.song != null){
			Resources.play(song);
			System.out.println("Play Music!");
		}else{
			System.out.println("Song not found");
		}
		songStartTime = System.currentTimeMillis();
		
		// GRID
//		addObjectNextTick(new RenderLayer.Renderable() {
//			
//			@Override
//			public void render(Graphics g) {
//				Graphics2D g2d = (Graphics2D)g;
////				for(int c=0; c<12; c++){
////					for(int c2=0; c2<9; c2++){
////						int x, y, width, height;
////						x = (int) Range.normalize(c, Resources.screenFieldX, Resources.virtualScreenGameFieldX);
////						y = (int) Range.normalize(c2, Resources.screenFieldY, Resources.virtualScreenGameFieldY);
////						width = (int) Range.scale(1, Resources.screenFieldX, Resources.virtualScreenGameFieldX);
////						height = (int) Range.scale(1, Resources.screenFieldY, Resources.virtualScreenGameFieldY);
////						if((c+c2)%2 == 0)
////							g2d.setColor(Color.BLACK);
////						else
////							g2d.setColor(Color.GRAY);
////						g2d.fillRect(x, y, width, height);
////					}
////				}
//				for(int c=0; c<12; c++){
//					for(int c2=0; c2<9; c2++){
////						if(c!=c2){
////							continue;
////						}
//						int x, y, width, height;
//						x = (int) Range.normalize(c, Resources.gameFieldX, Resources.virtualScreenGameFieldX);
//						y = (int) Range.normalize(c2, Resources.gameFieldY, Resources.virtualScreenGameFieldY);
//						width = (int) Range.scale(1, Resources.gameFieldX, Resources.virtualScreenGameFieldX);
//						height = (int) Range.scale(1, Resources.gameFieldY, Resources.virtualScreenGameFieldY);
//						g2d.setColor(Color.RED);
//						g2d.drawRect(x, y, width, height);
//						g2d.setFont(Resources.selectMapStandardFont);
//						g2d.drawString(c+","+c2, x, y);
//					}
//				}
//			}
//		});
		
	}
	
	int hitCircleCounter = 0;
	
	static private Range spawnFieldY = new Range(0, Resources.gameFieldY.max/2);
	
	public void requestClose(){
		stopLogic();
	}
	
	private float health = 100;
	
	public float getHealth(){
		return health;
	}
	
	public void increaseHealth(float in){
		if(in < 0) in = 0;
		health += in;
		if(health > 100)
			health = 100;
	}
	
	public void reduceHealth(float in){
		if(in < 0) in = 0;
		health -= in;
		if(health <= 0){
			health = 0;
			requestClose();
		}
	}
	
	@Override
	protected void stopLogic() {
		gameHud.setEnable(false);
		super.stopLogic();
	}
	
	int recoverCountdown = 0;
	
	@Override
	protected void logicLoop(long frameTime) {
		gamePanel.requestFocus();
		if(InputManager.isKeyActive(InputManager.KEY_ESC)){
			InputManager.forceFlushKeys();
			requestClose();
			return;
		}
		
		long currentSongTime = System.currentTimeMillis() - songStartTime;
		if(hitCircles != null){
			while(hitCircleCounter < hitCircles.size()){
				HitCircle currentHitCircle = hitCircles.get(hitCircleCounter);
				int offscreenSpawnOffset = 1;
				float y = Range.normalize(currentHitCircle.y, new Range(0, 512), spawnFieldY);
				long diffTime = (long) ((y+offscreenSpawnOffset)/Resources.gameSpeed);
				if(currentHitCircle.time < currentSongTime - diffTime){
					addTarget(new EnemyTarget(
							1,
							Range.normalize(currentHitCircle.x, new Range(0, 512), Resources.gameFieldX), 
							-offscreenSpawnOffset, diffTime));
					hitCircleCounter++;
				}
				else break;
			}
		}
		
		for(Bullet bullet : bulletList){
			for(Target target : targetList){
				if((target instanceof EnemyTarget) && ((EnemyTarget)target).isOpened() == false)
					continue;
				if(bullet.isHitting(target)){
					bullet.hit(target);
					break;
				}
				
			}
		}
		
		if(recoverCountdown > frameTime){
			recoverCountdown -= frameTime;
		}else{
			int toAdd = (int) (frameTime - recoverCountdown);
			this.increaseHealth(toAdd * healthRegenRatePerMiliSec);
		}
	}
	
	private int combo = 0;
	private int score = 0;

	public int getScore() {
		return score;
	}
	public int getCombo() {
		return combo;
	}

	private void hitTarget(){
		combo++;
		score += 100 * (1f+combo/comboDivider);
	}

	@Override
	protected void objectDestroyReport(GameObject2D gameObject2D) {
		if(gameObject2D instanceof EnemyTarget){
			if( ((EnemyTarget) gameObject2D).isDestoryedByLeavingTheScreen() ){
				this.reduceHealth(healthReduceRatePerHit);
				this.recoverCountdown = recoverDelayMiliSec;
				combo = 0;
			}else{
				hitTarget();
			}
		}
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
		gamePanel.getRenderLayers().remove(this.gameLayer);
	}
	
}
