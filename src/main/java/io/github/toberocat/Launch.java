package io.github.toberocat;

import io.github.toberocat.async.AsyncTask;
import io.github.toberocat.gui.EditorGui;
import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.loop.RenderLoop;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Utility;
import io.github.toberocat.utils.selection.Pixelate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static io.github.toberocat.utils.DataUtility.join;

public class Launch {
    public static File ORIGINAL_IMAGES_PATH;
    public static File CENSORED_IMAGES_PATH;
    public static File LABEl_PATH;

    public static final File TEMP = new File(System.getProperty("java.io.tmpdir") + "/ImageEditor/");



    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("You need to give a path to a image");
            return;
        }

        ORIGINAL_IMAGES_PATH = new File(args[1]);
        if (!ORIGINAL_IMAGES_PATH.exists() || ORIGINAL_IMAGES_PATH.list() == null) {
            System.out.println("Root folder doesn't ex+ist");
            return;
        }

        CENSORED_IMAGES_PATH = new File(args[2]);
        if (!CENSORED_IMAGES_PATH.exists() || CENSORED_IMAGES_PATH.list() == null) {
            System.out.println("Final image folder doesn't exist");
            return;
        }

        LABEl_PATH = new File(args[3]);
        if (!LABEl_PATH.exists() || LABEl_PATH.list() == null) {
            System.out.println("Selection folder doesn't exist");
            return;
        }


        boolean load = args[0].equals("true");
        if (load) loadFrame();
        else createCensorTask();
    }

    private static void createCensorTask() throws IOException {
        //LinkedList<String> censorFiles = new LinkedList<>();
        for (String file : ORIGINAL_IMAGES_PATH.list()) {
            if (!DataUtility.join(LABEl_PATH, file + ".txt").exists()) continue;
            if (!DataUtility.join(CENSORED_IMAGES_PATH, file).exists()) continue;

            BufferedImage img = Pixelate.pixelate(join(Launch.ORIGINAL_IMAGES_PATH, file), Utility.readLabel(file));
            ImageIO.write(img, "jpg", join(Launch.CENSORED_IMAGES_PATH, file));
        }

        //new Censor(censorFiles.toArray(String[]::new));
    }


    private static void loadFrame() throws IOException {
        EditorGui frame = new EditorGui();
        frame.setVisible(true);

        RenderLoop.EXECUTE_ON_LOAD = () -> {
            AsyncTask.run(() -> {
                File loaded = EventListener.IMAGE_BATCH.read().c();
                Image img = EventListener.IMAGE_BATCH.getCurrent().t();
                ImageRenderer.instance().drawImage(img, Utility.readLabel(loaded.getName()), loaded.getName());
            });
        };

    }
}
