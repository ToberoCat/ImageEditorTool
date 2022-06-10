package io.github.toberocat.gui.image;

import io.github.toberocat.actions.ActionLog;
import io.github.toberocat.gui.listener.EventListener;
import io.github.toberocat.utils.DataUtility;
import io.github.toberocat.utils.Mathf;
import io.github.toberocat.utils.Mathi;
import io.github.toberocat.utils.Utility;
import io.github.toberocat.utils.selection.LabelSelection;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static io.github.toberocat.utils.Mathf.clampRound;

public class SelectionHandle {
    private static SelectionHandle HANDLE;
    private final ArrayList<LabelSelection> selections;
    private Rectangle current;

    private Point start;

    private SelectionHandle() {
        this.selections = new ArrayList<>();
        this.current = new Rectangle();
    }

    public static SelectionHandle handle() {
        if (HANDLE == null) HANDLE = new SelectionHandle();
        return HANDLE;
    }

    public void mousePress(MouseEvent event) {
        start = new Point(event.getX() - ImageRenderer.instance().getScrollX(),
                event.getY() - ImageRenderer.instance().getScrollY());
    }

    public void mouseDrag(MouseEvent event) {
        Point end = new Point(event.getX() - ImageRenderer.instance().getScrollX(),
                event.getY() - ImageRenderer.instance().getScrollY());

        Image image = ImageRenderer.instance().getImage();
        float width = image.getWidth(null) * ImageRenderer.instance().zoom(),
                height = image.getHeight(null) * ImageRenderer.instance().zoom();

        int startX = clampRound(Math.min(start.x, end.x), 0, width);
        int startY = clampRound(Math.min(start.y, end.y), 0, height);

        int endX = clampRound(Math.max(start.x, end.x), 0, width);
        int endY = clampRound(Math.max(start.y, end.y), 0, height);

        current = new Rectangle(startX, startY, endX - startX, endY - startY);
    }

    public void accept() {
        if (current.width <= 5 || current.height <= 5) return;

        LabelSelection selection = LabelSelection.fromRect(selection());
        ActionLog.logSelection(selection);

        selections.add(selection);
        DataUtility.createFile(EventListener.IMAGE_BATCH.getCurrent().c(), selection());
        current = new Rectangle();

        EventListener.dragX = 0;
        EventListener.dragY = 0;
        EventListener.x = 0;
        EventListener.y = 0;
    }

    public void cancel() {
        current = new Rectangle();
    }

    public Rectangle current() {
        return current;
    }

    public ArrayList<LabelSelection> getSelections() {
        return selections;
    }

    public Rectangle selection() {
        return new Rectangle(
                Math.round(current.x / ImageRenderer.instance().zoom()),
                Math.round(current.y / ImageRenderer.instance().zoom()),
                Math.round(current.width / ImageRenderer.instance().zoom()),
                Math.round(current.height / ImageRenderer.instance().zoom()));
    }

    public void reloadFromDisk() {
        selections.clear();
        selections.addAll(Utility.readLabel(EventListener.IMAGE_BATCH.getCurrent().c().getName()));
    }
}
