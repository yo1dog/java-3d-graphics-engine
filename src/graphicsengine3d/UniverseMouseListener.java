package graphicsengine3d;

import java.awt.event.MouseEvent;

public abstract class UniverseMouseListener
{
	public abstract void MouseClicked(MouseEvent e);
	public abstract void MouseDragged(MouseEvent e);
	public abstract void MouseMoved(MouseEvent e);
	public abstract void MousePressed(MouseEvent e);
	public abstract void MouseReleased(MouseEvent e);
}
