/*
 * Kevin Serina (Project Manager)
 * Lou Demartino
 * BCS345 Final Project
 * Professor: Moaath Aljarab
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// This final project is a simple version of a Paint app
// The user should be able to pick a color from the color palette
// and then start drawing on the canvas on mouse click event.

public class SimplePaint extends JApplet 
{
	public static void main(String[] args) 
	{
		JFrame window = new JFrame("Simple Paint");
	    SimplePaintPanel content = new SimplePaintPanel();
	    window.setContentPane(content);
	    window.setSize(600,480);
	    window.setLocation(100,100);
	    window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    window.setVisible(true);
	}
	
	public void init() 
	{
	    setContentPane( new SimplePaintPanel() );
	}
	   
	public static class SimplePaintPanel extends JPanel implements MouseListener, MouseMotionListener 
	{
		private final static int BLACK = 0;
		private final static int RED = 1;    
		private final static int GREEN = 2;   
		private final static int BLUE = 3; 
		private final static int CYAN = 4;   
		private final static int MAGENTA = 5;
		private final static int YELLOW = 6;
	      
	    private int currentColor = BLACK;  // The currently selected drawing color
	                                      
	      
	      
	    /* The following variables are used when the user is sketching a
	    curve while dragging a mouse. */
	      
	    private int prevX, prevY;     // The previous location of the mouse.
	      
	    private boolean dragging;      // This is set to true while the user is drawing.
	      
	    private Graphics graphicsForDrawing;  // A graphics context for the panel
	                                  		  // that is used to draw the user's curve.
	      
	      
	    /*
	     * Constructor for SimplePaintPanel class sets the background color to be
	     * white and sets it to listen for mouse events on itself.
	     */
	    SimplePaintPanel() 
	    {
	    	setBackground(Color.WHITE);
	        addMouseListener(this);
	        addMouseMotionListener(this);
	    }
	      
	            
	     // Draw the contents of the panel.
	     public void paintComponent(Graphics g) {
	    	 super.paintComponent(g);  // Fill with background color (white).
	         int width = getWidth();    // Width of the panel.
	         int height = getHeight();  // Height of the panel.
	         
	         int colorSpacing = (height - 56) / 7;
	         // Distance between the top of one colored rectangle in the palette
	         // and the top of the rectangle below it. 
	               
	         // Draw a 3-pixel border around the applet in gray.  This has to be
	         // done by drawing three rectangles of different sizes. 
	         
	         g.setColor(Color.GRAY);
	         g.drawRect(0, 0, width-1, height-1);
	         g.drawRect(1, 1, width-3, height-3);
	         g.drawRect(2, 2, width-5, height-5);
	         
	         /* Draw a 56-pixel wide gray rectangle along the right edge of the applet.
	          The color palette and Clear button will be drawn on top of this.
	         */ 
	         
	         g.fillRect(width - 56, 0, 56, height);
	         
	         /* Draw the "Clear button" as a 50-by-50 white rectangle in the lower right
	          corner of the applet
	         */
	         
	         g.setColor(Color.WHITE);
	         g.fillRect(width-53,  height-53, 50, 50);
	         g.setColor(Color.BLACK);
	         g.drawRect(width-53, height-53, 49, 49);
	         g.drawString("CLEAR", width-48, height-23); 
	         
	         // Draw the seven color rectangles. 
	         
	         g.setColor(Color.BLACK);
	         g.fillRect(width-53, 3 + 0*colorSpacing, 50, colorSpacing-3);
	         g.setColor(Color.RED);
	         g.fillRect(width-53, 3 + 1*colorSpacing, 50, colorSpacing-3);
	         g.setColor(Color.GREEN);
	         g.fillRect(width-53, 3 + 2*colorSpacing, 50, colorSpacing-3);
	         g.setColor(Color.BLUE);
	         g.fillRect(width-53, 3 + 3*colorSpacing, 50, colorSpacing-3);
	         g.setColor(Color.CYAN);
	         g.fillRect(width-53, 3 + 4*colorSpacing, 50, colorSpacing-3);
	         g.setColor(Color.MAGENTA);
	         g.fillRect(width-53, 3 + 5*colorSpacing, 50, colorSpacing-3);
	         g.setColor(Color.YELLOW);
	         g.fillRect(width-53, 3 + 6*colorSpacing, 50, colorSpacing-3);
	         
	         /* Draw a 2-pixel white border around the color rectangle
	          of the current drawing color. */
	         
	         g.setColor(Color.WHITE);
	         g.drawRect(width-55, 1 + currentColor*colorSpacing, 53, colorSpacing);
	         g.drawRect(width-54, 2 + currentColor*colorSpacing, 51, colorSpacing-2);
	         
	      } 
	      
	      
	      /**
	       * Change the drawing color after the user has clicked the
	       * mouse on the color palette at a point with y-coordinate y.
	       * (Note that I can't just call repaint and redraw the whole
	       * panel, since that would erase the user's drawing!)
	       */
	      private void changeColor(int y) 
	      {
	    	  int width = getWidth();           // Width of applet.
	          int height = getHeight();         // Height of applet.
	          int colorSpacing = (height - 56) / 7;  // Space for one color rectangle.
	          int newColor = y / colorSpacing;       // Which color number was clicked?
	         
	          if (newColor < 0 || newColor > 6)      // Make sure the color number is valid.
	            return;
	         
	         /* Remove the hilite from the current color, by drawing over it in gray.
	          Then change the current drawing color and draw a hilite around the
	          new drawing color.  */
	         
	         Graphics g = getGraphics();
	         g.setColor(Color.GRAY);
	         g.drawRect(width-55, 1 + currentColor*colorSpacing, 53, colorSpacing);
	         g.drawRect(width-54, 2 + currentColor*colorSpacing, 51, colorSpacing-2);
	         currentColor = newColor;
	         g.setColor(Color.WHITE);
	         g.drawRect(width-55, 1 + currentColor*colorSpacing, 53, colorSpacing);
	         g.drawRect(width-54, 2 + currentColor*colorSpacing, 51, colorSpacing-2);
	         g.dispose();
	      }
	      	/*
	      	 * This method is called in mousePressed when the user clicks on the drawing area.
	         * It sets up the graphics context, graphicsForDrawing, to be used to draw the user's 
	         * sketch in the current color.
	        */
	      private void setUpDrawingGraphics() {
	         graphicsForDrawing = getGraphics();
	         switch (currentColor) {
	         case BLACK:
	            graphicsForDrawing.setColor(Color.BLACK);
	            break;
	         case RED:
	            graphicsForDrawing.setColor(Color.RED);
	            break;
	         case GREEN:
	            graphicsForDrawing.setColor(Color.GREEN);
	            break;
	         case BLUE:
	            graphicsForDrawing.setColor(Color.BLUE);
	            break;
	         case CYAN:
	            graphicsForDrawing.setColor(Color.CYAN);
	            break;
	         case MAGENTA:
	            graphicsForDrawing.setColor(Color.MAGENTA);
	            break;
	         case YELLOW:
	            graphicsForDrawing.setColor(Color.YELLOW);
	            break;
	         }
	      } 
	      
	      
	      
	      // This method is called when the user presses the mouse anywhere in the applet.  
	       
	      public void mousePressed(MouseEvent evt) {
	         
	         int x = evt.getX();   // x-coordinate where the user clicked.
	         int y = evt.getY();   // y-coordinate where the user clicked.
	         
	         int width = getWidth();    // Width of the panel.
	         int height = getHeight();  // Height of the panel.
	         
	         if (dragging == true)  // Ignore mouse presses that occur
	            return;             // when user is already drawing a curve.
	                                
	         
	         if (x > width - 53) {
	        	 					// User clicked to the right of the drawing area.
	               					// This click is either on the clear button or
	        	 					// on the color palette.
	            if (y > height - 53)
	               repaint();       //  Clicked on "CLEAR button".
	            else
	               changeColor(y);  // Clicked on the color palette.
	         }
	         else if (x > 3 && x < width - 56 && y > 3 && y < height - 3) {
	               // The user has clicked on the white drawing area.
	               // Start drawing a curve from the point (x,y).
	            prevX = x;
	            prevY = y;
	            dragging = true;
	            setUpDrawingGraphics();
	         }
	         
	      }
	      
	      // This method is called whenever the user releases the mouse button.
	      public void mouseReleased(MouseEvent evt) {
	         if (dragging == false)
	            return; 			// Nothing to do because the user isn't drawing.
	         dragging = false;
	         graphicsForDrawing.dispose();
	         graphicsForDrawing = null;
	      }
	      
	      /**
	       * Called whenever the user moves the mouse while a mouse button is held down.  
	       * If the user is drawing, draw a line segment from the previous mouse location 
	       * to the current mouse location, and set up prevX and prevY for the next call.  
	       */
	      public void mouseDragged(MouseEvent evt) {
	         
	         if (dragging == false)
	            return;  // Nothing to do because the user isn't drawing.
	         
	         int x = evt.getX();   // x-coordinate of mouse.
	         int y = evt.getY();   // y-coordinate of mouse.
	         
	         if (x < 3)                          // Adjust the value of x,
	            x = 3;                           // to make sure it's in
	         if (x > getWidth() - 57)       	 // the drawing area.
	            x = getWidth() - 57;
	         
	         if (y < 3)                          // Adjust the value of y,
	            y = 3;                           // to make sure it's in
	         if (y > getHeight() - 4)      		 // the drawing area.
	            y = getHeight() - 4;
	         
	         graphicsForDrawing.drawLine(prevX, prevY, x, y);  // Draw the line.
	         
	         prevX = x;  // Get ready for the next line segment in the curve.
	         prevY = y;
	      } 
	      
	      public void mouseEntered(MouseEvent evt) { }   
	      public void mouseExited(MouseEvent evt) { }    //    (Required by the MouseListener
	      public void mouseClicked(MouseEvent evt) { }   //    and MouseMotionListener
	      public void mouseMoved(MouseEvent evt) { }     //    interfaces).
	      
	} 
}