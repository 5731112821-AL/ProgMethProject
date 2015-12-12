package flyerGame.EngineExtension;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import engine.utilities.Range;

public class Resources {
	private Resources() {}
	
	public static final Range 
		gameField = new Range(1f, 11f),
		movableGameField = new Range(2, 10),

		screenField = new Range(0, 12),
		screenFieldEx = new Range(-6, 18),
		trueScreenField = new Range(0, 600);
	public static final Dimension
		screenDimension = new Dimension(600, 600);

	public static int normalizeToScreen(float in){
		return (int)(in * trueScreenField.max / screenField.max);
//		return (int)Range.normalize(in, screenField, trueScreenField);
	}
	public static float normalizeToGame(int in){
		return in *screenField.max / trueScreenField.max;
//		return Range.normalize(in, trueScreenField, screenField);
	}
	
	public static Range normalizeToScreen(Range in){
		return new Range(
				Range.normalize(in.min, screenField, trueScreenField),
				Range.normalize(in.max, screenField, trueScreenField) );
	}
	
	public static BufferedImage ImgButton;
	public static BufferedImage StartButton;
	public static AudioClip music;
	
	static{
		try {
			ClassLoader loader = Resources.class.getClassLoader();
			ImgButton = ImageIO.read(loader.getResource("flyerGame/res/button.png"));
			StartButton = ImageIO.read(loader.getResource("flyerGame/res/start_button.png"));
			music = Applet.newAudioClip((loader.getResource("flyerGame/res/Lunatic_Tears_Tatsh_Remix_.wav")).toURI().toURL());
			System.out.println("Resouces Sucessfully loaded YAY !!!");
		} catch (Exception e) {
			ImgButton = null;
			StartButton = null;
			System.out.println("Resouces Failed to load !!!");
		}
	}
	
}
