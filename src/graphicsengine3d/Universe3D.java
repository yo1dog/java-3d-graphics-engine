package graphicsengine3d;

import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.swing.JPanel;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * @author Mike
 *
 * Manages a J3D SimpleUniverse and its drawing panel as well as Scene3D's.
 * @see Scene3D
 */
public class Universe3D
{
	private SimpleUniverse univ;
	private TransformGroup viewTransformGroup;
	private BranchGroup inputBranch = new BranchGroup();
	private UniverseKeyBoardListener keyboardListener;
	private UniverseMouseListener mouseListener;
	
	private boolean dragging = false;
	private boolean firstClick = true;
	
	private int keyDown = -1;
	
	protected Universe3D(JPanel drawingPanel)
	{
		// create canvas and add set config
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		
		// create the universe
		univ = new SimpleUniverse(c);
		univ.getViewingPlatform().setNominalViewingTransform();
		
		viewTransformGroup = univ.getViewingPlatform().getViewPlatformTransform();
		
		univ.getViewer().getView().setMinimumFrameCycleTime(20);
		univ.getViewer().getView().setBackClipDistance(1000000000);
		
		//Input Listeners
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 1000000000);
		KeyBoardListener_KeyPressed keyBoardListener_KeyPressed = new KeyBoardListener_KeyPressed();
		keyBoardListener_KeyPressed.setSchedulingBounds(bounds);
		KeyBoardListener_KeyReleased keyBoardListener_KeyReleased = new KeyBoardListener_KeyReleased();
		keyBoardListener_KeyReleased.setSchedulingBounds(bounds);
		MouseListener_MouseClicked mouseListener_MouseClicked = new MouseListener_MouseClicked();
		mouseListener_MouseClicked.setSchedulingBounds(bounds);
		MouseListener_MouseDragged mouseListener_MouseDragged = new MouseListener_MouseDragged();
		mouseListener_MouseDragged.setSchedulingBounds(bounds);
		MouseListener_MouseMoved mouseListener_MouseMoved = new MouseListener_MouseMoved();
		mouseListener_MouseMoved.setSchedulingBounds(bounds);
		MouseListener_MousePressed mouseListener_MousePressed = new MouseListener_MousePressed();
		mouseListener_MousePressed.setSchedulingBounds(bounds);
		MouseListener_MouseReleased mouseListener_MouseReleased = new MouseListener_MouseReleased();
		mouseListener_MouseReleased.setSchedulingBounds(bounds);
		
		inputBranch.addChild(keyBoardListener_KeyPressed);
		inputBranch.addChild(keyBoardListener_KeyReleased);
		inputBranch.addChild(mouseListener_MouseClicked);
		inputBranch.addChild(mouseListener_MouseDragged);
		inputBranch.addChild(mouseListener_MouseMoved);
		inputBranch.addChild(mouseListener_MousePressed);
		inputBranch.addChild(mouseListener_MouseReleased);
		inputBranch.compile();
		univ.addBranchGraph(inputBranch);
		
		// add the universe to the drawing panel
		drawingPanel.add(c, java.awt.BorderLayout.CENTER);
	}
	
	/**
	 * Gets the TransformGroup for the view.
	 * @return View's TransformGroup
	 */
	public TransformGroup GetViewTransformGroup()
	{
		return viewTransformGroup;
	}
	
	/**
	 * Places the view at a position angled to face another position.
	 * @param from Position of the view
	 * @param to Position to look at
	 */
	public void ViewLookAt(Point3d from, Point3d to)
	{
		Transform3D trans = new Transform3D();
		trans.lookAt(from, to, new Vector3d(0.0d, 0.0d, 1.0d));
		trans.invert();
		
		viewTransformGroup.setTransform(trans);
	}
	
	protected void AddBranchGraph(BranchGroup objBranch)
	{
		univ.addBranchGraph(objBranch);
	}
	
	protected void finalize() throws Throwable
	{
		univ.removeAllLocales();
		univ.cleanup();
	}
	
	/**
	 * Gets the universe's SimpleUniverse.
	 * @return SimpleUniverse
	 */
	public SimpleUniverse getSimpleUniverse()
	{
		return univ;
	}
	
	
	
	/**
	 * Sets the UniverseKeyBoardListener object to handle keyboard events.
	 * @param listener Listener object 
	 */
	public void SetKeyBoardListener(UniverseKeyBoardListener listener)
	{
		keyboardListener = listener;
	}
	
	/**
	 * Sets the UniverseMouseListener object to handle mouse events.
	 * @param listener Listener object 
	 */
	public void SetMouseListener(UniverseMouseListener listener)
	{
		mouseListener = listener;
	}
	

	
	//KeyBoard Listener KeyPressed
	private class KeyBoardListener_KeyPressed extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (keyboardListener != null)
	    		keyboardListener.KeyPressed((KeyEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
	    }
	}
	
	//KeyBoard Listener KeyReleased
	private class KeyBoardListener_KeyReleased extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (keyboardListener != null)
	    		keyboardListener.KeyReleased((KeyEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED));
	    }
	}
	
	
	//Mouse Listener MouseClicked
	private class MouseListener_MouseClicked extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (mouseListener != null)
	    		mouseListener.MouseClicked((MouseEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED));
	    }
	}
	
	//Mouse Listener MouseDragged
	private class MouseListener_MouseDragged extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (mouseListener != null)
	    		mouseListener.MouseDragged((MouseEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED));
	    }
	}
	
	//Mouse Listener MouseMoved
	private class MouseListener_MouseMoved extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (mouseListener != null)
	    		mouseListener.MouseMoved((MouseEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED));
	    }
	}
	
	//Mouse Listener MousePressed
	private class MouseListener_MousePressed extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (mouseListener != null)
	    		mouseListener.MousePressed((MouseEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED));
	    }
	}
	
	//Mouse Listener MouseReleased
	private class MouseListener_MouseReleased extends Behavior
	{
		public void initialize()
		{
			this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED));
		}

		public void processStimulus(Enumeration criteria) 
	    {
	    	if (mouseListener != null)
	    		mouseListener.MouseReleased((MouseEvent)(((WakeupOnAWTEvent)criteria.nextElement()).getAWTEvent()[0]));
	    	
	    	this.wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED));
	    }
	}
}