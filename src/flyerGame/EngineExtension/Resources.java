package flyerGame.EngineExtension;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import osuUtilities.OsuBeatmap;
import engine.render.InfiniteTile;
import engine.utilities.Range;
import flyerGame.Main.SongIndexer;
import flyerGame.Main.SongIndexer.Song;

public class Resources {
	private Resources() {
	}

	public static final int
		virtualScreenWidth = 1440,
		virtualScreenHeight = 1080;
	public static final int
		screenWidth = 800,
		screenHeight = 600;
	
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

	public static BufferedImage ImgButton;
	public static BufferedImage StartButton;

	public static AudioClip soundFxStart;
	public static AudioClip soundFxDrum;

	public static InfiniteTile gameBackground;

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

	static {
		// Load images and SoundFx
		try {
			ImgButton = ImageIO.read(loader.getResource("res/button.png"));
			StartButton = ImageIO.read(loader
					.getResource("res/main_startgame.png"));
			gameBackground = new InfiniteTile(ImageIO.read(loader
					.getResource("res/game_background.png")));

			soundFxStart = Applet.newAudioClip((loader
					.getResource("res/soundFX/gos.wav")));
			soundFxDrum = Applet.newAudioClip((loader
					.getResource("res/soundFX/drum-hitnormal.wav")));

			System.out.println("Resouces Sucessfully loaded YAY !!!");
		} catch (Exception e) {
			ImgButton = null;
			StartButton = null;
			System.out.println("Resouces Failed to load !!!");
		}
		// Beatmap Indexing
		try {
			InputStream inputStream = loader
					.getResourceAsStream("res/songs/songIndex.dat");
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			songs = (List<Song>) ois.readObject();
			System.out.println("Beatmaps indexed !!!");
		} catch (Exception e) {
			System.out.println("Failed to index Beatmaps !!!");
			e.printStackTrace();
		}
	}

}
