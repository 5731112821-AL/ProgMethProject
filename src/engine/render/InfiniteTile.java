package engine.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Is used to draw a {@link BufferedImage} in repeating pattern,
 * filling the whole GamePanel.
 * @author L2k-nForce
 */
public class InfiniteTile {

	private BufferedImage bufferedImage;

	/**
	 * @param bufferedImage to be drawn
	 */
	public InfiniteTile(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	/**
	 * @param g is the {@link Graphics} object passed from paintComponent's parameter
	 * @param leftXOfScreen is the x coordinate of the top left position of {@link GamePanel}
	 * or image offset-x. 
	 * @param topYOfScreen is the y coordinate of the top left position of {@link GamePanel}
	 * or image offset-y
	 * @param screenWidth
	 * @param screenHheight
	 */
	public void draw(Graphics g, int leftXOfScreen, int topYOfScreen,
			int screenWidth, int screenHheight) {
		Graphics2D g2 = (Graphics2D) g;
		int biWidth = bufferedImage.getWidth(), biHeight = bufferedImage
				.getHeight();
		leftXOfScreen = (leftXOfScreen+biWidth) % biWidth;
		topYOfScreen = (topYOfScreen+biHeight) % biHeight;
		for (int cx = 0; cx * biWidth - leftXOfScreen <= screenWidth; cx++) {
			for (int cy = 0; cy * biHeight - topYOfScreen <= screenHheight; cy++) {
				g2.drawImage(bufferedImage, null, cx * biWidth - leftXOfScreen,
						cy * biHeight - topYOfScreen);
			}
		}
	}

}
