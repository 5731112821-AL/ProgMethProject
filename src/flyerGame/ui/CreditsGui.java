package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.ui.Button;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

public class CreditsGui extends Gui{

	private Button backButton;
	
	public CreditsGui(SystemLogic systemLogic) {
		int offset = Resources.globalOffset;
		backButton = new Button(Resources.SelectMapGUI.back, 820+offset, 950, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				backButton.setClicked(false);
				systemLogic.action(Action.back);
			}
			@Override public void mousePressed(MouseEvent e) {
				backButton.setClicked(true);}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {
				backButton.setHover(true);}
			@Override public void mouseExited(MouseEvent e) {
				backButton.setHover(false);}
		});
		buttons.add(backButton);
		buttons.add(backButton);
		
		renderablesToAdd.add(new VisibleObject(Resources.SettingGUI.background, offset, 0, 0, 0));
		
		postConstrutorConfig();
	}

}