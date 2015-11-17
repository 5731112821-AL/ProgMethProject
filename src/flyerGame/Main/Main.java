package flyerGame.Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import engine.game.InputManager;
import engine.render.GamePanel;
import engine.render.RenderLayer;
import engine.ui.Button;
import engine.ui.VisibleObject;
import flyerGame.EngineExtension.GameLogic;
import flyerGame.EngineExtension.Resources;

public class Main {
	public static void main(String[] args) {
		System.out.println("Game Start");
		
		/// Prepare Layers
		ArrayList<RenderLayer> renderLayers = new ArrayList<>();

		GamePanel gamePanel = new GamePanel(Resources.screenDimension, renderLayers);
		{/// Prepare Game Panel as Main Panel of JFrame
			InputManager.addComponent(gamePanel); 	// Bound Input System to this Panel
		}
		
		GameLogic gameLogic = new GameLogic();
		{/// Adding Game to System
			gameLogic.updatePostList.add(gamePanel);// Add gamePanel's render system to render after the GameLoop (updatePostList)
		}
		RenderLayer gameLayer = new RenderLayer(gameLogic.getRenderList());
													// Create game layer this is where the game objects are
		renderLayers.add(gameLayer);				// Add game layer to renderLayers
		
		RenderLayer uiLayer = new RenderLayer();
		renderLayers.add(uiLayer);
		{/// Adding UI to System
			uiLayer.addRenderable(new VisibleObject(Resources.ImgButton, 10, 10));
			Button button = new Button(Resources.ImgButton, 100, 10, new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("Button Clicked");
				}
			}, 2);
			uiLayer.addRenderable(button);
//			InputManager.
		}
		
		JFrame frame = new JFrame("Flyer Game");
		{/// Prepare JFrame to create a window
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
													// Close game on Exiting the window, prevent memory leak during development
			frame.setResizable(false);				// Prevent Window from being resize by the user
			frame.add(gamePanel); 					// Add Game Panel to JFrame
			frame.pack();							// Resize frame to fit Game Panel
			frame.setVisible(true);					// Show window to the User
		}

		gamePanel.requestFocus(); 					// Focus on GamePanel to allow KeyInput to work
													// This line MUST be after frame.setVisible(true);
		
		gameLogic.runGame();						// Start running the game
	}
}
