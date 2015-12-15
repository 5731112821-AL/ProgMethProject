package engine.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import engine.game.Logic.Updatable;
//import flyerGame.engineExtension.Resources;

/**
 * Is used for all rendering in the Engine. This includes UI and game elements.
 * This is meant to be added as sole {@link JComponent} in the {@link JFrame} of the application.
 * @author BobbyL2k
 */
@SuppressWarnings("serial")
public class GamePanel extends JComponent implements Updatable{
	
	private Dimension dimension;
	private ArrayList<RenderLayer> renderLayers;
	private AffineTransform transform;
	
	/**
	 * renderLayers is automatically initialized to an empty list
	 * @param dimension is the dimension of the GamePanel
	 * @param scale is used for scaling all contents on the GamePanel
	 */
	public GamePanel(Dimension dimension, float scale) {
		this(dimension, new ArrayList<RenderLayer>(), scale);
	}
	/**
	 * @param dimension - Dimension of the GamePanel
	 * @param renderLayers - Pass in null if you want to set it later with setRenderLayers
	 */
	public GamePanel(Dimension dimension, ArrayList<RenderLayer> renderLayers, float scale) {
		super();
		this.dimension = dimension;
		this.setRenderLayers(renderLayers);
		this.setPreferredSize(dimension);
		this.transform = new AffineTransform();
		transform.scale(scale, scale);
	}
	
	@Override
	protected void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;

	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g2d.setBackground(Color.BLACK);
		g2d.fillRect(0, 0, dimension.width, dimension.height);
		
		g2d.setTransform(transform);
		
		if(renderLayers != null){
			for (int i = 0; i < renderLayers.size(); i++) {
				renderLayers.get(i).renderAll(g);
			}
		}
	}
	
	/**
	 * @return List of {@link RenderLayer} assigned to be rendered.
	 */
	public List<RenderLayer> getRenderLayers() {
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
