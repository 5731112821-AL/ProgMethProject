package flyerGame.engineExtension;

import java.util.List;

import osuUtilities.OsuBeatmap;
import engine.game.GameObject2D;
import engine.game.InputManager;
import engine.render.GamePanel;
import engine.render.RenderLayer;
import flyerGame.main.SongIndexer.Song;
import flyerGame.ui.CreditsGui;
import flyerGame.ui.Gui;
import flyerGame.ui.MainGui;
import flyerGame.ui.ScoreReportGui;
import flyerGame.ui.SelectMapGui;
import flyerGame.ui.SettingGui;

public class SystemLogic extends engine.game.Logic {
	
	private static final int TARGET_FPS = 40;
	
	private Gui mainGui, settingGui, creditsGui;
	private ScoreReportGui scoreReportGui;
	private SelectMapGui selectMapGui;
	private GamePanel gamePanel;
	private boolean gameRunning;
	private GameLogic gameLogic;
	public int currentSongIndex;
	private int currentBeatmapIndex;

	public SystemLogic(GamePanel gamePanel) {
		super(TARGET_FPS);
		this.gamePanel = gamePanel;
		this.gameRunning = false;
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		initUI();
//		setCurrentSongIndex(0);
		setCurrentSongIndex((int)(Math.random()*Resources.songs.size()));
	}

	public int getCurrentSongIndex() {
		return currentSongIndex;
	}
	public int getCurrentBeatmapIndex() {
		return currentBeatmapIndex;
	}
	public void setCurrentBeatmapIndex(int currentBeatmapIndex){
		if(currentBeatmapIndex>=0 && currentBeatmapIndex<Resources.songs.get(this.currentSongIndex).beatmapNames.size()){
			System.out.println("current beatmap = "+currentBeatmapIndex);
			selectMapGui.setBeatmapIndex(currentBeatmapIndex);
			this.currentBeatmapIndex=currentBeatmapIndex; // TODO
		}
	}

	public void setCurrentSongIndex(int currentSongIndex) {
		if(currentSongIndex>=0 && currentSongIndex<Resources.songs.size()){
			Resources.stop(Resources.songs.get(this.currentSongIndex));
			this.currentSongIndex = currentSongIndex;
			selectMapGui.setSongIndex(currentSongIndex);
			Resources.play(Resources.songs.get(currentSongIndex));
		}
	}

	public static enum Action{
		startGame, selectMap, setting, screenRenderingUpdate, credits, back, exit
	}
	public void action(Action action){
		switch (action) {
		case startGame:
			System.out.println("Start");
			gameRunning = true;
			Song song = Resources.songs.get(currentSongIndex);
			OsuBeatmap osuBeatmap = Resources.loadOsuBeatmap(song.folderPath + song.beatmapNames.get(this.currentBeatmapIndex));
			gameLogic = new GameLogic(
					gamePanel, 
					song, 
					osuBeatmap);
			gameRunning = true;
			selectMapGui.setEnable(false);
			break;
			
		case selectMap:
			System.out.println("Select Map");
			selectMapGui.setEnable(true);
			mainGui.setEnable(false);
			break;

		case setting:
			System.out.println("Setting");
			settingGui.setEnable(true);
			mainGui.setEnable(false);
			break;
			
		case screenRenderingUpdate:
			System.out.println("updateRenderableStates");
			mainGui.updateRenderableStates();
			settingGui.updateRenderableStates();
			creditsGui.updateRenderableStates();
			selectMapGui.updateRenderableStates();
			scoreReportGui.updateRenderableStates();
			break;

		case credits:
			System.out.println("Credits");
			creditsGui.setEnable(true);
			mainGui.setEnable(false);
			break;

		case back:
			System.out.println("Back");
			if(scoreReportGui.isEnable()){
				selectMapGui.setEnable(true);
				scoreReportGui.setEnable(false);
			}
			else{
				settingGui.setEnable(false);
				creditsGui.setEnable(false);
				selectMapGui.setEnable(false);
				mainGui.setEnable(true);
			}
			break;

		case exit:
			System.out.println("Exit");
			stopLogic();
			break;

		default:
			break;
		}
		System.out.println(Resources.scale);
	}

	private void initUI() {
		List<RenderLayer> gamePanelRenderLayers = this.gamePanel.getRenderLayers();
		
		mainGui = new MainGui(this);
		gamePanelRenderLayers.add(mainGui.getRenderLayer());
		int[] data = {1,2};
		settingGui = new SettingGui(this, data);
		gamePanelRenderLayers.add(settingGui.getRenderLayer());
		creditsGui = new CreditsGui(this);
		gamePanelRenderLayers.add(creditsGui.getRenderLayer());
		selectMapGui = new SelectMapGui(this);
		gamePanelRenderLayers.add(selectMapGui.getRenderLayer());
		scoreReportGui = new ScoreReportGui(this);
		gamePanelRenderLayers.add(scoreReportGui.getRenderLayer());

		mainGui.setEnable(true);
		
		if(Resources.debugMode){
			mainGui.setEnable(false);
			scoreReportGui.setEnable(true);
			scoreReportGui.setMaxComboValue(1000);
			scoreReportGui.setScoreValue(1000000);
		}
	}
	
	@Override
	protected void logicLoop(long frameTime) {
		gamePanel.requestFocus();
		if(!gameRunning){
			if(selectMapGui.isEnable()){
				if(InputManager.isKeyActive(InputManager.KEY_RIGHT_ARROW)){
					System.out.println("next song");
					setCurrentSongIndex(getCurrentSongIndex()+1);
					InputManager.forceFlushKeys();
				}
				else if(InputManager.isKeyActive(InputManager.KEY_LEFT_ARROW)){
					setCurrentSongIndex(getCurrentSongIndex()-1);
					InputManager.forceFlushKeys();
				}
				else if(InputManager.isKeyActive(InputManager.KEY_ENTER)){
					action(Action.startGame);
				}
			}
			
			if(mainGui.isEnable()){
				if(InputManager.isKeyActive(InputManager.KEY_ENTER)){
					action(Action.selectMap);
					InputManager.forceFlushKeys();
				}
				if(InputManager.isKeyActive(InputManager.KEY_ESC)){
					stopLogic();
				}
			}
			else{
				if(InputManager.isKeyActive(InputManager.KEY_ESC)){
					action(Action.back);
					InputManager.forceFlushKeys();
				}
			}
		}else{
			gameLogic.runLogic();
			scoreReportGui.setEnable(true);
			scoreReportGui.setMaxComboValue(gameLogic.getMaxCombo());
			scoreReportGui.setScoreValue(gameLogic.getScore());
			gameLogic = null;
			gameRunning=false;
		}
	}

	@Override
	protected void onExitLogic() {}

	@Override 
	protected void objectDestroyReport(GameObject2D gameObject2D) {}
//	private void setActiveAllScreenMouseListener(boolean set){
//		for(ScreenMouseListener screenMouseListener:screenMouseListeners)
//			screenMouseListener.setActive(set);
//	}
//	/**
//	 * Temporary disable the Logic
//	 * Note: does not Block the Thread
//	 */
//	private void disableLogic(){
//		mainGuiLayer.setVisible(false);
//		setActiveAllScreenMouseListener(false);
//	}
//	
//	/**
//	 * Get the logic back up and running 
//	 */
//	private void enableLogic(){
//		mainGuiLayer.setVisible(true);
//		setActiveAllScreenMouseListener(true);
//	}

}
