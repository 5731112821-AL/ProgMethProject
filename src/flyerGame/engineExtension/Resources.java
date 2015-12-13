package flyerGame.engineExtension;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import osuUtilities.OsuBeatmap;
import engine.render.InfiniteTile;
import engine.render.SpriteMap;
import engine.utilities.Range;
import flyerGame.main.SongIndexer;
import flyerGame.main.SongIndexer.Song;

public class Resources {
	private Resources() {
	}

	public static final int
		virtualScreenWidth = 1440,
		virtualScreenHeight = 1080;
	public static final int
		screenWidth = 1024,
		screenHeight = 768;
	
	public static final float
		scale = (float)screenHeight/1080;
	
	public static final Range
			screenFieldX = new Range(0, 12),
			screenFieldY = new Range(0, 9),
			screenFieldExX = new Range(screenFieldX.min-1, screenFieldX.max+1),
			screenFieldExY = new Range(screenFieldY.min-1, screenFieldY.max+1),
			virtualScreenFieldX = new Range(0, virtualScreenWidth),
			virtualScreenFieldY = new Range(0, virtualScreenHeight),
			trueScreenFieldX = new Range(0, screenWidth),
			trueScreenFieldY = new Range(0, screenHeight);
	public static final Dimension virtualScreenDimension = new Dimension(virtualScreenWidth, virtualScreenHeight);
	public static final Dimension screenDimension = new Dimension(screenWidth, screenHeight);

	public static enum Axis{
		X, Y
	}
	
//	public static float normalizeToVirtualScreen(float in, Axis axis) {
//		if(axis == Axis.X)
//			return (in * trueScreenFieldX.max / screenFieldX.max);
//		else
//			return (in * trueScreenFieldY.max / screenFieldY.max);
//	}
//
//	public static float normalizeToGame(int in, Axis axis) {
//		if(axis == Axis.X)
//			return (in * screenFieldX.max / trueScreenFieldX.max);
//		else
//			return (in * screenFieldY.max / trueScreenFieldY.max);
//	}
//
//	public static Range normalizeToVirtualScreen(Range in, Axis axis) {
//		if(axis == Axis.X)
//			return new Range(Range.normalize(in.min, screenFieldX, trueScreenFieldX),
//					Range.normalize(in.max, screenFieldX, trueScreenFieldX));
//		else
//			return new Range(Range.normalize(in.min, screenFieldY, trueScreenFieldY),
//					Range.normalize(in.max, screenFieldY, trueScreenFieldY));
//	}

	private static ClassLoader loader = Resources.class.getClassLoader();
	private static String spriteFolderPath = "res/images/sprite/";

	public static AudioClip soundFxStart;
	public static AudioClip soundFxDrum;

	public static InfiniteTile gameBackground;
	public static BufferedImage backButton;

	static {
		// Load images and SoundFx
		try {
			gameBackground = new InfiniteTile(ImageIO.read(loader
					.getResource("res/images/game_background.png")));
			backButton = ImageIO.read(loader.getResource("res/images/back_button.png"));

			soundFxStart = Applet.newAudioClip((loader
					.getResource("res/soundFX/gos.wav")));
			soundFxDrum = Applet.newAudioClip((loader
					.getResource("res/soundFX/drum-hitnormal.wav")));

			System.out.println("Resouces Sucessfully loaded YAY !!!");
		} catch (Exception e) {
			System.out.println("Resouces Failed to load !!!");
			e.printStackTrace();
		}
		// Beatmap Indexing
		try {
			InputStream inputStream = loader
					.getResourceAsStream("res/songs/songIndex.dat");
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			Object obj = ois.readObject();
			songs = (List<Song>) obj;
			System.out.println("Beatmaps indexed !!!");
		} catch (Exception e) {
			System.out.println("Failed to index Beatmaps !!!");
			e.printStackTrace();
		}
	}

