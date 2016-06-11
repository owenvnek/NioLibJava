package com.niopullus.NioLib.scene.dynscene;

import java.awt.image.BufferedImage;

/**Image Node that operates as a Trigger
 * Created by Owen on 4/6/2016.
 */
public class ImageTrigger extends ImageNode implements Trigger {

    private int xRad;
    private int yRad;

    public ImageTrigger(final String name, final BufferedImage image, final int xRad, final int yRad) {
        super(name, image);
        this.xRad = xRad;
        this.yRad = yRad;
    }

    public void trigger(final CollideData data) {
        //To be overridden
    }

    public int getCx() {
        return getX() + getWidth() / 2 - xRad;
    }

    public int getCy() {
        return getY() + getHeight() / 2 - yRad;
    }

    public int getCwidth() {
        return getWidth() / 2 + xRad;
    }

    public int getCheight() {
        return getHeight() / 2 + yRad;
    }

    public int getXRad() {
        return xRad;
    }

    public int getYRad() {
        return yRad;
    }

}