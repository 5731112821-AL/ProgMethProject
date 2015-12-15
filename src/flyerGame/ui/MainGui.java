package flyerGame.ui;

import java.awt.event.MouseEvent;

import engine.ui.Button;
import engine.ui.DefaultedMouseListener;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

public class MainGui extends Gui {
	

	private VisibleObject background;
	private Button startButton, settingButton, creditsButton, exitButton;

	public MainGui(SystemLogic systemLogic) {
		
		int offset = Resources.globalOffset;
		
		startButton = new Button(Resources.MainGUI.start, 660+offset, 730, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.selectMap);
			}
		});
		buttons.add(startButton);
		
		settingButton = new Button(Resources.MainGUI.setting, 830+offset, 825, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.setting);
			}
		});
		buttons.add(settingButton);
		
		creditsButton = new Button(Resources.MainGUI.credits, 860+offset, 895, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.credits);
			}
		});
		buttons.add(creditsButton);
		
		exitButton = new Button(Resources.MainGUI.exit, 900+offset, 960, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.exit);
			}
		});
		buttons.add(exitButton);
		
		background = new VisibleObject(Resources.MainGUI.background, offset, 0, 0, 0);
		renderablesToAdd.add(background);

		postConstrutorConfig();
	}

	@Override
	public void updateRenderableStates() {
		int offset = Resources.globalOffset;

		background.setScreenX(offset);
		
		startButton.setScreenX(660+offset);
		settingButton.setScreenX(830+offset);
		creditsButton.setScreenX(860+offset);
		exitButton.setScreenX(900+offset);
	}

}
