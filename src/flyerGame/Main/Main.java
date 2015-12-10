package flyerGame.Main;

import javax.swing.JFrame;

import engine.game.InputManager;
import engine.render.GamePanel;
import flyerGame.EngineExtension.Resources;
import flyerGame.EngineExtension.SystemLogic;

public class Main {
	
	public static void main(String[] args) {
		
		System.out.println("Game Start");
		
		GamePanel gamePanel = new GamePanel(Resources.screenDimension);
		{/// Prepare Game Panel as Main Panel of JFrame
			InputManager.addComponent(gamePanel); 	// Bound Input System to this Panel
		}

		SystemLogic systemLogic = new SystemLogic(gamePanel);
		
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
		systemLogic.runGame();
	}
}
