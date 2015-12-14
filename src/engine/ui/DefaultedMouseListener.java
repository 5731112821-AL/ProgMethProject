package engine.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface DefaultedMouseListener extends MouseListener {
	public default void mouseClicked(MouseEvent e) {};
	public default void mouseEntered(MouseEvent e) {};
	public default void mouseExited(MouseEvent e) {};
	public default void mousePressed(MouseEvent e) {};
	public default void mouseReleased(MouseEvent e) {};
}
