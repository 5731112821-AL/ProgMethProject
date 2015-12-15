package flyerGame.main;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import engine.game.InputManager;
import engine.render.GamePanel;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;

public class Main {
	
	public static JFrame frame;
	public static GamePanel gamePanel;
	
	
	/**
	 * Creates {@link JFrame}, {@link GamePanel}, {@link SystemLogic}<br>
	 * And start running {@link SystemLogic} after finished initializing 
	 * the game. <br>
	 * Once {@link SystemLogic} returns the application is cloesed.
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Game Start");

		System.out.println("creating gamepanel");
		System.out.println(Resources.screenDimension);
		gamePanel = new GamePanel(Resources.screenDimension, Resources.scale);
		System.out.println("created gamepanel");
		{/// Prepare Game Panel as Main Panel of JFrame
			InputManager.addComponent(gamePanel); 	// Bound Input System to this Panel
		}

		SystemLogic systemLogic = new SystemLogic(gamePanel);

		frame = new JFrame("Beats Arcade");
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
		systemLogic.runLogic();						// This methond blocks the cpu until the game is exited
		
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}
