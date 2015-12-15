package flyerGame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import engine.render.RenderLayer.Renderable;
import engine.ui.Align;
import engine.ui.Button;
import engine.ui.DefaultedMouseListener;
import engine.ui.DynamicUiLabel;
import engine.ui.DynamicUiLabel.GetString;
import flyerGame.engineExtension.GameLogic;
import flyerGame.engineExtension.Resources;

public class GameHud extends Gui {

	Button closeButton;
	
	public GameHud(GameLogic gameLogic) {
		int offset = Resources.globalOffset;
		closeButton = new Button(Resources.closeButton, 270+offset, 15, new DefaultedMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				gameLogic.requestClose();
			}
		});
		buttons.add(closeButton);
		
		renderablesToAdd.add(new Renderable() {
			@Override
			public void render(Graphics g) {
				Graphics2D g2d = (Graphics2D)g;
				int width = (int) (Resources.healthbar.getWidth()*gameLogic.getHealth()/100);
				if(width == 0)
					width = 1;
				g2d.drawImage(Resources.healthbar.getSubimage(0, 0, width, Resources.healthbar.getHeight()), null, 350+offset, 15);
			}
		});

		renderablesToAdd.add(new DynamicUiLabel(new GetString() {
			
			int lastValue = gameLogic.getScore();
			
			@Override
			public String getString() {
				int add = (gameLogic.getScore() - lastValue)/2;
				if(add < 10)
					add = gameLogic.getScore() - lastValue;
				lastValue += add;
				return ""+lastValue;
			}
		}, 1670+offset, 80, Resources.scoreFont, new Color(109, 222, 243), Align.right));
		
		renderablesToAdd.add(new ComboUiLabel(new GetString() {
			
			@Override
			public String getString() {
				int lastValue = gameLogic.getCombo();
				if(lastValue != 0)
					return ""+lastValue+"x";
				else
					return "";
			}
		}, 1670+offset, 1000, Resources.scoreFont, new Color(109, 222, 243), Align.right));
		
		postConstrutorConfig();
	}

	private static class ComboUiLabel extends DynamicUiLabel{

		public ComboUiLabel(GetString getString, int x, int y, Font font,
				Color color, Align align) {
			super(getString, x, y, font, color, align);
		}
		
		String lastString = "";
		int counter = 0;
		
		
		@Override
		protected void stringReport(String str) {
			if(lastString.compareTo(str) != 0){
				counter = 3;
			}
			lastString = str;
			super.stringReport(str);
		}

		private static final Font[] fonts = new Font[4];
		
		static{
			fonts[0] = Resources.scoreFont.deriveFont(50f);
			fonts[1] = Resources.scoreFont.deriveFont(55f);
			fonts[2] = Resources.scoreFont.deriveFont(67f);
			fonts[3] = Resources.scoreFont.deriveFont(60f);
		}
		
		@Override
		protected Font getFont() {
			if(counter < 0) counter = 0;
			return fonts[counter--];
		}
		
		
	}

	@Override
	public void updateRenderableStates() {}
	
}
