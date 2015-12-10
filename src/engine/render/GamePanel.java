package engine.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import engine.game.Logic.Updatable;

/**
 * GamePanel is used for all rendering in the Engine. This includes UI and game elements.
 * This is meant to be added directly to the {@link JFrame} of the application.
 * @author BobbyL2k
 */
@SuppressWarnings("serial")
public class GamePanel extends JComponent implements Updatable{
	private Dimension dimension;
	private ArrayList<RenderLayer> renderLayers;
	
	/**
	 * renderLayers - is initialized to an empty list
	 * @param dimension - Dimension of the GamePanel
	 */
	public GamePanel(Dimension dimension) {
		this(dimension, new ArrayList<RenderLayer>());
	}
	/**
	 * @param dimension - Dimension of the GamePanel
	 * @param renderLayers - Pass in null if you want to set it later with setRenderLayers
	 */
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

	/**
	 * @param renderLayers - replaces the old renderLayers(if set)
	 */
	public void setRenderLayers(ArrayList<RenderLayer> renderLayers) {
		this.renderLayers = renderLayers;
	}

	@Override
	public void update(long frameTime) {
		this.repaint();
	}

}
