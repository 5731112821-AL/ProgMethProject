package flyerGame.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import osuUtilities.OsuBeatmap;
import osuUtilities.OsuBeatmap.Beats;
import engine.ui.Align;
import engine.ui.Button;
import engine.ui.DefaultedMouseListener;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;
import flyerGame.main.SongIndexer.Song;

/**
 * Gui Class of the Main GUI Page
 * (The welcome screen)
 * @author L2k-nForce
 */
public class MainGui extends Gui {
	

	private VisibleObject background, logoText, logoCircle;
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
		
		logoCircle = new DancingVisibleObject(Resources.MainGUI.logoCircle, 960+offset, 347, 0, 0, Align.center);
		renderablesToAdd.add(logoCircle);
		
		logoText = new VisibleObject(Resources.MainGUI.logoText, 960+offset, 347, 0, 0, Align.center);
		renderablesToAdd.add(logoText);

		postConstrutorConfig();
	}
	
	private static class DancingVisibleObject extends VisibleObject{

		public DancingVisibleObject(BufferedImage bufferedImage, int screenX,
				int screenY, int width, int height, Align align) {
			super(bufferedImage, screenX, screenY, width, height, align);
		}
		
		@Override
		public void render(Graphics g) {
//			System.out.println(Thread.currentThread().getName());
			Graphics2D g2d = (Graphics2D)g;
			Song song = Resources.getSongPlaying();
			if(song != null){
//				OsuBeatmap beatmap = Resources.loadOsuBeatmap(song,song.beatmapNames.size()-1);
//				long bpm = (long) beatmap.beats.get(0).mpb;
//				long time = Resources.songPlayedfor();
//				for(int c=1; c<beatmap.beats.size() && time < beatmap.beats.get(c).offset; c++){
//					bpm = (long) beatmap.beats.get(c).mpb;
//				}
//				time %= bpm;
//				double scale = Resources.scale + (1-Math.sqrt(1-Math.pow((double)time/bpm, 2)))/100;
//				AffineTransform transform = new AffineTransform();
//				transform.translate(-getWidth()/2, -getHeight()/2);
//				transform.scale(3, 3);	
//				transform.translate(getWidth()*scale/2, getHeight()*scale/2);
//				g2d.setTransform(transform);
			}
			super.render(g);
			g2d.setTransform(Resources.scaledTransform);
		}
	}

	@Override
	public void updateRenderableStates() {
		int offset = Resources.globalOffset;

		background.setScreenX(offset);
		logoCircle.setScreenX(960+offset);
		logoText.setScreenX(960+offset);
		
		startButton.setScreenX(660+offset);
		settingButton.setScreenX(830+offset);
		creditsButton.setScreenX(860+offset);
		exitButton.setScreenX(900+offset);
	}

}
