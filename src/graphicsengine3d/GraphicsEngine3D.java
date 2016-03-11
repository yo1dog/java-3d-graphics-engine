package graphicsengine3d;

import java.util.Vector;

import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;

/**
 * @author Mike
 *
 * Base of the engine. Manages Universe3D's.
 * @see Universe3D
 */

public class GraphicsEngine3D
{
	protected static Vector<Universe3D> universes = new Vector<Universe3D>();
	
	/**
	 * Creates a new Universe3D. This is used as the default universe.
	 * @param drawingPanel Panel used for drawing
	 */
	public GraphicsEngine3D(JPanel drawingPanel)
	{
		universes.add(new Universe3D(drawingPanel));
	}
	
	/**
	 * Adds a new Universe3D to the engine.
	 * @param drawingPanel JPanel used for drawing
	 * @return The new Universe3D object
	 */
	public Universe3D AddUniverse(JPanel drawingPanel)
	{
		Universe3D univ = new Universe3D(drawingPanel);
		universes.add(univ);
		
		return univ;
	}
	
	/**
	 * Removes a Universe3D from the engine.
	 * @param univ Universe to be removed.
	 * @return If the universe was found and removed
	 */
	public boolean DeleteUniverse(Universe3D univ)
	{
		return universes.remove(univ);
	}
	
	/**
	 * Gets the default universe.
	 * @return Default universe
	 */
	public Universe3D GetDefaultUniverse()
	{
		return universes.get(0);
	}
	
	protected void finalize() throws Throwable
	{
		universes.clear();
	}
}
