package io.github.toberocat.exceptions;

import java.io.File;

public class NoImagesAvailable extends RuntimeException {
    public NoImagesAvailable(File folder) {
        super("No images where found in: " + folder.getAbsolutePath());
    }
}
