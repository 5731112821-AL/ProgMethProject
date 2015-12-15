package flyerGame.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.ui.Align;
import engine.ui.Button;
import engine.ui.DynamicUiLabel;
import engine.ui.DynamicUiLabel.GetString;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;

public class ScoreReportGui extends Gui{

	private Button backButton;
	private VisibleObject background;
	private DynamicUiLabel scoreLabel, maxComboLabel, scoreString, maxComboString;
	private int scoreValue, maxComboValue;

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public void setMaxComboValue(int maxComboValue) {
		this.maxComboValue = maxComboValue;
	}

	public ScoreReportGui(SystemLogic systemLogic) {
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
		
		background = new VisibleObject(Resources.scoreReportBackground, offset, 0, 0, 0);
		renderablesToAdd.add(background);
		
		scoreLabel = new DynamicUiLabel(new GetString() {

			private int run = 0;
			private int lastValue = 0;

			@Override
			public String getString() {
				if(lastValue != scoreValue){
					lastValue = scoreValue;
					run = 0;
				}
				run += (lastValue - run) / 10;
				if(lastValue - run < 50){
					run = lastValue;
				}
				return ""+run;
			}
		}, 1500+offset, 540, Resources.scoreFont, Color.WHITE, Align.right);
		renderablesToAdd.add(scoreLabel);

		maxComboLabel = new DynamicUiLabel(new GetString() {

			private int run = 0;
			private int lastValue = 0;

			@Override
			public String getString() {
				if(lastValue != maxComboValue){
					lastValue = maxComboValue;
					run = 0;
				}
				run += (lastValue - run) / 10;
				if(lastValue - run < 10){
					run = lastValue;
				}
				return run+"x";
			}
		}, 1500+offset, 640, Resources.scoreFont, Color.WHITE, Align.right);
		renderablesToAdd.add(maxComboLabel);
		
		scoreString = new DynamicUiLabel(new GetString() {

//			private String toSet = "Score";
//			private int counter = 0;
			
			@Override
			public String getString() {
				return "Score";
//				if(counter/5 < toSet.length())
//					counter++;
//				return toSet.substring(0, counter/5);
			}
		}, 420+offset, 540, Resources.scoreFont, Color.WHITE, Align.left);
		renderablesToAdd.add(scoreString);
		
		maxComboString = new DynamicUiLabel(new GetString() {
			
//			private String toSet = "Max Combo";
//			private int counter = 0;
			
			@Override
			public String getString() {
				return "Max Combo";
//				if(counter/5 < toSet.length())
//					counter++;
//				return toSet.substring(0, counter/5);
			}
		}, 420+offset, 640, Resources.scoreFont, Color.WHITE, Align.left);
		renderablesToAdd.add(maxComboString);
		
		postConstrutorConfig();
		
	}

	@Override
	public void updateRenderableStates() {
		int offset = Resources.globalOffset;
		
		backButton.setScreenX(280+offset);
		background.setScreenX(offset);
		scoreLabel.setX(1500+offset);
		maxComboLabel.setX(1500+offset);
		scoreString.setX(420+offset);
		maxComboString.setX(420+offset);
	}

}
