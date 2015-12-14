package flyerGame.ui;

import java.awt.event.MouseEvent;
import java.util.Map;

import osuUtilities.OsuBeatmap;
import engine.ui.Button;
import engine.ui.DefaultedMouseListener;
import engine.ui.ScaledImage;
import engine.ui.UiLabel;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic;
import flyerGame.engineExtension.SystemLogic.Action;
import flyerGame.main.SongIndexer.Song;

public class SelectMapGui extends Gui {

	private Button prevDiffButton, nextDiffButton, prevSongButton, nextSongButton, backButton;
	private UiLabel songName, artistName, creatorName;
	private int songIndex;
	private int beatmapIndex;
	private ScaledImage[] preview = new ScaledImage[4];
	
	public SelectMapGui(SystemLogic systemLogic) {
		int offset = Resources.globalOffset;
		backButton = new Button(Resources.SelectMapGUI.back, 820+offset, 950, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.action(Action.back);
			}
		});
		buttons.add(backButton);

		nextSongButton = new Button(Resources.SelectMapGUI.sideArrow, 850+offset, 355, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.setCurrentSongIndex(systemLogic.getCurrentSongIndex()+1);
			}
		});
		buttons.add(nextSongButton);

		prevSongButton = new Button(Resources.SelectMapGUI.sideArrow, 400+offset, 355, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				systemLogic.setCurrentSongIndex(systemLogic.getCurrentSongIndex()-1);
			}
		});
//		buttons.add(prevSongButton); TODO

		nextDiffButton = new Button(Resources.SelectMapGUI.rightArrow, 706+offset, 780, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
//				systemLogic.action(Action.setting); TODO
			}
		});
		buttons.add(nextDiffButton);
		
		prevDiffButton = new Button(Resources.SelectMapGUI.leftArrow, 370+offset, 780, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
//				systemLogic.action(Action.setting); TODO
			}
		});
		buttons.add(prevDiffButton);
		
		renderablesToAdd.add(new VisibleObject(Resources.SelectMapGUI.background, offset, 0, 0, 0));
		renderablesToAdd.add(new VisibleObject(Resources.SelectMapGUI.tag, 880+offset, 615, 0, 0));

		preview[0] = new ScaledImage(null, 295+offset, 285, 540, 405);
		renderablesToAdd.add(preview[0]);
		preview[1] = new ScaledImage(null, 910+offset, 285, 324, 243);
		renderablesToAdd.add(preview[1]);
		preview[2] = new ScaledImage(null, 1255+offset, 285, 324, 243);
		renderablesToAdd.add(preview[2]);
		preview[3] = new ScaledImage(null, 1600+offset, 285, 324, 243);
		renderablesToAdd.add(preview[3]);
		songName = new UiLabel("", 1010+offset ,745, Resources.selectMapLargeFont);
		renderablesToAdd.add(songName);
		artistName = new UiLabel("", 1150+offset ,790, Resources.selectMapStandardFont);
		renderablesToAdd.add(artistName);
		creatorName = new UiLabel("", 1150+offset ,830, Resources.selectMapStandardFont);
		renderablesToAdd.add(creatorName);
		
		postConstrutorConfig();
	}
	
	private void setBeatmapIndex(int index){
		this.beatmapIndex = index;
		prevDiffButton.setEnable(index > 0);
		nextDiffButton.setEnable(index < Resources.songs.get(this.songIndex).beatmapNames.size()-1);
	}
	
	private void textReload(){
		Song song = Resources.songs.get(this.songIndex);
		OsuBeatmap beatmap = Resources.loadOsuBeatmap(song, beatmapIndex);
		
		Map<String, String> metadata = beatmap.data.get("Metadata");
		this.songName.setString(metadata.get("Title"));
		this.artistName.setString(metadata.get("Artist"));
		this.creatorName.setString(metadata.get("Creator"));
		
	}
	private void imageReload(){
		Song song = Resources.songs.get(this.songIndex);
		OsuBeatmap beatmap = Resources.loadOsuBeatmap(song, this.beatmapIndex);
		preview[0].setImage(Resources.loadBeatmapImage(song, beatmap));
		for(int c=1; c<4; c++){
			try{
				song = Resources.songs.get(this.songIndex+c);
			}catch(IndexOutOfBoundsException e){
				song = null;
			}
			beatmap = Resources.loadOsuBeatmap(song, 0);
			preview[c].setImage(Resources.loadBeatmapImage(song, beatmap));
		}
	}
	
	public void setSongIndex(int index){
		System.out.println("setSongIndex " + index);
		
		if(index != this.songIndex){
			this.beatmapIndex = 0;
		}
		
		this.songIndex = index;
		prevSongButton.setEnable(index > 0);
		nextSongButton.setEnable(index < Resources.songs.size()-1);
		imageReload();
		textReload();
	}

}
