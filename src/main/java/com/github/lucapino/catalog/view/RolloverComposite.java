package com.github.lucapino.catalog.view;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class RolloverComposite implements Composite {

    public static RolloverComposite DEFAULT = new RolloverComposite();

    private RolloverComposite() {
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {

        return new CompositeContext() {
            @Override
            public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {

                for (int x = 0; x < dstOut.getWidth(); x++) {
                    for (int y = 0; y < dstOut.getHeight(); y++) {
                        // Get the source pixels
                        int[] srcPixels = new int[4];
                        src.getPixel(x, y, srcPixels);
                        // Ignore transparent pixels
                        if (srcPixels[3] != 0) {
                            // Lighten each color by 1/2, and increasing the blue
                            srcPixels[0] = srcPixels[0] / 2;
                            srcPixels[1] = srcPixels[1] / 2;
                            srcPixels[2] = srcPixels[2] / 2 + 68;
                            dstOut.setPixel(x, y, srcPixels);
                        }
                    }
                }
            }

            @Override
            public void dispose() {
            }
        };

    }
}
