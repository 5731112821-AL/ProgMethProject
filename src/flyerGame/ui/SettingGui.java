package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.game.InputManager;
import engine.ui.Button;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

public class SettingGui extends Gui {

	public SettingGui(SystemLogic systemLogic) {
		int offset = -240;
		Button backButton = new Button(Resources.SettingGUI.back, 820+offset, 950, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.back);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		buttons.add(backButton);
		
		visibleObjects.add(new VisibleObject(Resources.SettingGUI.background, offset, 0, 0, 0));
		
		initButtonsAndVisibleObjects();
	}

}
