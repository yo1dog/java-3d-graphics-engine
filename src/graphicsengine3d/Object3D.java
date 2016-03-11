package graphicsengine3d;

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * @author Mike
 * Manages a J3D Node. Must be added to a Scene3D to be visible.
 * @see Scene3D
 */
public class Object3D
{
	protected Node node;
	protected TransformGroup trans = new TransformGroup();
	private Transform3D trans3d = new Transform3D();
	private Vector3f pos = new Vector3f();
	private Vector3f rot = new Vector3f();
	private float scale = 1.0f;
	
	/**
	 * Creates an object at the default position and rotation.
	 * @param node J3D Node
	 */
	public Object3D(Node node)
	{ Init(node, null, null, null); }
	
	/**
	 * Creates an object with the specified transformation.
	 * @param node		J3D Node
	 * @param newTrans	Transformation
	 */
	public Object3D(Node node, Transform3D newTrans)
	{ Init(node, newTrans, null, null); }
	
	/**
	 * Creates an object at the specified position and default rotation.
	 * @param node		J3D Node
	 * @param posVector	Position
	 */
	public Object3D(Node node, Vector3f posVector)
	{ Init(node, null, posVector, null); }
	
	/**
	 * Creates an object at the specified position and rotation
	 * @param node		J3D Node
	 * @param posVector	Position
	 * @param rotVector	Rotation (X, Y, Z)
	 */
	public Object3D(Node node, Vector3f posVector, Vector3f rotVector)
	{ Init(node, null, posVector, rotVector); }
	
	
	private void Init(Node node, Transform3D newTrans, Vector3f posVector, Vector3f rotVector)
	{
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		this.node = node;
		
		if (newTrans != null)
			trans.setTransform(newTrans);
		else
		{
			if (posVector != null)
				SetPosition(posVector);
			
			if (rotVector != null)
				AddRotation(rotVector);
			
			trans.setTransform(trans3d);
		}
	}
	
	/**
	 * Gets the object's transform group.
	 * @return Object's TransformGroup
	 */
	public TransformGroup GetTransformGroup()
	{
		return trans;
	}
	
	/**
	 * Sets the object TransformGroup's Transform3D.
	 * @param transform Transform3D
	 */
	public void SetTransform3D(Transform3D transform)
	{
		trans.setTransform(transform);
	}
	
//*********************************************************************************
//Rotation
//
//*********************************************************************************
	
	/**
	 * Adds to the object's rotation.
	 * @param rotVector Rotation
	 */
	public void AddRotation(Vector3f rotVector)
	{
		// create transformations and apply rotations
		Transform3D trans1 = new Transform3D();
		Transform3D trans2 = new Transform3D();
		Transform3D trans3 = new Transform3D();
		trans1.rotX(rotVector.x);
		trans2.rotY(rotVector.y);
		trans3.rotZ(rotVector.z);

		// multiply transformations
		trans3d.mul(trans1);
		trans3d.mul(trans2);
		trans3d.mul(trans3);
		
		trans.setTransform(trans3d);
		rot.x += rotVector.x;
		rot.y += rotVector.y;
		rot.z += rotVector.z;
	}
	
	/**
	 * Adds to the object's pitch.
	 * @param angle Angle
	 */
	public void AddPitch(float angle)
	{
		Transform3D trans1 = new Transform3D();
		trans1.rotY(angle);
		
		trans3d.mul(trans1);
		trans.setTransform(trans3d);
		rot.y += angle;
	}
	
	/**
	 * Adds to the object's yaw.
	 * @param angle Angle
	 */
	public void AddYaw(float angle)
	{
		Transform3D trans1 = new Transform3D();
		trans1.rotZ(angle);
		
		trans3d.mul(trans1);
		trans.setTransform(trans3d);
		rot.z += angle;
	}
	
	/**
	 * Adds to the object's roll.
	 * @param angle Angle
	 */
	public void AddRoll(float angle)
	{
		Transform3D trans1 = new Transform3D();
		trans1.rotX(angle);
		
		trans3d.mul(trans1);
		trans.setTransform(trans3d);
		rot.x += angle;
	}
	
