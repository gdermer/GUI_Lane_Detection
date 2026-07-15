package com.mycompany.gsgui;


import javax.swing.*;


public abstract class ImageAction extends AbstractAction {

    /**
     * The user interface element containing the image upon which actions should
     * be performed. This is common to all ImageActions.
     */
    public static ImagePanel target;

    /**
     * <p>
     * Constructor for ImageActions.
     * </p>
     *
     * <p>
     * To construct an ImageAction you provide the information needed to
     * integrate it with the interface. Note that the target is not specified
     * per-action, but via the static member {@link target} via
     * {@link setTarget}.
     * </p>
     *
     * @param name The name of the action (ignored if null).
     * @param icon An icon to use to represent the action (ignored if null).
     * @param desc A brief description of the action (ignored if null).
     * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
     */
    ImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
        super(name, icon);
        if (desc != null) {
            putValue(SHORT_DESCRIPTION, desc);
        }
        if (mnemonic != null) {
            putValue(MNEMONIC_KEY, mnemonic);
        }
    }

    /**
     * <p>
     * Set the target for ImageActions.
     * </p>
     *
     * @param newTarget The ImagePanel to apply ImageActions to.
     */
    public static void setTarget(ImagePanel newTarget) {
        target = newTarget;
    }

    /**
     * <p>
     * Get the target for ImageActions.
     * </p>
     *
     * @return The ImagePanel to which ImageActions are currently being applied.
     */
    public static ImagePanel getTarget() {
        return target;
    }

}
