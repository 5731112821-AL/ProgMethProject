package engine.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import engine.game.GameLogic.Updatable;

/**
 * @author L2k-nForce
 * is used for all rendering in the Engine. This includes UI and game elements.
 */
@SuppressWarnings("serial")
public class GamePanel extends JComponent implements Updatable{
	private Dimension dimension;
	private ArrayList<RenderLayer> renderLayers;
	
	public GamePanel(Dimension dimension) {
		this(dimension, null);
	}

	public GamePanel(Dimension dimension, ArrayList<RenderLayer> renderLayers) {
		super();
		this.dimension = dimension;
		this.setRenderLayers(renderLayers);
		this.setPreferredSize(dimension);
	}

	@Override
	protected void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setBackground(Color.BLACK);
		g2d.fillRect(0, 0, dimension.width, dimension.height);
		if(renderLayers != null){
			for (int i = 0; i < renderLayers.size(); i++) {
				renderLayers.get(i).renderAll(g);
			}
		}
		
	}
	
	public ArrayList<RenderLayer> getRenderLayers() {
		return renderLayers;
	}

	public void setRenderLayers(ArrayList<RenderLayer> renderLayers) {
		this.renderLayers = renderLayers;
	}

	@Override
	public void update(long frameTime) {
		this.repaint();
	}

}
