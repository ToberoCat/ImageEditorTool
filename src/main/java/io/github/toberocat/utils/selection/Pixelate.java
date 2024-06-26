package io.github.toberocat.utils.selection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.github.toberocat.utils.Mathi.clamp;

public class Pixelate {
    public static BufferedImage pixelate(File file, List<LabelSelection> labels) throws IOException {
        BufferedImage img = ImageIO.read(file);

        float width = img.getWidth(), height = img.getHeight();

        for (LabelSelection label : labels) {
            int pixelate = (int) Math.round(
                    (height / width) * (width / height) * (width + height) * 2 * (Math.min(label.width() + label.height(), 350) / 750f) /100f + (Math.random() * 10));
            pixelate(img, pixelate, label.x(), label.y(), label.width(), label.height());
        }

        return img;
    }

    public static void pixelate(BufferedImage img, int pixelSize, int startX, int startY, int width, int height) {
        Raster src = img.getData();
        WritableRaster dest = src.createCompatibleWritableRaster();
        dest.setRect(src);

        startY = clamp(startY, 0, src.getHeight());
        startX = clamp(startX, 0, src.getWidth());

        for (int y = startY; y < clamp(startY + height, 0, src.getHeight()); y += pixelSize) {
            for (int x = startX; x < clamp(startX + width, 0, src.getWidth()); x += pixelSize) {
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