	public static class MainGUI{
		public static BufferedImage background, start, setting, credits, exit;
		static{
			try {
				System.out.println("Loading MainGUI data");
				String folderPath = "res/images/main_gui/";
				background = ImageIO.read(loader.getResource(folderPath+"main_background.png"));
				start = ImageIO.read(loader.getResource(folderPath+"main_startgame.png"));
				setting = ImageIO.read(loader.getResource(folderPath+"main_setting.png"));
				credits = ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				exit = ImageIO.read(loader.getResource(folderPath+"main_exit.png"));
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
				start = null;
				setting = null;
				credits = null;
				exit = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}
	public static class SelectMapGUI{
		public static BufferedImage background, tag, sideArrow, back;
		public static SpriteMap rightArrow, leftArrow;
		static{
			try {
				System.out.println("Loading SelectMapGUI data");
				String folderPath = "res/images/selectmap_gui/";
				background = ImageIO.read(loader.getResource(folderPath+"selectmap_background.png"));
				tag = ImageIO.read(loader.getResource(folderPath+"selectmap_tag.png"));
				sideArrow = ImageIO.read(loader.getResource(folderPath+"side_arrow.png"));
				rightArrow = new SpriteMap(ImageIO.read(loader.getResource(spriteFolderPath+"selectmap_rightarrow_sprite.png")), 3, 1);
				leftArrow = new SpriteMap(ImageIO.read(loader.getResource(spriteFolderPath+"selectmap_leftarrow_sprite.png")), 3, 1);
//				credits = ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				back = backButton;
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
				tag = null;
				sideArrow = null;
//				credits = null;
				back = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}
	public static class SettingGUI{
		public static BufferedImage background, back;
		public static SpriteMap rightArrow, leftArrow;
		static{
			try {
				System.out.println("Loading SettingGUI data");
				String folderPath = "res/images/setting_gui/";
				background = ImageIO.read(loader.getResource(folderPath+"setting_background.png"));
				rightArrow = new SpriteMap(ImageIO.read(loader.getResource(spriteFolderPath+"setting_rightarrow_sprite.png")), 3, 1);
				leftArrow = new SpriteMap(ImageIO.read(loader.getResource(spriteFolderPath+"setting_leftarrow_sprite.png")), 3, 1);
//				setting = ImageIO.read(loader.getResource(folderPath+"main_setting.png"));
//				credits = ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				back = backButton;
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
				rightArrow = null;
//				setting = null;
//				credits = null;
				back = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}
	public static class CreditsGUI{
		public static BufferedImage background, back;
		static{
			try {
				System.out.println("Loading CreditsGUI data");
				String folderPath = "res/images/setting_gui/";
				background = ImageIO.read(loader.getResource(folderPath+"setting_background.png"));
//				start = ImageIO.read(loader.getResource(folderPath+"main_startgame.png"));
//				setting = ImageIO.read(loader.getResource(folderPath+"main_setting.png"));
//				credits = ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				back = backButton;
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
//				start = null;
//				setting = null;
//				credits = null;
				back = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}
	private static Map<String, AudioClip> beatmapAudioClip = new TreeMap<String, AudioClip>();
	private static Map<String, OsuBeatmap> maps = new TreeMap<String, OsuBeatmap>();

	public static List<Song> songs = new ArrayList<SongIndexer.Song>();

	public static AudioClip loadBeatmapAudioClip(String filePath) {
		System.out.println("loadBeatmapAudioClip " + filePath);
		AudioClip beatmapAC = beatmapAudioClip.get(filePath);
		if (beatmapAC == null) {
			try {
				beatmapAC = Applet.newAudioClip((loader
						.getResource("res/songs/" + filePath)));
				beatmapAudioClip.put(filePath, beatmapAC);
				System.out.println("Loaded File");
			} catch (Exception e) {
				System.out.println("Load Map : " + filePath + "Failed");
				e.printStackTrace();
			}
		} else {
			System.out.println("Using Cache");
		}
		return beatmapAC;
	}

	public static OsuBeatmap loadOsuBeatMap(String filePath) {
		OsuBeatmap map = maps.get(filePath);
		if (map == null) {
			try {
				map = new OsuBeatmap(loader.getResourceAsStream("res/songs/"
						+ filePath));
				maps.put(filePath, map);
			} catch (Exception e) {
				System.out.println("Load Map : " + filePath + "Failed");
				e.printStackTrace();
			}
		}
		return map;
	}

}
