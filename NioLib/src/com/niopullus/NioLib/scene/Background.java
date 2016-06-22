package com.niopullus.NioLib.scene;


import com.niopullus.NioLib.draw.*;
import com.niopullus.NioLib.draw.Canvas;

import java.awt.*;

/**Used to display a rectangular visual in multiple contexts
 * Created by Owen on 3/19/2016.
 */
public class Background implements Parcel {

    private int width;
    private int height;

    public Background() {
        this(0, 0);
    }

    public Background(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return null; //To be overridden
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void setColor(final Color color) {
        //To be overridden
    }

    public void parcelDraw(final Canvas canvas) {
        //To be overridden
    }

}
