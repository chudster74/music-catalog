package com.github.lucapino.catalog.view;

import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

/**
 * this class was created by two ibm authors.
 *
 * @see http://www.ibm.com/developerworks/web/library/us-j2d/
 */
public class RolloverIcon implements Icon {

    protected Icon icon;

    public RolloverIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D graphics2d = (Graphics2D) g;
        Composite oldComposite = graphics2d.getComposite();
        graphics2d.setComposite(RolloverComposite.DEFAULT);
        icon.paintIcon(c, g, x, y);
        graphics2d.setComposite(oldComposite);
    }
}
