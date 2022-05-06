package io.github.toberocat;

import io.github.toberocat.async.AsyncTask;
import io.github.toberocat.callbacks.ExceptionCallback;
import io.github.toberocat.utils.Utility;
import io.github.toberocat.utils.selection.Pixelate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static io.github.toberocat.utils.DataUtility.join;

public class Censor {
    private final Queue<String> queue;

    public Censor(String[] files) {
        this.queue = new LinkedList<>();
        queue.addAll(List.of(files));

        for (int i = 0; i < 50; i++) {
            if (!queue.isEmpty()) censor(queue.remove());
        }
    }

    private void censor(String file) {
        AsyncTask.run((ExceptionCallback) () -> {
            BufferedImage img = Pixelate.pixelate(join(Launch.ORIGINAL_IMAGES_PATH, file), Utility.readLabel(file));
            ImageIO.write(img, "jpg", join(Launch.CENSORED_IMAGES_PATH, file));
        }).then(() -> {
            if (!queue.isEmpty()) censor(queue.remove());
        });
    }
}
