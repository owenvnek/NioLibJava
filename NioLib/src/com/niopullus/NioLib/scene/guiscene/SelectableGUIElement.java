package com.niopullus.NioLib.scene.guiscene;

import com.niopullus.NioLib.draw.Draw;
import com.niopullus.NioLib.scene.*;

import java.awt.*;

/**GUIElement that can be informed that they have been selected by the GUIScene
 * Created by Owen on 4/1/2016.
 */
public class SelectableGUIElement extends GUIElement {

    private Background selectedBG;
    private Background selectedBorderBG;
    private Color selectedTextColor;
    private boolean selected;
    private boolean overrideMouse;
    private boolean overrideArrows;
    private boolean overrideKeys;
    private boolean overrideMouseWheel;

    public SelectableGUIElement(final String content, final Font font, final int x, final int y, final int widthGap, final int heightGap) {
        super(content, font, x, y, widthGap, heightGap);
        this.selectedBG = new ColorBackground(Color.WHITE);
        this.selectedBorderBG = new ColorBackground(Color.CYAN);
        this.selectedTextColor = Color.black;
        this.selected = false;
        this.overrideArrows = false;
    }

    public Color getSelectedColor() {
        return selectedBG.getColor();
    }

    public Color getSelectedBorderColor() {
        return selectedBorderBG.getColor();
    }

    public Color getSelectedTextColor() {
        return selectedTextColor;
    }

    public boolean getSelected() {
        return this.selected;
    }

    public Background getSelectedBG() {
        return selectedBG;
    }

    public Background getSelectedBorderBG() {
        return selectedBorderBG;
    }

    public void setSelectedColor(final Color color) {
        selectedBG.setColor(color);
    }

    public void setSelectedBorderColor(final Color color) {
        selectedBorderBG.setColor(color);
    }

    public void setSelectedTextColor(final Color color) {
        this.selectedTextColor = color;
    }

    public void setTheme(final Theme t) {
        super.setTheme(t);
        selectedBG.setColor(t.getSelectedBgColor());
        selectedBorderBG.setColor(t.getSelectedBorderColor());
        selectedTextColor = t.getSelectedTextColor();
    }

    public void updateBackgrounds() {
        super.updateBackgrounds();
        updateSelectedBG();
        updateSelectedBorder();
    }

    private void updateSelectedBG() {
        selectedBG.setWidth(getWidth() - getBorderWidth() * 2);
        selectedBG.setHeight(getHeight() - getBorderWidth() * 2);
    }

    private void updateSelectedBorder() {
        selectedBorderBG.setWidth(getWidth());
        selectedBorderBG.setHeight(getHeight());
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public void draw() {
        if (selected) {
            int yPos = getHeightGap() + getY();
            for (int i = 0; i < getLineCount(); i++) {
                final String line = getLine(i);
                final int xPos = getXPos(line);
                Draw.mode(getDrawMode()).text(line, selectedTextColor, getFont(), xPos, yPos, getZ(), 0);
                yPos += getLineGap();
            }
            selectedBG.draw(getX() + getBorderWidth(), getY() + getBorderWidth(), getZ(), getDrawMode());
            selectedBorderBG.draw(getX(), getY(), getZ(), getDrawMode());
        } else {
            super.draw();
        }
    }

    public void activate() {
        //To be overridden
    }

    public void upArrow() {
        //To be overridden
    }

    public void downArrow() {
        //To be overridden
    }

    public void keyPress(final Scene.KeyPack pack) {
        //To be overridden
    }

    public void keyRelease(final Scene.KeyPack pack) {
        //To be overridden
    }

    public void moveMouse(final Scene.MousePack pack) {
        //To be overridden
    }

    public void mousePress(final Scene.MousePack pack) {
        //To be overridden
    }

    public void moveMouseWheel(final Scene.MouseWheelPack pack) {
        //To be overridden
    }

    public boolean isOverrideMouse() {
        return overrideMouse;
    }

    public boolean isOverrideArrows() {
        return overrideArrows;
    }

    public boolean isOverrideKeys() {
        return overrideKeys;
    }

    public boolean isOverrideMouseWheel() {
        return overrideMouseWheel;
    }

    public void enableOverrideMouse() {
        overrideKeys = true;
    }

    public void enableOverrideArrows() {
        overrideArrows = true;
    }

    public void enableOverrideKeys() {
        overrideKeys = true;
    }

    public void enableOverrideMouseWheel() {
        overrideMouseWheel = true;
    }

    public void disableOverrideMouse() {
        overrideMouse = false;
    }

    public void disableOverrideArrows() {
        overrideArrows = false;
    }

    public void disableOverrideKeys() {
        overrideKeys = false;
    }

    public void disableOverrideMouseWheel() {
        overrideMouseWheel = false;
    }

}