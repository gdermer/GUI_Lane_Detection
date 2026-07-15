package com.mycompany.gsgui;

import com.mycompany.gsgui.CSVActions.LabelLane1;
import com.mycompany.gsgui.CSVActions.LabelLane2;
import com.mycompany.gsgui.CSVActions.LabelLane3;
import com.mycompany.gsgui.CSVActions.LabelLane4;
import com.mycompany.gsgui.CSVActions.MinusOneLane;
import com.mycompany.gsgui.CSVActions.OpenCSVPic;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ImagePanel extends JPanel {

    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;

    /* the background specified design */
    //private ImageIcon backgroundLogo = new ImageIcon(getClass().getClassLoader().getResource("logo-groupD11.png"));

    /* the image to be modified when selecting */
    private BufferedImage previewImg;

    /* a mouse tool to be applied on the picture */
    private MouseTool tool;

    private int button;
    
    public JButton laneLabel;

    /**
     * <p>
     * The zoom-level of the current view. A scale of 1.0 represents actual
     * size; 0.5 is zoomed out to half size; 1.5 is zoomed in to one-and-a-half
     * size; and so forth.
     * </p>
     *
     * <p>
     * Note that the scale is internally represented as a multiplier, but
     * externally as a percentage.
     * </p>
     */
    private double scale;
  
    /**
     * <p>
     * Create a new ImagePanel.
     * </p>
     *
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * </p>
     */
    public ImagePanel() {
        image = new EditableImage();
        scale = 1.0;
        tool = null;
        this.laneLabel = new JButton("lane");
        this.add(laneLabel);
        InputMap im = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();


        im.put(KeyStroke.getKeyStroke("ESCAPE"), "cancelSelection");
        am.put("cancelSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("selecting reset!");
                if (tool instanceof SelectionTool s) {
                    s.cancelSelection();
                }
            }
        });
        
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                tool.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                tool.mouseReleased(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                tool.mouseClicked(e);
            }

        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                tool.mouseDragged(e);
            }
        });
        
        
       

    }

   
    

    /**
     * <p>
     * Get the currently displayed image
     * </p>
     *
     * @return the image currently displayed.
     */
    @SuppressWarnings("NonPublicExported")
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     *
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc.
     * </p>
     *
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100 * scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     *
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc. The zoom level is restricted to the
     * range [50, 200].
     * </p>
     *
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        if (zoomPercent < 50) {
            zoomPercent = 50;
        }
        if (zoomPercent > 200) {
            zoomPercent = 200;
        }
        scale = zoomPercent / 100;
    }

    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     *
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a
     * default size if no image is present.
     * </p>
     *
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            return new Dimension((int) Math.round(image.getCurrentImage().getWidth() * scale),
                    (int) Math.round(image.getCurrentImage().getHeight() * scale));
        } else {
            return new Dimension(450, 450);
        }
    }

    /**
     * <p>
     * Set the current mouse tool for the image panel.
     * </p>
     *
     * @param tool the MouseTool to set as the active tool
     */
    public void setTool(MouseTool tool) {
        this.tool = tool;
    }

    /**
     * <p>
     * Get the current mouse tool for the image panel.
     * </p>
     *
     * @return the currently active MouseTool
     */
    public MouseTool getTool() {
        return this.tool;
    }

    /**
     * <p>
     * Set a preview image to display in place of the current image.
     * </p>
     *
     * <p>
     * Used by the SelectionTool to show a brightened preview of the selected
     * region. Pass null to revert to displaying the current image.
     * </p>
     *
     * @param previewImg the preview image to display, or null to clear
     */
    public void setPreview(BufferedImage previewImg) {
        this.previewImg = previewImg;
        repaint();
    }

    /* return the button number pressed*/
    public int returnButtonNumber() {
        return this.button;
    }

    private void handleNumberInput(int number) {

        this.button = number;

    }
//public void addToolButtons(Action... buttonActions) {
//    for (Action a : buttonActions) {
//        JButton button = new JButton(a);
//        button.setPreferredSize(new Dimension(100,50));
//        
//        if(a instanceof LabelLane1 lane1l){
//            
//        
//            button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                laneLabel.setLabel("1");
//            }
//            
//        });
//                      }
//        
//        if(a instanceof LabelLane2 lane2l){
//            
//        
//            button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                laneLabel.setLabel("2");
//            }
//            
//        });
//                      }
//        
//        if(a instanceof LabelLane3 lane3l){
//            
//        
//            button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                laneLabel.setLabel("3");
//            }
//            
//        });
//                      }
//        
//        if(a instanceof LabelLane4 lane4l){
//            
//        
//            button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                laneLabel.setLabel("4");
//            }
//            
//        });
//                      }
//                      
//     
//        this.add(button);
//        
//    }




    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     *
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
//        if (!image.hasImage()) {
//            g.drawImage(backgroundLogo.getImage(), 0, 0, getWidth(), getHeight(), this);
//        }
        if (image.hasImage()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scale, scale);

            //when the mouse is released, the previewImg is reset to null, so the display goes back to the orig image
            if (previewImg == null) {
                g2.drawImage(image.getCurrentImage(), null, 0, 0);
            } else {
                g2.drawImage(previewImg, null, 0, 0);
            }

            if (tool instanceof SelectionTool s) {
                s.drawSelection(g2);
                s.setScale(scale);
            }
            
            

            g2.dispose();
        }

    }
}
