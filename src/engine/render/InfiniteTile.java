package engine.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class InfiniteTile {

	private BufferedImage bufferedImage;

	public InfiniteTile(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void draw(Graphics g, int leftXOfScreen, int topYOfScreen,
			int width, int height) {
		Graphics2D g2 = (Graphics2D) g;
		int biWidth = bufferedImage.getWidth(), biHeight = bufferedImage
				.getHeight();
		leftXOfScreen = (leftXOfScreen+biWidth) % biWidth;
		topYOfScreen = (topYOfScreen+biHeight) % biHeight;
		for (int cx = 0; cx * biWidth - leftXOfScreen <= width; cx++) {
			for (int cy = 0; cy * biHeight - topYOfScreen <= height; cy++) {
				g2.drawImage(bufferedImage, null, cx * biWidth - leftXOfScreen,
						cy * biHeight - topYOfScreen);
			}
		}
	}

}
