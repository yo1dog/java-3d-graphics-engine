package graphicsengine3d;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.media.j3d.Appearance;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;

public class Test extends JFrame
{
	GraphicsEngine3D GE3D;
	
    public static final int board_rows = 52;
    public static final int board_cols = 72;	
    public static final int cellsize = 10;
    
	private static JPanel drawingPanel;
	
	private static Scene3D scene;
	private static Object3D movingCube;
	
	private double camX = board_cols*cellsize/2, camY = board_rows*cellsize/2;
	private double camRotXY = 0, camRotYZ = 0;
	private double camZoom = 400;
	
	private boolean dragging = false;
	private boolean firstClick = true;
	
	private int keyDown = -1;
	
	private boolean fallowing = false;
	
	public static void main(String[] args)
	{
		new Test();
	}
	
	public Test()
	{
		javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		initiateComponents();
		
		 // initiate graphics engine. NOTE: this must be done BEFORE the JFrame is set to visible
		GE3D = new GraphicsEngine3D(drawingPanel);
		
		setVisible(true);
		
		// set the input listeners
		GE3D.GetDefaultUniverse().SetKeyBoardListener(new KeyBoardListener());
		GE3D.GetDefaultUniverse().SetMouseListener(new MouseListener());
		
		// set the view
		GE3D.GetDefaultUniverse().ViewLookAt(new Point3d(-200, -200, 1000), new Point3d(200, 200, 0));
		
		// create scene
		scene = new Scene3D();
		
		// crate objects and add them to the scene
		movingCube = new Object3D(new ColorCube(10));
		
		scene.AddObject(movingCube);
		scene.AddObject(new Object3D(new ColorCube(10)));
		scene.AddObject(new Object3D(new ColorCube(10), new Vector3f(board_cols*cellsize, 0, 0)));
		scene.AddObject(new Object3D(new ColorCube(10), new Vector3f(board_cols*cellsize, board_rows*cellsize, 0)));
		scene.AddObject(new Object3D(new ColorCube(10), new Vector3f(0, board_rows*cellsize, 0)));
		scene.AddObject(new Object3D(new ColorCube(10), new Vector3f(board_cols*cellsize/2, board_rows*cellsize/2, 0)));
		
		// create a texture, transform it, apply it to an object an add it to the scene
		Texture textImage = new TextureLoader("grass.jpg", this).getTexture();
		Appearance appearance = new Appearance();
		
		Transform3D textTrans = new Transform3D();
		textTrans.setScale(new Vector3d(board_cols/8, board_rows/8, 1));
		
		TextureAttributes textureAttributes = new TextureAttributes();
		textureAttributes.setTextureTransform(textTrans);
		
		appearance.setTexture(textImage);
		appearance.setTextureAttributes(textureAttributes);
		
		scene.AddObject(new Object3D(new Box(board_cols*cellsize/2, board_rows*cellsize/2, 2, Primitive.GENERATE_TEXTURE_COORDS, appearance), new Vector3f(board_cols*cellsize/2, board_rows*cellsize/2, 0)));
		
		// show the scene
		scene.Show();
		
		Vector3f pos = new Vector3f(board_cols*cellsize/2, board_rows*cellsize/2, 7);
		float xv = 0.2f, yv = 0.8f;
		
		while(true)
		{
			// move the cube around
			pos.x += xv;
			pos.y += yv;
			
			movingCube.SetPosition(pos);
			
			if (fallowing)
			{
				camX = pos.x;
				camY = pos.y;
				
				double camRotX = camX + Math.cos(camRotXY)*camZoom*Math.cos(camRotYZ);
				double camRotY = camY + Math.sin(camRotXY)*camZoom*Math.cos(camRotYZ);
				double camRotZ = 100 + Math.sin(camRotYZ)*camZoom;
				
				GE3D.GetDefaultUniverse().ViewLookAt(new Point3d(camRotX, camRotY, camRotZ), new Point3d(camX, camY, 0));
			}
			
			if (pos.x < 0)
				xv = 0.2f;
			if (pos.x > board_cols*cellsize)
				xv = -0.2f;
			if (pos.y < 0)
				yv = 0.8f;
			if (pos.y > board_rows*cellsize)
				yv = -0.8f;
			
			if (dragging)
				mouseDragged();
			
			if (keyDown > -1)
			{
				if (keyDown == KeyEvent.VK_UP)
				{
					camX -= Math.cos(camRotXY) * 8;
					camY -= Math.sin(camRotXY) * 8;
				}
				else if (keyDown == KeyEvent.VK_DOWN)
				{
					camX += Math.cos(camRotXY) * 8;
					camY += Math.sin(camRotXY) * 8;
				}
				else if (keyDown == KeyEvent.VK_LEFT)
				{
					camX -= Math.cos(camRotXY + Math.PI/2) * 8;
					camY -= Math.sin(camRotXY + Math.PI/2) * 8;
				}
				else if (keyDown == KeyEvent.VK_RIGHT)
				{
					camX += Math.cos(camRotXY + Math.PI/2) * 8;
					camY += Math.sin(camRotXY + Math.PI/2) * 8;
				}
				
				double camRotX = camX + Math.cos(camRotXY)*camZoom*Math.cos(camRotYZ);
				double camRotY = camY + Math.sin(camRotXY)*camZoom*Math.cos(camRotYZ);
				double camRotZ = Math.sin(camRotYZ)*camZoom;
				
				GE3D.GetDefaultUniverse().ViewLookAt(new Point3d(camRotX, camRotY, camRotZ), new Point3d(camX, camY, 0));
			}
			
			try
			{
				Thread.currentThread().sleep(8);
			}
			catch(Exception e)
			{
				System.err.println(e); 
			}
		}
	}
	
	
	
	
	private class KeyBoardListener extends UniverseKeyBoardListener
	{
		public void KeyPressed(KeyEvent e)
		{
			keyDown = e.getKeyCode();
		}

