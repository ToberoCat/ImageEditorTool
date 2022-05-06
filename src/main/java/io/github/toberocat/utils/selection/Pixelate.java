package io.github.toberocat.utils.selection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.toberocat.utils.Mathf.clamp;

public class Pixelate {
    public static BufferedImage pixelate(File file, List<LabelSelection> labels) throws IOException {
        BufferedImage img = ImageIO.read(file);

        float width = img.getWidth(), height = img.getHeight();

        for (LabelSelection label : labels) {
            int pixelate = Math.round((height / width) * (width / height) * (width + height) / 100f);
            pixelate(img, pixelate, label.x(), label.y(), label.width(), label.height());
        }

        return img;
    }

    public static void pixelate(BufferedImage img, int pixelSize, int startX, int startY, int width, int height) {
        Raster src = img.getData();
        WritableRaster dest = src.createCompatibleWritableRaster();
        dest.setRect(src);

        startY = clamp(startY, src.getHeight());
        startX = clamp(startX, src.getWidth());

        for (int y = startY; y < clamp(startY + height, src.getHeight()); y += pixelSize) {
            for (int x = startX; x < clamp(startX + width, src.getWidth()); x += pixelSize) {
                double[] pixel = new double[3];
                pixel = src.getPixel(x, y, pixel);

                for (int yd = y; (yd < y + pixelSize) && (yd < dest.getHeight()); yd++) {
                    for (int xd = x; (xd < x + pixelSize) && (xd < dest.getWidth()); xd++) {
                        dest.setPixel(xd, yd, pixel);
                    }
                }
            }
        }

        img.setData(dest);
    }
}
