package flyerGame.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import engine.render.RenderLayer.Renderable;
import engine.ui.Button;
import engine.ui.DefaultedMouseListener;
import engine.ui.DynamicUiLabel;
import engine.ui.DynamicUiLabel.Align;
import engine.ui.DynamicUiLabel.GetString;
import engine.ui.VisibleObject;
import flyerGame.engineExtension.GameLogic;
import flyerGame.engineExtension.Resources;
import flyerGame.engineExtension.SystemLogic.Action;

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
			
			int lastScore = gameLogic.getScore();
			
			@Override
			public String getString() {
				int add = (gameLogic.getScore() - lastScore)/2;
				if(add < 10)
					add = gameLogic.getScore() - lastScore;
				lastScore += add;
				return ""+lastScore;
			}
		}, 1670+offset, 25, Resources.scoreFont, new Color(109, 222, 243), Align.right));
		
		postConstrutorConfig();
	}

}