		public void KeyReleased(KeyEvent e)
		{
			if (keyDown == e.getKeyCode())
				keyDown = -1;
		}
	}
	
	private class MouseListener extends UniverseMouseListener
	{
		public void MouseClicked(MouseEvent e) {}
		public void MouseDragged(MouseEvent e) {}
		public void MouseMoved(MouseEvent e) {}

		public void MousePressed(MouseEvent e)
		{
			dragging = true;
			firstClick = true;
		}

		public void MouseReleased(MouseEvent e)
		{
			dragging = false;
		}
	}
	
	
	
	public void mouseDragged()
	{
		Point mouse = getMousePosition();
		
		if (mouse == null)
			return;
		
		SwingUtilities.convertPointToScreen(mouse, this);
		
		if (!firstClick)
		{
			double xy, yz;
	
			Point winPoint = new Point();
			SwingUtilities.convertPointToScreen(winPoint, this);
			
			xy = (winPoint.x + this.getWidth()/2 - mouse.x)/400.0d;
			yz = (winPoint.y + this.getHeight()/2 - mouse.y)/400.0d;
			
			camRotXY += xy;
			camRotYZ -= yz;
	
			if (camRotYZ > 1.5)
				camRotYZ = 1.5;
	
			if (camRotYZ < 0.1)
				camRotYZ = 0.1;
		}
		else
			firstClick = false;
		
		double camRotX = camX + Math.cos(camRotXY) * camZoom*Math.cos(camRotYZ);
		double camRotY = camY + Math.sin(camRotXY) * camZoom*Math.cos(camRotYZ);
		double camRotZ = Math.sin(camRotYZ) * camZoom;
		
		Robot rob = null;
		try
		{
			rob = new Robot();
		}
		catch (AWTException e)
		{
		}
		
		Point mousePoint = new Point();
		SwingUtilities.convertPointToScreen(mousePoint, this);
		rob.mouseMove(mousePoint.x + this.getWidth()/2, mousePoint.y + this.getHeight()/2);
		
		GE3D.GetDefaultUniverse().ViewLookAt(new Point3d(camRotX, camRotY, camRotZ), new Point3d(camX, camY, 0));
	}
	
	private void fallowButtonActionPerformed(java.awt.event.ActionEvent evt)
	{
		fallowing = true;
	}
	
	private void stopButtonActionPerformed(java.awt.event.ActionEvent evt)
	{
		fallowing = false;
		camX = board_cols*cellsize/2;
		camY = board_rows*cellsize/2;
		
		double camRotX = camX + Math.cos(camRotXY)*camZoom*Math.cos(camRotYZ);
		double camRotY = camY + Math.sin(camRotXY)*camZoom*Math.cos(camRotYZ);
		double camRotZ = 100 + Math.sin(camRotYZ)*camZoom;
		GE3D.GetDefaultUniverse().ViewLookAt(new Point3d(camRotX, camRotY, camRotZ), new Point3d(camX, camY, 0));
	}
	
	private static void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{
		System.exit(0);
	}
	
	
	/** Auto generated by Form Editor */
	private void initiateComponents()
	{
		java.awt.GridBagConstraints gridBagConstraints;
	    
	    javax.swing.JPanel guiPanel = new javax.swing.JPanel();
	    javax.swing.JButton fallowButton = new javax.swing.JButton();
	    javax.swing.JButton stopButton = new javax.swing.JButton();
        drawingPanel = new javax.swing.JPanel();
        javax.swing.JMenuBar jMenuBar1 = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Graphics Engine 3D");
        guiPanel.setLayout(new java.awt.GridBagLayout());

        fallowButton.setText("Fallow");
        fallowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	fallowButtonActionPerformed(evt);
            }
        });
        
        stopButton.setText("Stop Fallowing");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	stopButtonActionPerformed(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        guiPanel.add(fallowButton, gridBagConstraints);
        guiPanel.add(stopButton, gridBagConstraints);

        getContentPane().add(guiPanel, java.awt.BorderLayout.NORTH);

        drawingPanel.setLayout(new java.awt.BorderLayout());
        drawingPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        
        getContentPane().add(drawingPanel, java.awt.BorderLayout.CENTER);
        
        fileMenu.setText("File");
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        setJMenuBar(jMenuBar1);

        pack();
	}
}
