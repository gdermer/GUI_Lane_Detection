package com.mycompany.gsgui;
import java.awt.event.MouseEvent;


/**
 * <p>
 * Interface for mouse interaction tools that can be applied to the ImagePanel.
 * </p>
 *
 * <p>
 * MouseTool defines the contract for handling mouse events on the image canvas.
 * Classes implementing this interface can respond to mouse presses, drags,
 * releases, and clicks. This allows different tools to be swapped in and out
 * of the ImagePanel without changing the event handling code.
 * </p>
 * 
 * @author Nika Torres
 * @version 1.0
 */
public interface MouseTool {
       /**
     * <p>
     * Called when the mouse button is pressed down on the image panel.
     * </p>
     *
     * @param e the mouse event containing the position and button information
     */
public void mousePressed(MouseEvent e);

    /**
     * <p>
     * Called when the mouse is dragged (moved while a button is held down)
     * on the image panel.
     * </p>
     *
     * @param e the mouse event containing the current position information
     */
public void mouseDragged(MouseEvent e);
    /**
     * <p>
     * Called when the mouse button is released on the image panel.
     * </p>
     *
     * @param e the mouse event containing the position and button information
     */
public void mouseReleased(MouseEvent e);

    /**
     * <p>
     * Called when the mouse button is clicked (pressed and released) on the
     * image panel.
     * </p>
     *
     * @param e the mouse event containing the position and button information
     */
public void mouseClicked(MouseEvent e);
}
