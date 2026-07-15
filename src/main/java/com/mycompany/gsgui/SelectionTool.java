
package com.mycompany.gsgui;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import javax.swing.SwingUtilities;

/**
   * <p> 
  * A Mouse Tool to show the selected area of an image
  * </p>
  * 
  * <p>
  * The mouse listener detects the mouse clicks and release
  * Mouse motion listener is a similar class that detects the mouse dragging.
    *     -'mouse motion listener' listens for the mouse's motion
  *     -while the 'mouse listener' just listens for the mouse's actions
  * In this project, we will be mostly using the clicks, hold, and release.
  * For more information, here is the link https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html 
  * </p>
  * @author Nika Torres
  */
public class SelectionTool implements MouseTool, java.io.Serializable{
    
   transient  EditableImage image;
   transient  ImagePanel panel;
    double scale;
    
    /* the point where the mouse first clicks when selecting the image*/
    public Point2D.Float ptStart;
    
    /* the point where the mouse is last located when selecting the image*/
    public Point2D.Float ptEnd;
    
    /* the image to be modified when selecting */
    private transient BufferedImage previewImg;
    
    /* if there is an image selecting */
    private boolean selecting;

     /**
     * <p> 
     * Constructs a SelectionTool instance
     * </p>
     * 
     * <p>
     * This initializes the image to be selected and the panel that contains the image
     * </p>
     * 
     * @param image the target image
     * @param panel the panel that contains the image
     */

    public SelectionTool(EditableImage image, ImagePanel panel){
        this.image = image;
        this.panel = panel;
    }
    
     /**
     * <p>
     * A MouseListener method that records the event when the mouse is pressed
     * </p>
     * 
     * <p>
     * This initializes the starting point of the rectangular selection 
     * Attention: This is not always top left. Use Math.min(a,b) and Math.max(a,b)
     * methods to determine the top left point of the rectangle
     * </p>
     * @param e the behaviour of the mouse
     */

    @Override
    public void mousePressed(MouseEvent e){
       if (!image.hasImage()) return; // add this check

       if (SwingUtilities.isLeftMouseButton(e)){
           selecting = true;
           float imgX = (float)(e.getX() / scale);
           float imgY = (float)(e.getY() / scale); 
           ptStart = new Point2D.Float(Math.max(0, Math.min(imgX, image.getCurrentImage().getWidth())), 
                                       Math.max(0, Math.min(imgY, image.getCurrentImage().getHeight())));   
       } 
    }
    
    /**
     * <p>
     * A MouseListener method that records the event when the mouse is released
     * </p>
     * 
     * <p>
     * This method records if there is an area in the image being selected.
     * </p>
     * 
     * @param e the behaviour of the mouse
     */

    @Override
    public void mouseReleased(MouseEvent e){
        selecting = true; 
       
    }
    
    /**
     * <p>
     * A MouseMotionListener method that records the event when the mouse is dragged
     * </p>
     * 
     * <p>
     * This updates the preview image and the ending point every time the mouse is dragged.
     * Attention: The ptEnd isn't always bottom right. Use Math.min(a,b) and Math.max(a,b)
     * methods to determine the bottom right point of the rectangular selection area
     * </p>
     * @param e the behaviour of the mouse
     */
    @Override
    public void mouseDragged(MouseEvent e){ 
        if (!selecting || !image.hasImage()) return;
        
        previewImg = deepCopy(image.getCurrentImage());

        //this follows the coordinates of what the mouse is currently dragging
        float imgX = (float)(e.getX() / scale);
        float imgY = (float)(e.getY() / scale); 
        ptEnd = new Point2D.Float(Math.max(0, Math.min(imgX, image.getCurrentImage().getWidth())), 
                                  Math.max(0, Math.min(imgY, image.getCurrentImage().getHeight())));  
        if (ptStart == null) return;

        int imgW = previewImg.getWidth();
        int imgH = previewImg.getHeight();

        //the dimensions of the selected area
        int x = Math.max(0, Math.min((int) Math.min(ptStart.x, ptEnd.x), imgW-1));
        int y = Math.max(0, Math.min((int) Math.min(ptStart.y, ptEnd.y), imgH-1));
        int width = Math.min((int) Math.abs(ptStart.x - ptEnd.x), imgW-x);
        int height = Math.min((int) Math.abs(ptStart.y - ptEnd.y),  imgH-y);
        if (width <= 0 || height <= 0) return;

        //this brightens a selected part of the image, using the dimensions above
        RescaleOp bright = new RescaleOp(1.0f, 50f, null);
        BufferedImage brightenImg = previewImg.getSubimage(x, y, width, height);
        bright.filter(brightenImg, brightenImg);

        //updates the image everytime it is dragged
        panel.setPreview(previewImg);
        panel.repaint();

    }
    
    /**
     * <p>
     * A MouseListener method that records the event when the mouse is clicked
     * </p>
     * 
     * <p>
     * Aside from pressing escape to cancel the selection, the user can also click
     * anywhere to reset the preview
     * </p>
     * @param e the behaviour of the mouse
     */
    @Override
    public void mouseClicked(MouseEvent e){
       cancelSelection();
       mousePressed(e);

    }
    
    /**
     * <p>
     * This returns whether there is a current selection area
     * </p>
     * 
     * @return the boolean state of selecting
     */
    public boolean isSelected(){
        return selecting;
    }
    
    /**
     * <p>
     * This sets the scale of the image in ImagePanel
     * </p>
     * 
     * @param scale the scale of the image
     */

    public void setScale(double scale){
        this.scale = scale;
    }
    
    /**
     * <p>
     * This creates a black rectangle for the selection area.
     * If needed to be optimized for bigger images,
     * then the brightened preview image can be removed, and this will remain
     * because every time the mouse is dragged, a deep copy is made for the preview.
     * @param Graphics g2
     */

    public void drawSelection(Graphics2D g2) {
        if (ptStart == null || ptEnd == null) return;

        int x = (int)(Math.min(ptStart.x, ptEnd.x));
        int y = (int)(Math.min(ptStart.y, ptEnd.y));
        int w = (int)(Math.abs(ptStart.x - ptEnd.x));
        int h = (int)(Math.abs(ptStart.y - ptEnd.y));

        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, w, h);
    }
    
     /**
     * <p>
     * This resets all necessary variables back to zero. 
     * Also throws away all changes made to preview so that it doesn't modify original
     * </p>
     */

    public void cancelSelection(){
        previewImg = null; //throws away everything that was changed
        ptStart = null;
        ptEnd = null;
        selecting = false;
        panel.setPreview(previewImg);
        panel.repaint();
    }
  /**
     * <p>
     * Make a 'deep' copy of a BufferedImage.
     * </p>
     *
     * <p>
     * Object instances in Java are accessed via references, which means that
     * assignment does not copy an object, it merely makes another reference to
     * the original. In order to make an independent copy, the {@code clone()}
     * method is generally used. {@link BufferedImage} does not implement
     * {@link Cloneable} interface, and so the {@code clone()} method is not
     * accessible.
     * </p>
     *
     * <p>
     * This method makes a cloned copy of a BufferedImage. This requires
     * knowledge of some details about the internals of the BufferedImage, but
     * essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     *
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     *
     * </p>
     * Updated by MG 2025 to fix an error that occurs when copying a sub-image.
     * </p>
     *
     * <p>
     * This method (only) is released under
     * <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA
     * 2.5</a>
     * </p>
     *
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    
}
