package io.github.toberocat.utils.selection;

import java.awt.*;

public record LabelSelection(
        int x,
        int y,
        int width,
        int height) {

    public static LabelSelection fromRect(Rectangle rectangle) {
        return new LabelSelection(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
