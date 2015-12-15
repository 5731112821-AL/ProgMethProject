package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.render.InfiniteTile;
import engine.ui.Button;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.MovingBackground;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

/**
 * Gui Class of the Credits GUI Page
 * @author L2k-nForce
 */
public class CreditsGui extends Gui{

	private Button backButton;
	private VisibleObject background;
	private MovingBackground script;
	
	public CreditsGui(SystemLogic systemLogic) {
		int offset = Resources.globalOffset;
		backButton = new Button(Resources.CreditsGUI.back, 280+offset, 920, new MouseListener() {
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
		
		background = new VisibleObject(Resources.CreditsGUI.background, offset, 0, 0, 0);
		renderablesToAdd.add(background);
		script = new MovingBackground(new InfiniteTile(Resources.CreditsGUI.script), 0, 0.02f, -offset, 0);
		renderablesToAdd.add(script);
		systemLogic.addObjectNextTick(script);
		
		postConstrutorConfig();
	}
	
	@Override
	public void setEnable(boolean set) {
		super.setEnable(set);
		if(set){
			script.setY(0);
		}
	}

	@Override
	public void updateRenderableStates() {

		int offset = Resources.globalOffset;
		
		background.setScreenX(offset);
		script.setX(-offset);
		backButton.setScreenX(280+offset);
	}

}
