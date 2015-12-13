package flyerGame.EngineExtension;

import java.applet.AudioClip;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import osuUtilities.OsuBeatmap;
import engine.game.GameObject2D;
import engine.game.InputManager;
import engine.game.InputManager.ScreenMouseListener;
import engine.render.GamePanel;
import engine.render.RenderLayer;
import engine.ui.Button;
import flyerGame.Main.SongIndexer.Song;

public class SystemLogic extends engine.game.Logic {
	
	private static final int TARGET_FPS = 60;
	
	private RenderLayer uiLayer = new RenderLayer();
	private GamePanel gamePanel;
	private boolean gameRunning;
	private GameLogic gameLogic;
	
	public SystemLogic(GamePanel gamePanel) {
		super(TARGET_FPS);
		this.gamePanel = gamePanel;
		this.gameRunning = false;
		screenMouseListeners = new ArrayList<>();
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		initUI();
	}


	private ArrayList<ScreenMouseListener> screenMouseListeners;
	private void initUI() {
		Button startButton = new Button(Resources.StartButton, 300, 300, new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("start click");
				Resources.soundFxStart.play();
				Song song = Resources.songs.get(0);
				String folderPath = song.folderPath;
				OsuBeatmap beatmap = Resources.loadOsuBeatMap(folderPath+song.beatmapNames.get(song.beatmapNames.size()-1));
				AudioClip mapAudioClip = Resources.loadBeatmapAudioClip(folderPath+song.songName);
				gameLogic = new GameLogic(gamePanel, mapAudioClip, beatmap.hitCircles);
				gameRunning = true;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Mouse Entered start");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Mouse Exited start");
			}
		});
		screenMouseListeners.add(startButton.getScreenMouseListener());
		
		for(ScreenMouseListener screenMouseListener:screenMouseListeners)
			InputManager.addScreenMouseListener(screenMouseListener);
		
		uiLayer.addRenderable(startButton);
		this.gamePanel.getRenderLayers().add(uiLayer);
	}
	
	@Override
	protected void logicLoop(long frameTime) {
		if(!gameRunning){
			if(InputManager.isKeyActive(InputManager.KEY_ESC)){
				stopLogic();
			}
			
		}else{
			disableLogic();
			gameLogic.runLogic();
			gameRunning=false;
			enableLogic();
		}
	}

	@Override
	protected void onExitLogic() {
		
	}

	@Override
	protected void objectDestroyReport(GameObject2D gameObject2D) {
		
		
	}
	private void setActiveAllScreenMouseListener(boolean set){
		for(ScreenMouseListener screenMouseListener:screenMouseListeners)
			screenMouseListener.setActive(set);
	}
	/**
	 * Temporary disable the Logic
	 * Note: does not Block the Thread
	 */
	private void disableLogic(){
		uiLayer.setVisible(false);
		setActiveAllScreenMouseListener(false);
	}
	
	/**
	 * Get the logic back up and running 
	 */
	private void enableLogic(){
		uiLayer.setVisible(true);
		setActiveAllScreenMouseListener(true);
	}

}
