package io.github.toberocat.gui.image;

import io.github.toberocat.Launch;
import io.github.toberocat.utils.Pair;
import io.github.toberocat.utils.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

public class ImageBatch {

    public static final int BATCHED = 5;

    private final Queue<Pair<Image, File>> imageStack;
    private Pair<Image, File> current;

    public ImageBatch() throws IOException {
        imageStack = new LinkedList<>();

        Pair<Image, File> pair = null;

        File file = new File("last-edit");
        if (!file.exists()) {
            File fLabels = Launch.LABEl_PATH;
            while (fLabels.exists()) {
                pair = loadNew();
                fLabels = new File(Launch.LABEl_PATH.getPath() + "/" + pair.c().getName() + ".txt");
            }
        } else {
            String result = Files.readString(file.toPath());
            Utility.currentLastFile = Integer.parseInt(result);
            pair = loadNew();
        }

        imageStack.add(pair);

        for (int i = 0; i < BATCHED - 1; i++) imageStack.add(loadNew());
    }

    private Pair<Image, File> loadNew() {
        File file = Utility.getImage(1);
        try {
            return new Pair<>(ImageIO.read(file), file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pair<Image, File> readPrevious() {
        File file = Utility.getImage(-BATCHED - 1);

        imageStack.clear();
        for (int i = 0; i < BATCHED; i++) imageStack.add(loadNew());

        try {
            current = new Pair<>(ImageIO.read(file), file);
            return current;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pair<Image, File> read() {
        imageStack.add(loadNew());
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
