package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.ui.Button;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

public class MainGui extends Gui {

	public MainGui(SystemLogic systemLogic) {
		int offset = -240;
		Button startButton = new Button(Resources.MainGUI.start, 660+offset, 730, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.selectMap);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		buttons.add(startButton);
		
		Button settingButton = new Button(Resources.MainGUI.setting, 830+offset, 825, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.setting);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		buttons.add(settingButton);
		
		Button creditsButton = new Button(Resources.MainGUI.credits, 860+offset, 895, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.credits);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		buttons.add(creditsButton);
		
		Button exitButton = new Button(Resources.MainGUI.exit, 900+offset, 960, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.exit);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		buttons.add(exitButton);
		
		visibleObjects.add(new VisibleObject(Resources.MainGUI.background, offset, 0, 0, 0));

		initButtonsAndVisibleObjects();
	}

}
