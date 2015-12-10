package flyerGame.EngineExtension;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import engine.game.GameObject2D;
import engine.game.InputManager;
import engine.game.InputManager.MiniMouseListener;
import engine.game.InputManager.ScreenMouseListener;
import engine.render.GamePanel;
import engine.render.RenderLayer;
import engine.ui.Button;

public class SystemLogic extends engine.game.Logic {
	
	private static final int TARGET_FPS = 60;
	
	private RenderLayer uiLayer = new RenderLayer();
	private GamePanel gamePanel;
	private boolean gameRunning;
	private GameLogic gameLogic;
	private ArrayList<ScreenMouseListener> screenMouseListeners;
	
	public SystemLogic(GamePanel gamePanel) {
		super(TARGET_FPS);
		this.gamePanel = gamePanel;
		this.gameRunning = false;
		screenMouseListeners = new ArrayList<>();
		{/// Adding Game to System
			updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		Button startButton = new Button(Resources.StartButton, 300, 300, new MiniMouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("start click");
				gameLogic = new GameLogic(gamePanel);
				gameRunning=true;
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
//		System.out.println("Looping");
		if(gameRunning){
			disableLogic();
			gameLogic.runGame();
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
	private void disableLogic(){
		uiLayer.setVisible(false);
		setActiveAllScreenMouseListener(false);
	}
	private void enableLogic(){
		uiLayer.setVisible(true);
		setActiveAllScreenMouseListener(true);
	}

}
