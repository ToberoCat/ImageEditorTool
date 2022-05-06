package io.github.toberocat.gui.listener;

import io.github.toberocat.Launch;
import io.github.toberocat.utils.Pair;
import io.github.toberocat.utils.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class ImageBatch {

    private final Queue<Pair<Image, File>> imageStack;
    private Pair<Image, File> current;

    public ImageBatch() {
        imageStack = new LinkedList<>();
        for (int i = 0; i < 5; i++) loadNew();
    }

    private void loadNew() {
        File file = Utility.getImage(1);
        try {
            imageStack.add(new Pair<>(ImageIO.read(file), file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryC() throws IOException {
        File fLabels = Launch.LABEl_PATH;
        File file = null;
        while (fLabels.exists()) {
            file = Utility.getImage(1);
            fLabels = new File(Launch.LABEl_PATH.getPath() + "/" + file.getName() + ".txt");
        }

        Image image = ImageIO.read(file);
    }

    public Pair<Image, File> readPrevious() {
        File file = Utility.getImage(-6);

        imageStack.clear();
        for (int i = 0; i < 5; i++) loadNew();

        try {
            current = new Pair<>(ImageIO.read(file), file);
            return current;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pair<Image, File> read() {
        loadNew();
        current = imageStack.remove();
        return current;
    }

    public Pair<Image, File> getCurrent() {
        return current;
    }

    public void setCurrent(Pair<Image, File> current) {
        this.current = current;
    }
}