	/**
	 * Sets the object's rotation.
	 * @param angle Angle
	 */
	public void SetRotation(Vector3f rotVector)
	{
		trans3d = new Transform3D();
		rot = new Vector3f();
		SetPosition(pos);
		AddRotation(rotVector);
	}
	
	/**
	 * Sets the object's pitch.
	 * @param angle Angle
	 */
	public void SetPitch(float angle)
	{
		trans3d = new Transform3D();
		rot = new Vector3f();
		SetPosition(pos);
		AddRotation(new Vector3f(rot.x, angle, rot.z));
	}
	
	/**
	 * Sets the object's yaw.
	 * @param angle Angle
	 */
	public void SetYaw(float angle)
	{
		trans3d = new Transform3D();
		rot = new Vector3f();
		SetPosition(pos);
		AddRotation(new Vector3f(rot.x, rot.y, angle));
	}
	
	/**
	 * Sets the object's roll.
	 * @param angle Angle
	 */
	public void SetRoll(float angle)
	{
		trans3d = new Transform3D();
		rot = new Vector3f();
		SetPosition(pos);
		AddRotation(new Vector3f(angle, rot.y, rot.z));
	}
	
	/**
	 * Gets the objet's rotation.
	 * @return Rotation
	 */
	public Vector3f GetRot()
	{ return rot; }
	
	/**
	 * Gets the objet's pitch.
	 * @return Pitch
	 */
	public float GetPitch()
	{ return rot.y; }
	
	/**
	 * Gets the objet's yaw.
	 * @return Yaw
	 */
	public float GetYaw()
	{ return rot.z; }
	
	/**
	 * Gets the objet's roll.
	 * @return Roll
	 */
	public float GetRoll()
	{ return rot.x; }
	
	

	//*********************************************************************************
	//Position
	//
	//*********************************************************************************
	
	/**
	 * Adds to the object's position.
	 * @param posVector Position
	 */
	public void AddPosition(Vector3f posVector)
	{
		pos.x += posVector.x;
		pos.y += posVector.y;
		pos.z += posVector.z;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Adds to the object's X position.
	 * @param amt Amount to add
	 */
	public void AddX(float amt)
	{
		pos.x += amt;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Adds to the object's Y position.
	 * @param amt Amount to add
	 */
	public void AddY(float amt)
	{
		pos.y += amt;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Adds to the object's Z position.
	 * @param amt Amount to add
	 */
	public void AddZ(float amt)
	{
		pos.z += amt;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Sets the object's position.
	 * @param posVector Position
	 */
	public void SetPosition(Vector3f posVector)
	{
		pos.x = posVector.x;
		pos.y = posVector.y;
		pos.z = posVector.z;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Sets the object's X position.
	 * @param x X Position
	 */
	public void SetX(float x)
	{
		pos.x = x;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Sets the object's Y position.
	 * @param y Y Position
	 */
	public void SetY(float y)
	{
		pos.y = y;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Sets the object's Z position.
	 * @param z Z Position
	 */
	public void SetZ(float z)
	{
		pos.z = z;
		
		trans3d.setTranslation(pos);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Gets the object's position.
	 * @return Position
	 */
	public Vector3f GetPosition()
	{ return pos; }
	
	/**
	 * Gets the object's X position.
	 * @return X Position
	 */
	public float GetX()
	{ return pos.x; }
	
	/**
	 * Gets the object's Y position.
	 * @return Y Position
	 */
	public float GetY()
	{ return pos.y; }
	
	/**
	 * Gets the object's Z position.
	 * @return Z Position
	 */
	public float GetZ()
	{ return pos.z; }
	
	
	
	//*********************************************************************************
	//Scale
	//
	//*********************************************************************************
	
	/**
	 * Adds to the object's scale.
	 * @param amt Amount to add
	 */
	public void AddScale(float amt)
	{
		scale += amt;
		
		trans3d.setScale(scale);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Sets the object's scale.
	 * @param scale Scale
	 */
	public void SetScale(float scale)
	{
		this.scale = scale;
		
		trans3d.setScale(this.scale);
		trans.setTransform(trans3d);
	}
	
	/**
	 * Gets the object's scale.
	 * @return Scale
	 */
	public float GetScale()
	{ return scale; }
	
	
	protected void finalize() throws Throwable
	{
		trans.removeAllChildren();
	}
}
