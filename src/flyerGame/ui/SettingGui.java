package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import engine.render.GamePanel;
import engine.ui.Align;
import engine.ui.Button;
import engine.ui.DefaultedMouseListener;
import engine.ui.DynamicUiLabel;
import engine.ui.DynamicUiLabel.GetString;
import engine.ui.UiLabel;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;
import flyerGame.main.Main;

public class SettingGui extends Gui {
	
	/*
	private int currentRatioOption,currentResolutionOption;
	private static final int[] SCREEN_HEIGHT_FOR_4_3 = {480,600,768,864,960,1050};
	private static final int[] SCREEN_HEIGHT_FOR_16_9= {480,720,768,900,1080};
	private Button leftRatioButton , rightRatioButton,leftResolutionButton,rightResolutionButton;
	private UiLabel ratioSetting,resolutionSetting;
	*/
	
	private int offset = Resources.globalOffset;
	private SystemLogic systemLogic;
	private int[] indexs;
	private int[][] options = {
			{
				12,16
			},
			{
				600,720,768,900,1080
			}
	};
	private Button backButton;
	private SettingSelector aspectRatio, resolution;
	private VisibleObject background;
	
	private void updateScreen(){
		int c=0;
		int ratioWidth = options[c][indexs[c]];
		c++;
		int screenHeight = options[c][indexs[c]];
		Resources.recalculateScreenProperties(ratioWidth, screenHeight);
		Main.gamePanel.setPreferredSize(Resources.screenDimension);
		Main.frame.pack();
		systemLogic.action(Action.screenRenderingUpdate);
	}
	
	@Override
	public void updateRenderableStates() {
		this.offset = Resources.globalOffset;
		background.setScreenX(offset);
		backButton.setScreenX(820+offset);
		aspectRatio.updateRenderableStates();
		resolution.updateRenderableStates();
	}
	
	public SettingGui(SystemLogic systemLogic, int[] indexs) {

		this.systemLogic = systemLogic;
		this.indexs = indexs;
		if(indexs.length != options.length)
			throw new RuntimeException("indexs length do not match options");
		
		int c=0;
		
		/// Add background
		background = new VisibleObject(Resources.SettingGUI.background, offset, 0, 0, 0);
		renderablesToAdd.add(background);
		/// Add back button
		backButton = new Button(Resources.SelectMapGUI.back, 820+offset, 950, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.back);
			}
		});
		buttons.add(backButton);

		aspectRatio = new SettingSelector("Aspect Ratio", 
		   new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				indexs[aspectRatio.indexNum]--;
				updateScreen();
			}	
		}, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				indexs[aspectRatio.indexNum]++;
				updateScreen();
			}	
		}, new GetString() {
			
			@Override
			public String getString() {
				if(indexs[aspectRatio.indexNum]==0)
					return "4 : 3";
				else
					return "16 : 9";
			}
		}, c++);
		
		resolution = new SettingSelector("Resolution",
			new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				indexs[resolution.indexNum]--;
				updateScreen();
			}	
		}, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				indexs[resolution.indexNum]++;
				updateScreen();
			}	
		}, new GetString() {
			
			@Override
			public String getString() {
				int width, height;
				height = options[resolution.indexNum][indexs[resolution.indexNum]];
				width = height*Resources.ratioWidth/Resources.ratioHeight;
				return "" + width + " X " + height;
			}
		}, c++);
		
		if(c != options.length)
			throw new RuntimeException("options created do not match data");
		postConstrutorConfig();
	}

	private class SettingSelector{
		
		private UiLabel optionLabel;
		private DynamicUiLabel dynamicUiLabel;
		protected Button nextButton, prevButton;
		
		public int indexNum;
		public void updateRenderableStates(){
			prevButton.setEnable(indexs[this.indexNum]>0);
			nextButton.setEnable(indexs[this.indexNum]<options[this.indexNum].length-1);
			
			optionLabel.setX(380+offset);
			prevButton.setScreenX(1060+offset);
			dynamicUiLabel.setX(1301+offset);
			nextButton.setScreenX(1490+offset);
		}
		
		public SettingSelector(String optionName, MouseListener prevBL, MouseListener nextBL, GetString getString, int indexNum) {
			this.indexNum = indexNum;
			int y = 273 + indexNum*100;
			int yText = y + 60;
			optionLabel = new UiLabel(optionName, 380+offset, yText,  Resources.settingStandardFont, Resources.settingFontColor);
			prevButton = new Button(Resources.SettingGUI.leftArrow , 1060+offset, y, prevBL);
			dynamicUiLabel = new DynamicUiLabel(getString, 1301+offset, yText, Resources.settingStandardFont, Resources.settingFontColor, Align.center);
			nextButton = new Button(Resources.SettingGUI.rightArrow, 1490+offset, y, nextBL);
			
			buttons.add(nextButton);
			buttons.add(prevButton);
			renderablesToAdd.add(dynamicUiLabel);
			renderablesToAdd.add(optionLabel);
			updateRenderableStates();
		}
	}

}
