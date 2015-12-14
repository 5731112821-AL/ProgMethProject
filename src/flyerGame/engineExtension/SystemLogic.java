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
import flyerGame.ui.SelectMapGui;
import flyerGame.ui.SettingGui;

public class SystemLogic extends engine.game.Logic {
	
	private static final int TARGET_FPS = 60;
	
	private Gui mainGui, settingGui, creditsGui; 
	private SelectMapGui selectMapGui;
	private GamePanel gamePanel;
	private boolean gameRunning;
	private GameLogic gameLogic;
	private int currentSongIndex;

	public SystemLogic(GamePanel gamePanel) {
		super(TARGET_FPS);
		this.gamePanel = gamePanel;
		this.gameRunning = false;
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		initUI();
		setCurrentSongIndex(0);
//		setCurrentSongIndex((int)(Math.random()*Resources.songs.size()));
	}

	public int getCurrentSongIndex() {
		return currentSongIndex;
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
		startGame, selectMap, setting, credits, back, exit
	}
	public void action(Action action){
		switch (action) {
		case startGame:
			System.out.println("Start");
			gameRunning = true;
			Song song = Resources.songs.get(currentSongIndex);
			OsuBeatmap osuBeatmap = Resources.loadOsuBeatmap(song.folderPath + song.beatmapNames.get(song.beatmapNames.size()-1));
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

		case credits:
			System.out.println("Credits");
			creditsGui.setEnable(true);
//			Resources.screenHeight = 600;
//			Resources.recalculateScreenProperties();
//			gamePanel.setPreferredSize(Resources.screenDimension);
//			Main.frame.pack();
			mainGui.setEnable(false);
			break;

		case back:
			System.out.println("Back");
			settingGui.setEnable(false);
			creditsGui.setEnable(false);
			selectMapGui.setEnable(false);
			mainGui.setEnable(true);
			break;

		case exit:
			System.out.println("Exit");
			stopLogic();
			break;

		default:
			break;
		}
	}

	private void initUI() {
		List<RenderLayer> gamePanelRenderLayers = this.gamePanel.getRenderLayers();
		
		mainGui = new MainGui(this);
		gamePanelRenderLayers.add(mainGui.getRenderLayer());
		settingGui = new SettingGui(this);
		gamePanelRenderLayers.add(settingGui.getRenderLayer());
		creditsGui = new CreditsGui(this);
		gamePanelRenderLayers.add(creditsGui.getRenderLayer());
		selectMapGui = new SelectMapGui(this);
		gamePanelRenderLayers.add(selectMapGui.getRenderLayer());

		mainGui.setEnable(true);
	}
	
	@Override
	protected void logicLoop(long frameTime) {
		gamePanel.requestFocus();
		if(!gameRunning){
			if(InputManager.isKeyActive(InputManager.KEY_ESC)){
				stopLogic();
			}
			if(mainGui.isEnable()){
				if(InputManager.isKeyActive(InputManager.KEY_ENTER)){
					action(Action.selectMap);
					InputManager.forceFlushKeys();
				}
			}
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
		}else{
			gameLogic.runLogic();
			selectMapGui.setEnable(true);
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
