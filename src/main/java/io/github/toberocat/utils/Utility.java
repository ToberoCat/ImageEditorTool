package io.github.toberocat.utils;

import io.github.toberocat.Launch;
import io.github.toberocat.exceptions.NoImagesAvailable;
import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static io.github.toberocat.utils.DataUtility.readRawLabels;

public class Utility {
    public static int currentLastFile;

    public static ArrayList<LabelSelection> readLabel(String name) {
        LabelSelection[] items = new LabelSelection[0];
        try {
            items = readRawLabels(new File(Launch.LABEl_PATH.getPath() + "/" + name + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(Arrays.asList(items));
    }

    public Rectangle getAbsoluteRectangle(int x, int y, int width, int height) {
            return new Rectangle(
                    Math.round(x / ImageRenderer.instance().zoom()),
                    Math.round(y / ImageRenderer.instance().zoom()),
                    Math.round(width / ImageRenderer.instance().zoom()),
                    Math.round(height / ImageRenderer.instance().zoom()));

    }



    private static File getImageRotation(File[] files, int rot) {
        currentLastFile = (currentLastFile + rot) % files.length;
        if (currentLastFile < 0) currentLastFile += files.length;

        return files[currentLastFile];
    }

    public static File getImage(int rot) {
        File[] files = Launch.ORIGINAL_IMAGES_PATH.listFiles();
        if (files == null) throw new NoImagesAvailable(Launch.ORIGINAL_IMAGES_PATH);

        File orignalFile = getImageRotation(files, rot);
        if (Arrays.stream(Launch.CENSORED_IMAGES_PATH.list()).anyMatch(x -> x.equals(orignalFile.getName()))) {
            return new File(Launch.CENSORED_IMAGES_PATH.getPath() + "/" + orignalFile.getName());
        }

        return orignalFile;
    }

}
