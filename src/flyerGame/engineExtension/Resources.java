package flyerGame.engineExtension;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
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
	
	public static final boolean debugMode = true;
	
	private Resources() {
	}

	public static int
		ratioHeight, ratioWidth;
	
	public static final int 
			virtualScreenHeight = 1080;
	public static int 
			virtualScreenWidth;

	public static int 
			screenHeight, screenWidth;

	public static int
			globalOffset;
	public static float
			scale;
	public static AffineTransform
			scaledTransform;

	public static final Range 
			gameFieldX = new Range(0, 12),
			gameFieldY = new Range(0, 9);
	public static Range 
			gameFieldExX, gameFieldExY,
			virtualScreenFieldX, virtualScreenFieldY,
			virtualScreenGameFieldX, virtualScreenGameFieldY,
			trueScreenFieldX, trueScreenFieldY,
			trueScreenGameFieldX, trueScreenGameFieldY;
	public static Dimension 
			virtualScreenDimension;
	public static Dimension
			screenDimension;

	private static float 
		selectMapStandardFontSize, selectMapLargeFontSize,
		settingStandardFontSize, scoreFontSize;

	static{// TODO Set here
		ratioWidth = 16; ratioHeight = 9; 
		screenHeight = 768;
		selectMapStandardFontSize = 25f;
		selectMapLargeFontSize = 40f;
		settingStandardFontSize = 40f;
		scoreFontSize = 60f;
		recalculateScreenProperties();
		
		System.out.println("virtualScreenWidth " + virtualScreenWidth);
		System.out.println("screenWidth " + screenWidth);

		System.out.println("globalOffset " + globalOffset);

		System.out.println("screenFieldX " + gameFieldX);
		System.out.println("screenFieldY " + gameFieldY);
		System.out.println("screenFieldExX " + gameFieldExX);
		System.out.println("screenFieldExY " + gameFieldExY);
		System.out.println("virtualScreenFieldX " + virtualScreenFieldX);
		System.out.println("virtualScreenFieldY " + virtualScreenFieldY);
		System.out.println("virtualScreenGameFieldX " + virtualScreenGameFieldX);
		System.out.println("virtualScreenGameFieldY " + virtualScreenGameFieldY);
		System.out.println("trueScreenFieldX " + trueScreenFieldX);
		System.out.println("trueScreenFieldY " + trueScreenFieldY);

		System.out.println("virtualScreenDimension " + virtualScreenDimension);

		System.out.println("screenDimension " + screenDimension);
	}
	
	public static final void recalculateScreenProperties(){
		// ratioHeight, ratioWidth Must be set

		virtualScreenWidth = virtualScreenHeight*ratioWidth/ratioHeight;

		// screenHeight Must be set
		screenWidth = screenHeight*ratioWidth/ratioHeight;

		globalOffset = (virtualScreenWidth-1920)/2;
		
		scale = (float)screenHeight/virtualScreenHeight;
		scaledTransform = new AffineTransform();
		scaledTransform.scale(scale, scale);

		gameFieldExX = new Range(gameFieldX.min - 1, gameFieldX.max + 1);
		gameFieldExY = new Range(gameFieldY.min - 1,gameFieldY.max + 1);
		virtualScreenFieldX = new Range(0, 1920+2*globalOffset);
		virtualScreenFieldY = new Range(0, 1080);

		///TESTING
		virtualScreenGameFieldX = new Range(240+globalOffset, 1440+240+globalOffset);
		virtualScreenGameFieldY = new Range(0, 1080);
		
//		int offset = (int)( ((double)ratioWidth/ratioHeight - (double)4/3)*screenHeight/2 ) ;
//		trueScreenFieldX = new Range(0, offset+screenHeight*4/3);
		trueScreenFieldX = new Range(0, screenWidth);
		trueScreenFieldY = new Range(0, screenHeight);
		trueScreenGameFieldX = Range.map(virtualScreenGameFieldX, virtualScreenFieldX, trueScreenFieldX);
		trueScreenGameFieldY = Range.map(virtualScreenGameFieldY, virtualScreenFieldY, trueScreenFieldY);

		virtualScreenDimension = new Dimension(virtualScreenWidth, virtualScreenHeight);

		screenDimension = new Dimension(screenWidth, screenHeight);
	}
	
	public static enum Axis {
		X, Y
	}

	private static ClassLoader loader = Resources.class.getClassLoader();
	private static String spriteFolderPath = "res/images/sprite/";

	public static AudioClip soundFxStart;
	public static AudioClip soundFxDrum;

	public static InfiniteTile gameBackground;
	public static SpriteMap backButton;

	public static Font selectMapStandardFont, selectMapLargeFont, settingStandardFont, scoreFont;
	public static Color fontColor = new Color(246, 184, 152);

	static {
		// Load images and SoundFx
		try {
			gameBackground = new InfiniteTile(ImageIO.read(loader
					.getResource("res/images/game_background.png")));
			backButton = new SpriteMap(ImageIO.read(loader.getResource(
					spriteFolderPath + "back.png")), 3, 1);

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

		// Load Font
		try {
			Font neon = Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream("res/fonts/NEON CLUB MUSIC.otf"));
			Font neonBold = Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream("res/fonts/NEON CLUB MUSIC_bold.otf"));
			selectMapStandardFont = neonBold.deriveFont(Font.BOLD, selectMapStandardFontSize);
			selectMapLargeFont = neon.deriveFont(selectMapLargeFontSize);
			settingStandardFont = neonBold.deriveFont(settingStandardFontSize);
			scoreFont = neonBold.deriveFont(scoreFontSize);
			System.out.println("Font Sucessfully loaded YAY !!!");
		} catch (Exception e) {
			System.out.println("Font Failed to load !!!");
			e.printStackTrace();
		}
	}
	
	private static SpriteMap loadSpriteMap(String filePath, int columns, int rows) throws IOException{
		return new SpriteMap(ImageIO.read(loader.getResource(filePath)), columns, rows);
	}

	public static SpriteMap gameEnemySpriteMap, closeButton;
	public static BufferedImage playerSprite, raySprite, healthbar;
	public static float gameSpeed = 0.001f;

	static{
		try{
			System.out.print("Loading Game Assets data...");
			String folderPath = "res/images/gamepanel/";
			gameEnemySpriteMap = loadSpriteMap(spriteFolderPath+"enemy_sprite.png", 20, 1);
			closeButton = loadSpriteMap(spriteFolderPath+"x_sprite.png", 3, 1);
			playerSprite = ImageIO.read(loader.getResource(folderPath + "player.png"));
			raySprite = ImageIO.read(loader.getResource(folderPath + "ray.png"));
			healthbar = ImageIO.read(loader.getResource(folderPath + "healthbar.png"));
			System.out.println("Sucess!!!");
		} catch ( IOException e ) {
			gameEnemySpriteMap = null;
			System.out.println("Failed");
			e.printStackTrace();
		}
	}

	public static class MainGUI {
		public static BufferedImage background;
		public static SpriteMap start, setting, credits, exit;
		static {
			try {
				System.out.print("Loading MainGUI data...");
				String folderPath = "res/images/main_gui/";
				background = ImageIO.read(loader.getResource(folderPath
						+ "main_background.png"));
				start = new SpriteMap(ImageIO.read(loader.getResource(
						folderPath + "main_start.png")), 3, 1);
				setting = new SpriteMap(ImageIO.read(loader.getResource(
						folderPath + "main_setting.png")), 3, 1);
				credits = new SpriteMap(ImageIO.read(loader.getResource(
						folderPath + "main_credits.png")), 3, 1);
				exit = new SpriteMap(ImageIO.read(loader.getResource(
						folderPath + "main_exit.png")), 3, 1);
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

	public static class SelectMapGUI {
		public static BufferedImage background, tag;
		public static SpriteMap rightArrow, leftArrow, sideArrow, back;
		static {
			try {
				System.out.print("Loading SelectMapGUI data...");
				String folderPath = "res/images/selectmap_gui/";
				background = ImageIO.read(loader.getResource(folderPath
						+ "selectmap_background.png"));
				tag = ImageIO.read(loader.getResource(folderPath
						+ "selectmap_tag.png"));
				sideArrow = new SpriteMap(ImageIO.read(loader
						.getResource(spriteFolderPath + "nevigator.png")), 3, 1);
				rightArrow = new SpriteMap(ImageIO.read(loader
						.getResource(spriteFolderPath
								+ "selectmap_rightarrow.png")), 4, 1);
				leftArrow = new SpriteMap(ImageIO.read(loader
						.getResource(spriteFolderPath
								+ "selectmap_arrowleft.png")), 4, 1);
				// credits =
				// ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				back = backButton;
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
				tag = null;
				sideArrow = null;
				// credits = null;
				back = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}

	public static class SettingGUI {
		public static BufferedImage background;
		public static SpriteMap rightArrow, leftArrow, back;
		static {
			try {
				System.out.print("Loading SettingGUI data...");
				String folderPath = "res/images/setting_gui/";
				background = ImageIO.read(loader.getResource(folderPath
						+ "setting_background.png"));
				rightArrow = new SpriteMap(ImageIO.read(loader
						.getResource(spriteFolderPath
								+ "setting_arrowright.png")), 4, 1);
				leftArrow = new SpriteMap(
						ImageIO.read(loader.getResource(spriteFolderPath
								+ "setting_arrowleft.png")), 4, 1);
				// setting =
				// ImageIO.read(loader.getResource(folderPath+"main_setting.png"));
				// credits =
				// ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				back = backButton;
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
				rightArrow = null;
				// setting = null;
				// credits = null;
				back = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}

	public static class CreditsGUI {
		public static BufferedImage background;
		public static SpriteMap back;
		static {
			try {
				System.out.print("Loading CreditsGUI data...");
				String folderPath = "res/images/setting_gui/";
				background = ImageIO.read(loader.getResource(folderPath
						+ "setting_background.png"));
				// start =
				// ImageIO.read(loader.getResource(folderPath+"main_startgame.png"));
				// setting =
				// ImageIO.read(loader.getResource(folderPath+"main_setting.png"));
				// credits =
				// ImageIO.read(loader.getResource(folderPath+"main_credits.png"));
				back = backButton;
				System.out.println("Sucess!!!");
			} catch (IOException e) {
				background = null;
				// start = null;
				// setting = null;
				// credits = null;
				back = null;
				System.out.println("Failed");
				e.printStackTrace();
			}
		}
	}

	private static Map<String, AudioClip> beatmapAudioClipCache = new TreeMap<String, AudioClip>();
	private static Map<String, BufferedImage> beatmapImageCache = new TreeMap<String, BufferedImage>();
	private static Map<String, OsuBeatmap> osuBeatmapCache = new TreeMap<String, OsuBeatmap>();

	/**
	 * Class List < Song > <br>
	 * List of all Songs indexed.
	 */
	public static List<Song> songs;

	public static void play(Song song) {
		Resources.loadBeatmapAudioClip(song.folderPath + song.songName).play();
	}

	public static void stop(Song song) {
		Resources.loadBeatmapAudioClip(song.folderPath + song.songName).stop();
	}
	
	public static BufferedImage loadBeatmapImage(Song song) {
		return loadBeatmapImage(song, loadOsuBeatmap(song, 0));
	}

	public static BufferedImage loadBeatmapImage(Song song, OsuBeatmap beatmap) {
		if (song == null || beatmap == null)
			return null;
		return loadBeatmapImage(song.folderPath
				+ beatmap.data.get("Events").get("background"));
	}

	public static synchronized BufferedImage loadBeatmapImage(String filePath) {
		BufferedImage image = beatmapImageCache.get(filePath);
		if (image == null) {
			try {
				System.out.print("Load Image : " + filePath + "...");
				image = ImageIO.read(loader
						.getResource("res/songs/" + filePath));
				beatmapImageCache.put(filePath, image);
				System.out.println("Loaded");
			} catch (Exception e) {
				System.out.println("Failed");
				e.printStackTrace();
			}
		} else {
			// System.out.println("Using Cache");
		}
		return image;
	}

	public static synchronized AudioClip loadBeatmapAudioClip(String filePath) {
		// System.out.println("loadBeatmapAudioClip " + filePath);
		AudioClip beatmapAC = beatmapAudioClipCache.get(filePath);
		if (beatmapAC == null) {
			try {
				beatmapAC = Applet.newAudioClip((loader
						.getResource("res/songs/" + filePath)));
				beatmapAudioClipCache.put(filePath, beatmapAC);
				// System.out.println("Loaded File");
			} catch (Exception e) {
				// System.out.println("Load Map : " + filePath + "Failed");
				e.printStackTrace();
			}
		} else {
			// System.out.println("Using Cache");
		}
		return beatmapAC;
	}

	public static OsuBeatmap loadOsuBeatmap(Song song, int index) {
		if (song == null)
			return null;
		return loadOsuBeatmap(song.folderPath + song.beatmapNames.get(index));
	}

	public static synchronized OsuBeatmap loadOsuBeatmap(String filePath) {
		OsuBeatmap map = osuBeatmapCache.get(filePath);
		if (map == null) {
			try {
				map = new OsuBeatmap(loader.getResourceAsStream("res/songs/"
						+ filePath));
				osuBeatmapCache.put(filePath, map);
			} catch (Exception e) {
				System.out.println("Load Map : " + filePath + "Failed");
				e.printStackTrace();
				return null;
			}
		}
		return map;
	}



	public static Thread resourcesLoader = new Thread(new Runnable() {
		
		@Override
		public void run() {
			for(Song song : songs){
				for(int c=0; c<song.beatmapNames.size(); c++){
					OsuBeatmap beatmap = loadOsuBeatmap(song, c);
					loadBeatmapImage(song, beatmap);
				}
			}
		}
	});
	
	static{
		resourcesLoader.start();
	}
	
}
