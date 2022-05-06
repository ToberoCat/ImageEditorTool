package io.github.toberocat;

import io.github.toberocat.gui.EditorGui;
import io.github.toberocat.gui.image.ImageRenderer;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.loop.RenderLoop;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Utility;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Launch {
    public static File ORIGINAL_IMAGES_PATH;
    public static File CENSORED_IMAGES_PATH;
    public static File LABEl_PATH;


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

        LABEl_PATH = new File(args[3]);
        if (!LABEl_PATH.exists() || LABEl_PATH.list() == null) {
            System.out.println("Selection folder doesn't exist");
            return;
        }

        CENSORED_IMAGES_PATH = new File(args[2]);
        if (!CENSORED_IMAGES_PATH.exists() || CENSORED_IMAGES_PATH.list() == null) {
            System.out.println("Final image folder doesn't exist");
            return;
        }

        boolean load = args[0].equals("true");
        if (load) loadFrame();
        else createCensorTask();
    }

    private static void createCensorTask() {
        LinkedList<String> censorFiles = new LinkedList<>();
        for (String file : ORIGINAL_IMAGES_PATH.list()) {
            if (!DataUtility.join(LABEl_PATH, file + ".txt").exists()) continue;
            censorFiles.add(file);
        }

        new Censor(censorFiles.toArray(String[]::new));
    }


    private static void loadFrame() throws IOException {
        EditorGui frame = new EditorGui();
        frame.setVisible(true);

        RenderLoop.EXECUTE_ON_LOAD = () -> {
            File loaded = EventListener.IMAGE_BATCH.read().c();
            Image img = EventListener.IMAGE_BATCH.getCurrent().t();
            ImageRenderer.instance().drawImage(img, Utility.readLabel(loaded.getName()), loaded.getName());
        };

    }
}
