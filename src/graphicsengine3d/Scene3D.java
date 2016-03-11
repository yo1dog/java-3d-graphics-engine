package graphicsengine3d;

import java.util.Vector;

import javax.media.j3d.BranchGroup;

/**
 * @author Mike
 * Manages a J3D BranchGroup as well as Object3D's.
 * @see Object3D
 */
public class Scene3D
{
	private Universe3D univ;
	
	private boolean shown = false;
	private BranchGroup branch = new BranchGroup();
	private Vector<BranchGroup> objBranches = new Vector<BranchGroup>();
	private Vector<Object3D> objects = new Vector<Object3D>();
	
	/**
	 * Creates a new scene in the default universe.
	 * @see Universe3D
	 */
	public Scene3D()
	{
		univ = GraphicsEngine3D.universes.get(0);
		branch.setCapability(BranchGroup.ALLOW_DETACH);
		branch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		branch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
	}
	/**
	 * Creates a new scene in a specified universe.
	 * @param universe Universe in which to create the scene
	 * @see Universe3D
	 */
	public Scene3D(Universe3D universe)
	{
		univ = universe;
		branch.setCapability(BranchGroup.ALLOW_DETACH);
		branch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		branch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
	}
	
	/**
	 * Adds an object to the scene. Objects cannot be added while the scene is being shown.
	 * @param object3D Object to add
	 * @return If the object was successfully added to the scene
	 * @see Object3D
	 */
	public void AddObject(Object3D object3D)
	{
		BranchGroup newBranch = new BranchGroup();	// create new branch for the object
		newBranch.setCapability(BranchGroup.ALLOW_DETACH);
		
		newBranch.addChild(object3D.trans);			// add the object's transformation group to the new branch
		object3D.trans.addChild(object3D.node);		// add the object's node to the transformation group
		branch.addChild(newBranch);					// add the new branch to the main branch
		
		objBranches.add(newBranch);
		objects.add(object3D);
	}
	
	/**
	 * Removes an object from the scene. Objects cannot be removed while seen is being shown.
	 * @param object3D Object to delete
	 * @return	If the object was found and removed
	 * @see Object3D
	 */
	public boolean DeleteObject(Object3D object3D)
	{
		int index = objects.indexOf(object3D);
		
		if (index == -1)
			return false;
		
		branch.removeChild(objBranches.get(index));	// remove the object's branch from the main branch
		objBranches.get(index).removeAllChildren();	// remove transform group from the object's branch
		object3D.trans.removeAllChildren();			// remove object from transform group
		
		objects.remove(index);
		objBranches.remove(index);
		return true;
	}
	
	/**
	 * Removes all objects in the scene. Cannot remove objects while see in being shown.
	 * @return If all objects where removed
	 */
	public boolean ClearObjects()
	{
		if (shown)				// cannot be deleted while being shown ????
			return false;
		
		for (int i = 0; i < objects.size(); i++)
			objects.get(i).trans.removeAllChildren();
		
		objects.clear();
		branch.removeAllChildren();
		
		return true;
	}
	
	/**
	 * Displays the scene in the universe.
	 */
	public void Show()
	{
		if (!shown)
		{
			shown = true;
			univ.AddBranchGraph(branch);		//add the branch to the universe
		}
	}
	
	/**
	 * Hides the scene in the universe.
	 */
	public void Hide()
	{
		if (shown)
		{
			shown = false;
			branch.detach();
		}
	}
	
	/**
	 * Removes the scene.
	 */
	public void Delete()
	{
		if (shown)
			branch.detach();
		
		branch.removeAllChildren();
		objects.clear();
		shown = false;
	}
	
	/**
	 * Gets if the the scene is being shown.
	 * @return If the scene is being shown
	 */
	public boolean getShown()
	{ return shown; }
	
	/**
	 * Gets the scene's Universe3D.
	 * @return Universe3D
	 */
	public Universe3D getUniverse()
	{ return univ; }
	
	/**
	 * Gets the scene's BranchGroup.
	 * @return BranchGroup
	 */
	public BranchGroup getBranchGroup()
	{ return branch; }
}