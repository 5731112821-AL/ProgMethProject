package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.ui.Button;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

public class SelectMapGui extends Gui {

	private Button prevDiffButton, nextDiffButton;
	
	public SelectMapGui(SystemLogic systemLogic) {
		int offset = -240;
		Button backButton = new Button(Resources.SelectMapGUI.back, 820+offset, 950, new MouseListener() {
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
		
		Button nextSongButton = new Button(Resources.SelectMapGUI.sideArrow, 850+offset, 355, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.nextSong);
			}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
		});
		buttons.add(nextSongButton);

		nextDiffButton = new Button(Resources.SelectMapGUI.rightArrow, 706+offset, 780, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				nextDiffButton.setClicked(false);
				systemLogic.action(Action.setting);
				}
			@Override public void mousePressed(MouseEvent e) {
				nextDiffButton.setClicked(true);}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {
//				System.out.println("hover"); TODO
				nextDiffButton.setHover(true);}
			@Override public void mouseExited(MouseEvent e) {
				nextDiffButton.setHover(false);}
		});
		buttons.add(nextDiffButton);
		
		prevDiffButton = new Button(Resources.SelectMapGUI.leftArrow, 370+offset, 780, new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				prevDiffButton.setClicked(false);
				systemLogic.action(Action.setting);
			}
			@Override public void mousePressed(MouseEvent e) {
				prevDiffButton.setClicked(true);}
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {
				prevDiffButton.setHover(true);}
			@Override public void mouseExited(MouseEvent e) {
				prevDiffButton.setHover(false);}
		});
		buttons.add(prevDiffButton);
		
		visibleObjects.add(new VisibleObject(Resources.SelectMapGUI.background, offset, 0, 0, 0));
		visibleObjects.add(new VisibleObject(Resources.SelectMapGUI.tag, 880+offset, 615, 0, 0));
		
		initButtonsAndVisibleObjects();
	}

}
